package com.example.swagger.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.swagger.entity.SwaggerData;
import com.example.swagger.mapper.SwaggerDataMapper;
import com.example.swagger.service.SwaggerDataService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.json.JSONSerializer;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SwaggerDataServiceImpl extends ServiceImpl<SwaggerDataMapper, SwaggerData> implements SwaggerDataService {
    public static JSONObject root;

    public List<SwaggerData> analyze() {
        List<SwaggerData> swaggerDataList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        root = JSONObject.fromObject(getJson());
        analysisJson(root);
        String servers = root.getJSONArray("servers").getJSONObject(0).get("url").toString();
        Iterator paths = root.getJSONObject("paths").keys();
        while (paths.hasNext()) {
            SwaggerData swaggerData = new SwaggerData();
            //获取服务地址
            swaggerData.setServerUrl(servers);
            //获取API地址
            String apiUrl = paths.next().toString();
            swaggerData.setApiUrl(apiUrl);
            //获取请求方式
            String method = root.getJSONObject("paths").getJSONObject(apiUrl).keys().next().toString();
            swaggerData.setMethod(method);
            JSONObject tmp = root.getJSONObject("paths").getJSONObject(apiUrl).getJSONObject(method);
            //获取标签
            String tags = tmp.getJSONArray("tags").get(0).toString();
            swaggerData.setTag(tags);
            //获取描述
            String summary = tmp.get("summary").toString();
            swaggerData.setSummary(summary);
            //获取输入参数
            if (method.equals("get") && tmp.has("parameters")) {
                // 需要将输入参数分割为参数和描述
                String parametersTmp = tmp.get("parameters").toString();

                // 将 JSON 字符串转换为 JSONArray 对象
                JSONArray jsonArray = JSONArray.fromObject(parametersTmp);

                // 提取第一个 JSON 对象的 name 字段，并生成新的 JSON 字符串 就是参数
                JSONObject parameters = new JSONObject();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    parameters.put(jsonObject.getString("name"), "");
                }
                String result1 = parameters.toString();
                System.out.println(result1);
                // 提取第二个 JSON 对象的 name 字段，并生成新的 JSON 字符串 就是描述

                JSONArray parametersDec = new JSONArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject schema = jsonObject.getJSONObject("schema");
                    JSONObject json2 = new JSONObject();
                    json2.put("name", jsonObject.getString("name"));
                    json2.put("description", jsonObject.getString("description"));
                    json2.put("required", jsonObject.getBoolean("required"));
                    json2.put("type", schema.getString("type"));
                    parametersDec.add(json2);
                }
                swaggerData.setInputParam(parameters.toString());
                swaggerData.setInputParamDec(parametersDec.toString());
            } else if (method.equals("post") && tmp.has("requestBody")) {
                String parameters = tmp.getJSONObject("requestBody").getJSONObject("content").getJSONObject("application/json").get("schema").toString();
                swaggerData.setInputParam(parameters);
            }
            //获取输出参数
            if (tmp.getJSONObject("responses").toString().contains("*/*")) {
                String responses_tmp = tmp.getJSONObject("responses").getJSONObject("200").getJSONObject("content").getJSONObject("*/*").getJSONObject("schema").toString();
                String responses = StringEscapeUtils.unescapeJavaScript(responses_tmp);
                swaggerData.setOutputParam(responses);

            } else if (tmp.getJSONObject("responses").toString().contains("application/json")) {
                String responses = tmp.getJSONObject("responses").getJSONObject("200").getJSONObject("content").getJSONObject("application/json").getJSONObject("schema").getJSONObject("$ref").toString();
                swaggerData.setOutputParam(responses);
            }

            //将数据加入列表中
//            swaggerDataList.add(swaggerData);


        }
        //将数据存入数据库
//        this.saveBatch(swaggerDataList);
        return swaggerDataList;
    }


    public static String getJson() {
        String jsonStr = "";
        try {
            File file = new File("D:\\swagger_java\\swagger\\src\\main\\resources\\11.json");
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file), "Utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            return null;
        }
    }

    public static void analysisJson(Object objJson) {

        //如果obj为json数组
        if (objJson instanceof JSONArray) {
            JSONArray objArray = (JSONArray) objJson;
            for (int i = 0; i < objArray.size(); i++) {
                analysisJson(objArray.get(i));
            }
        }
        //如果为json对象
        else if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;

            Iterator it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                //如果得到的是数组
                if (object instanceof JSONArray) {
                    JSONArray objArray = (JSONArray) object;
                    analysisJson(objArray);
                }
                //如果key中是一个json对象
                else if (object instanceof JSONObject) {
                    if (((JSONObject) object).has("$ref")) {
                        String[] refs = ((JSONObject) object).get("$ref").toString().split("/");
                        String ref = refs[refs.length - 1];
//                        System.out.println(ref);
                        Object jsonObjectTmp = root.getJSONObject("components").getJSONObject("schemas").get(ref);
                        ((JSONObject) object).put("$ref", jsonObjectTmp);
//                        System.out.println(root);
                    }
                    analysisJson((JSONObject) object);

                }
                //如果key中是其他
            }

        }
    }


}
