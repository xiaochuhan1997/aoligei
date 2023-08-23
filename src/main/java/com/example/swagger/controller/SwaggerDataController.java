package com.example.swagger.controller;

import com.example.swagger.common.R;
import com.example.swagger.entity.SwaggerData;
import com.example.swagger.entity.User;
import com.example.swagger.service.SwaggerDataService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.BagUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/swagger")

public class SwaggerDataController {
    @Autowired
    private SwaggerDataService swaggerDataService;

    @PostMapping
    public R<List<SwaggerData>> save(@RequestBody Object a) {
        log.info("解析接口开始:{}");
//        SwaggerData swaggerData = new SwaggerData();
//        swaggerData.setInputParam(a.get("inputParam").toString());
//        System.out.println(swaggerData);
//        swaggerDataService.save(swaggerData);
        List<SwaggerData> dataList = swaggerDataService.analyze();
//        List<SwaggerData> dataList = swaggerDataService.list();
        return R.success(dataList);
    }

    @GetMapping
    private String list() {
        return "get ok";
    }


}
