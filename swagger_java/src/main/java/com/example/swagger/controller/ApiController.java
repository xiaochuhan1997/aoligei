package com.example.swagger.controller;


import com.example.swagger.entity.SwaggerData;
import com.example.swagger.service.SwaggerDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "测试apiController",tags = {"接口"})
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
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation("发送Post请求")
    @PostMapping("/sendPostRequest")
    public ResponseEntity<String> sendPostRequest(@RequestBody Map<String, Object> request) {
//        Long id = Long.valueOf(String.valueOf(request.get("id")));
//      SwaggerData result = swaggerDataService.getInputOutputParamsById(id);
        String method = String.valueOf(request.get("methods"));
        //测试用json/url
        String json = String.valueOf(request.get("inputParam"));
        String url = String.valueOf(request.get("serverUrl"))+ request.get("apiUrl");
        if ("POST".equalsIgnoreCase(method)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            return restTemplate.postForEntity(url, entity, String.class);
        } else {
            return ResponseEntity.badRequest().body("Invalid method");
        }
    }
    @GetMapping("/getAll")
    public List<SwaggerData> getAllSwaggerData() {
        return swaggerDataService.list();
    }
}
