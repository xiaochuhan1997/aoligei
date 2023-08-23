package com.example.swagger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.swagger.entity.SwaggerData;

import java.util.List;

public interface QueryInerfaceService extends IService<SwaggerData> {

    public List<SwaggerData> queryinterface();
}
