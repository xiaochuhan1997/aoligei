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

    @PostMapping("/sendJson")
    public ResponseEntity<String> sendJson(@RequestBody Map<String, Object> requestData) {
        log.info(String.valueOf(requestData));

        try {
            // 获取请求数据中的 id 参数
            Long id = Long.valueOf(String.valueOf(requestData.get("id")));
            // 根据 id 获取对应的 SwaggerData 对象
            SwaggerData result = swaggerDataService.getInputOutputParamsById(id);
            String serverUrl = result.getServerUrl();
            String apiUrl = result.getApiUrl();
            String method = result.getMethod();
            // 设置请求头，指定接受 JSON 格式的响应
            HttpHeaders headers = new HttpHeaders();

            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            // 创建一个 RestTemplate 对象，用于发送 HTTP 请求
            RestTemplate restTemplate = new RestTemplate();
            // 创建一个 HttpEntity 对象，包含请求头
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response;

            if (StringUtils.equalsIgnoreCase(method, "get")) {
                // 如果请求类型是 GET，使用 RestTemplate 的 exchange 方法发送 GET 请求
                response = restTemplate.exchange(serverUrl + apiUrl, HttpMethod.GET, entity, String.class);
                System.out.println("GET Request Sent");
            } else if (StringUtils.equalsIgnoreCase(method, "post")) {
                // 如果请求类型是 POST
                // 设置 Content-Type 请求头为 JSON
                headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                // 从请求数据中获取请求体，这里还需要进行修改
                String requestBody = requestData.get("requestBody").toString();
                // 创建一个包含请求体和请求头的 HttpEntity 对象
                entity = new HttpEntity<>(requestBody, headers);
                // 使用 RestTemplate 的 exchange 方法发送 POST 请求
                response = restTemplate.exchange(serverUrl + apiUrl, HttpMethod.POST, entity, String.class);
                System.out.println("POST Request Sent");
            } else {
                // 如果请求类型既不是 GET 也不是 POST，返回错误响应
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
