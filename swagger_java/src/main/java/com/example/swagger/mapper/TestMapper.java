package com.example.swagger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.swagger.entity.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<Test> {
}
