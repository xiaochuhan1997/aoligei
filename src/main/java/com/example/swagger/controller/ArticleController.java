package com.example.swagger.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.swagger.entity.Article;
import com.example.swagger.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/list")
    public Page<Article> findAll(Long pageIndex, Long pageSize){
        Page<Article> page = articleMapper.selectPage(new Page(pageIndex, pageSize), null);
        return page;
    }

    @GetMapping("/list_custom")
    public Page<Article> customFindAll(Long pageIndex, Long pageSize, String title){
        Page<Article> page = articleMapper.selectByPage(new Page(pageIndex, pageSize), title);
        return page;
    }
}