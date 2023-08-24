package com.example.swagger.service;

import com.example.swagger.controller.ApiController;

import java.io.IOException;
import java.util.Map;

public interface ApiControllerService {
    public ApiController.Response GetRequestSender(String url, Map<String, String> headers, Map<String, String> params) throws IOException;

    public ApiController.Response PostRequestSender(String url, Map<String, String> headers, Map<String, String> params) throws IOException;


}
