package com.example.swagger.controller;


import com.alibaba.druid.util.StringUtils;
import com.example.swagger.entity.SwaggerData;
import com.example.swagger.service.SwaggerDataService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.io.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {
    @Data
    public static class Request implements Serializable {

        private String url;
        private String method;
        private Map<String, String> headers;
        private Map<String, String> params;
    }

    @Data
    public static class Response {
        private int responseCode;
        private Map<String, List<String>> headers;
        private StringBuffer params;
    }
    @Autowired
    private SwaggerDataService swaggerDataService;
//    @Autowired
//    private ApiControllerService apiControllerService;

//    @RequestMapping(method = RequestMethod.GET)
//    public R<Response> handleGetRequest(Request request) throws IOException {
//        // 处理GET请求
//        Response response = apiControllerService.GetRequestSender(request.getUrl(), request.getHeaders(), request.getParams());
//        return R.success(response);
//    }



    @PostMapping("/sendJson")
    public ResponseEntity<String> sendJson(@RequestBody Map<String, Object> requestData) {
        log.info(String.valueOf(requestData));

        try {
            Long id = Long.valueOf(String.valueOf(requestData.get("id")));
            SwaggerData result = swaggerDataService.getInputOutputParamsById(id);
            String serverUrl = result.getServerUrl();
            String apiUrl = result.getApiUrl();
            String method = result.getMethod();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response;

            if (StringUtils.equalsIgnoreCase(method, "get")) {
                response = restTemplate.exchange(serverUrl + apiUrl, HttpMethod.GET, entity, String.class);
                System.out.println("GET Request Sent");
            } else if (StringUtils.equalsIgnoreCase(method, "post")) {
                headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                String requestBody = requestData.get("requestBody").toString();
                entity = new HttpEntity<>(requestBody, headers);
                response = restTemplate.exchange(serverUrl + apiUrl, HttpMethod.POST, entity, String.class);
                System.out.println("POST Request Sent");
            } else {
                System.out.println("Invalid method");
                return ResponseEntity.badRequest().build();
            }

            // 处理响应数据
            return response;
        } catch (Exception e) {
            // 异常处理
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }
}
