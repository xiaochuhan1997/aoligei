package com.example.swagger.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.swagger.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    Page<Article> selectByPage(Page<Article> page, @Param("title") String title);
}