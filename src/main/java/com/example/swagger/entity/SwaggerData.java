package com.example.swagger.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SwaggerData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String serverUrl;
    private String apiUrl;
    private String method;
    private String tag;
    private String summary;
    private String inputParam;
    private String inputParamDec;

    private String outputParam;
    private String outputParamDec;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

//    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;



}
