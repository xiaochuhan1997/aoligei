package com.example.swagger.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.swagger.controller.ApiController;
import com.example.swagger.service.ApiControllerService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service

public class ApiControllerServiceImpl implements ApiControllerService {
    @Override
    public ApiController.Response GetRequestSender(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        // 构建参数字符串
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(params.get(key));
            }
        }

        String paramString = sb.toString();

        // 拼接完整的请求URL
        String requestUrl = url;
        if (paramString.length() > 0) {
            requestUrl += "?" + paramString;
        }

        // 建立连接
        URL obj = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // 设置请求方法
        con.setRequestMethod("GET");

        // 设置请求头信息
        if (headers != null) {
            for (String key : headers.keySet()) {
                con.setRequestProperty(key, headers.get(key));
            }
        }
        // 读取响应内容
        int responseCode = con.getResponseCode();
        Map<String, List<String>> headerTmp = con.getHeaderFields();

        Map<String, List<String>> header = new HashMap<>();
        headerTmp.forEach((key, value) -> {
            if (key != null) {
                header.put(key, value);
            }
        });

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer responseBody = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();
        ApiController.Response response = new ApiController.Response();
        response.setResponseCode(responseCode);
        response.setHeaders(header);
        response.setParams(responseBody);

        return response;
    }

    @Override
    public ApiController.Response PostRequestSender(String url, Map<String, String> headers, Map<String, String> params) throws IOException {

        // 建立连接
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // 设置请求方法
        con.setRequestMethod("POST");

        // 设置请求头信息
        con.setRequestProperty("Content-Type", "application/json");
        if (headers != null) {
            for (String key : headers.keySet()) {
                con.setRequestProperty(key, headers.get(key));
            }
        }

        // 启用输出
        con.setDoOutput(true);

        // 发送POST请求体的内容
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        String jsonParams = JSON.toJSONString(params);
//        String jsonParams = params.toString();
        wr.writeBytes(jsonParams);
        wr.flush();
        wr.close();

        // 读取响应内容
        int responseCode = con.getResponseCode();

        Map<String, List<String>> headerTmp = con.getHeaderFields();

        Map<String, List<String>> header = new HashMap<>();
        headerTmp.forEach((key, value) -> {
            if (key != null) {
                header.put(key, value);
            }
        });
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer responseBody = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();
        ApiController.Response response = new ApiController.Response();
        response.setResponseCode(responseCode);
        response.setHeaders(header);
        response.setParams(responseBody);
        return response;

    }
}
