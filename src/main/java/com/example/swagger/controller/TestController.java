package com.example.swagger.controller;

import com.example.swagger.common.R;
import com.example.swagger.service.TestService;
import com.example.swagger.service.impl.SwaggerDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private SwaggerDataServiceImpl swaggerDataService;

    //    @GetMapping
//    public List<SwaggerData> test() {
//        Test test = new Test();
//        List<SwaggerData> analyze = swaggerDataService.analyze();
//        test.setJson(json);
//        testService.save(test);
//        return analyze;
//    }
    @GetMapping
    public ResponseEntity<String> handleGetRequest( @RequestParam Map<String, Object> map) {
        // 处理GET请求
        log.info("发送get请求成功参数为"+ map.toString());
        return ResponseEntity.ok(map.toString());
    }
    @PostMapping
    public R<Map<String, Object>> handlePostRequest(@RequestBody Map<String, Object> map) {
        // 处理GET请求
        log.info("发送Post请求成功参数为"+ map.toString());
        return R.success(map);
    }
}
