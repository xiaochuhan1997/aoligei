package com.example.swagger.controller;

import com.example.swagger.common.R;
import com.example.swagger.service.ApiControllerService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
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
    private ApiControllerService apiControllerService;

//    @RequestMapping(method = RequestMethod.GET)
//    public R<Response> handleGetRequest(Request request) throws IOException {
//        // 处理GET请求
//        Response response = apiControllerService.GetRequestSender(request.getUrl(), request.getHeaders(), request.getParams());
//        return R.success(response);
//    }

    @PostMapping
    public R<Response> handlePostRequest(@RequestBody Request request) throws IOException {
        log.info(String.valueOf(request));
        if (request.getMethod().equals("get")) {
            Response response = apiControllerService.GetRequestSender(request.getUrl(), request.getHeaders(), request.getParams());
            return R.success(response);
        } else if (request.getMethod().equals("post")) {
            Response response = apiControllerService.PostRequestSender(request.getUrl(), request.getHeaders(), request.getParams());
            return R.success(response);
        }
        return R.error("不支持该类型请求");
    }

//    @RequestMapping(method = RequestMethod.PUT)
//    public ResponseEntity<String> handlePutRequest(@RequestBody String requestBody) {
//        // 处理PUT请求，requestBody包含请求体的内容
//        return ResponseEntity.ok("PUT request handled with request body: " + requestBody);
//    }
//
//    @RequestMapping(method = RequestMethod.DELETE)
//    public ResponseEntity<String> handleDeleteRequest() {
//        // 处理DELETE请求
//        return ResponseEntity.ok("DELETE request handled");
//    }
}