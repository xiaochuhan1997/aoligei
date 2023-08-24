package com.example.swagger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.swagger.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
