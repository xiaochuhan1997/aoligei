package com.example.swagger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.swagger.entity.SwaggerData;

import java.util.List;
import java.util.Map;

public interface SwaggerDataService extends IService<SwaggerData> {
    public List<SwaggerData> analyze();




}
