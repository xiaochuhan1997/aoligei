package com.example.swagger.controller;

import com.example.swagger.common.R;
import com.example.swagger.entity.SwaggerData;
import com.example.swagger.service.SwaggerDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/swagger")

public class SwaggerDataController {
    @Autowired
    private SwaggerDataService swaggerDataService;

    @PostMapping("/analyze")//swagger解析
    public R<List<SwaggerData>> analyze(@RequestBody Object a) {
        log.info("解析接口开始:{}");
        List<SwaggerData> dataList = swaggerDataService.analyze();
        return R.success(dataList);
    }
    //swagger查询
    @PostMapping("/query")
    public R<List<SwaggerData>> query(@RequestBody Object a) {
        log.info("查询接口开始:{}");
        List<SwaggerData> dataList = swaggerDataService.list();
        return R.success(dataList);
    }
    @GetMapping
    private String list() {
        return "get ok";
    }
    //保存案例
    @PostMapping("/saveData")
    public SwaggerData saveData(@RequestBody Map<String, Object> requestData) {
        SwaggerData swaggerdata = new SwaggerData();
        swaggerdata.setMethod(String.valueOf(requestData.get("method")));
        swaggerdata.setInputParam(String.valueOf(requestData.get("inputParam")));
        swaggerdata.setOutputParam(String.valueOf(requestData.get("outputParam")));
        swaggerdata.setApiUrl(String.valueOf(requestData.get("apiUrl")));
        swaggerdata.setServerUrl(String.valueOf(requestData.get("serverUrl")));
        swaggerdata.setTag(String.valueOf(requestData.get("tag")));
        swaggerdata.setSummary(String.valueOf(requestData.get("summary")));
        swaggerdata.setCaseNo(String.valueOf(requestData.get("caseNo")));
        swaggerdata.setCaseDec(String.valueOf(requestData.get("caseDec")));
        swaggerdata.setOutputParamDec(String.valueOf(requestData.get("outputParamDec")));
        swaggerdata.setInputParamDec(String.valueOf(requestData.get("inputParamDec")));
        return swaggerDataService.saveData(swaggerdata);
    }

}
