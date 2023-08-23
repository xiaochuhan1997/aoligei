package com.example.swagger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.swagger.entity.SwaggerData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SwaggerDataMapper extends BaseMapper<SwaggerData> {
    @Select("select * from swagger_data limit #{pageNum}, #{pageSize}")
    List<SwaggerData> selectPage(Integer pageNum,Integer pageSize);
}
