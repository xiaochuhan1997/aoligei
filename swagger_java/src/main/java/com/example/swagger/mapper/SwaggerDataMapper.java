package com.example.swagger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.swagger.entity.SwaggerData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SwaggerDataMapper extends BaseMapper<SwaggerData> {
    @Select("SELECT server_url, api_url, method, tag, summary, input_param, input_param_dec, output_param, output_param_dec, create_time, update_time FROM swagger_data WHERE id = #{id}")
    SwaggerData findInputOutputParamsById(Long id);
    @Insert("INSERT INTO swagger_data (method, input_param, input_param_dec, output_param, output_param_dec, api_url, server_url, tag,summary, case_no,case_dec) " +
            "VALUES (#{swaggerData.method}, #{swaggerData.inputParam}, #{swaggerData.inputParamDec}, #{swaggerData.outputParam}, #{swaggerData.outputParamDec}," +
            "#{swaggerData.apiUrl}, #{swaggerData.serverUrl}, #{swaggerData.tag}, #{swaggerData.summary}, #{swaggerData.caseNo}, #{swaggerData.caseDec})")
    int saveData(@Param("swaggerData") SwaggerData swaggerdata);
}
