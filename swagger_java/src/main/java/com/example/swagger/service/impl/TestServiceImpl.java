package com.example.swagger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.swagger.entity.Test;
import com.example.swagger.mapper.TestMapper;
import com.example.swagger.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper,Test> implements TestService {
}
