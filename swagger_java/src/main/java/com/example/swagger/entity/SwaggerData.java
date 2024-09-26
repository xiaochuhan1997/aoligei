package com.example.swagger.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("Swagger实体类")
public class SwaggerData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("服务器url")
    private String serverUrl;
    @ApiModelProperty("APIurl")
    private String apiUrl;
    @ApiModelProperty("请求方式")
    private String method;
    @ApiModelProperty("标签")
    private String tag;
    @ApiModelProperty("总结")
    private String summary;
    @ApiModelProperty("入参")
    private String inputParam;
    @ApiModelProperty("入参详细")
    private String inputParamDec;
    @ApiModelProperty("出参")
    private String outputParam;
    @ApiModelProperty("出参详细")
    private String outputParamDec;
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

//    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private Long createUser;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新人")
    private Long updateUser;

    @ApiModelProperty("案例号")
    private String caseNo;

    @ApiModelProperty("案例描述")
    private String caseDec;



}
