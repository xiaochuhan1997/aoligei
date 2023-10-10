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

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
//            ResponseEntity<String> response;
            String targetUrl = "http://"+serverUrl + apiUrl;
//            String targetUrl = serverUrl;
            String jsonData = result.getInputParam();
            if (StringUtils.equalsIgnoreCase(method, "get")) {
               try {
                   // 对 JSON 数据进行 URL 编码
                   String encodedData = URLEncoder.encode(jsonData, "UTF-8");
                   // 构建带有 JSON 数据的完整 URL
                   String completeUrl = targetUrl + "?data=" + encodedData;
                   // 创建 URL 对象
                   URL url = new URL(completeUrl);
                   // 打开连接
                   HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                   // 设置请求方法为 GET
                   connection.setRequestMethod("GET");
                   // 获取响应状态码
                   int responseCode = connection.getResponseCode();
                   // 读取响应内容
                   BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                   String line;
                   StringBuffer response = new StringBuffer();
                   while ((line = reader.readLine()) != null) {
                       response.append(line);
                   }
                   reader.close();
                   // 处理响应内容
                   System.out.println("Response Code: " + responseCode);
                   System.out.println("Response Body: " + response.toString());
                   // 关闭连接
                   connection.disconnect();
               }catch (IOException e) {
                   e.printStackTrace();
               }
            } else if (StringUtils.equalsIgnoreCase(method, "post")) {
                try {
                    // 创建 URL 对象
                    URL url = new URL(targetUrl);
                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方法为 POST
                    connection.setRequestMethod("POST");
                    // 启用输入输出流
                    connection.setDoOutput(true);
                    // 设置请求头属性
                    connection.setRequestProperty("Content-Type", "application/json");
                    // 将 JSON 数据写入请求体
                    try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                        outputStream.writeBytes(jsonData);
                        outputStream.flush();
                    }
                    // 获取响应状态码
                    int responseCode = connection.getResponseCode();
                    // 读取响应内容
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    // 处理响应内容
                    System.out.println("Response Code: " + responseCode);
                    System.out.println("Response Body: " + response.toString());
                    // 关闭连接
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
 } else {
                // 如果请求类型既不是 GET 也不是 POST，返回错误响应
                System.out.println("Invalid method");
                return ResponseEntity.badRequest().build();
            }

            // 处理响应数据
//            return response;
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            // 异常处理
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }
}
