package com.example.swagger.entity;

import lombok.Data;

@Data
public class Article {
    private Long id;
    private String title;
    private String logo;
    private String descn;
    private String createTime;
    private Long cid;
}