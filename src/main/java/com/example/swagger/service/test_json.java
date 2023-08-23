package com.example.swagger.service;


import com.example.swagger.utils.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @author superboo
 * @version 3.0
 * @company
 * @date 2014-5-21 上午09:45:51
 */
public class test_json {
    public static JSONObject root;

    @SuppressWarnings("rawtypes")
    public void analysisJson(Object objJson) {

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
                        System.out.println(ref);
                        Object jsonObjectTmp = root.getJSONObject("components").getJSONObject("schemas").get(ref);
                        ((JSONObject) object).put("$ref", jsonObjectTmp);
                        System.out.println(root);
                    }
                    analysisJson((JSONObject) object);

                }
                //如果key中是其他
            }

        }

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

    public static Map find(Map map) {
        System.out.println("------------------");
        map.forEach((key, value) -> {
            System.out.println(key);
            System.out.println(value);
            System.out.println(value.getClass());
            if (value instanceof Map){
                find((Map)value ) ;
            }
            if (value instanceof ArrayList){
                ArrayList valueArray=(ArrayList)value;
                for (int i = 0; i < valueArray.size(); i++) {
                    find((Map) valueArray.get(i));
                }
            };
        });

        return map;
    }

    public static void main(String[] args) throws IOException {
        String jsonString = "{\n" + "  \"sites\": {\n" + "    \"site\": [\n" + "      {\n" + "        \"id\": \"1\",\n" + "        \"name\": \"菜鸟教程\",\n" + "        \"url\": \"www.runoob.com\"\n" + "      },\n" + "      {\n" + "        \"id\": \"2\",\n" + "        \"name\": \"菜鸟工具\",\n" + "        \"url\": \"c.runoob.com\"\n" + "      },\n" + "      {\n" + "        \"id\": \"3\",\n" + "        \"name\": \"Google\",\n" + "        \"url\": \"www.google.com\"\n" + "      }\n" + "    ]\n" + "  }\n" + "}";
        Map map = JsonUtils.json2Map(jsonString);
        System.out.println(map);
        Map map1 = find(map);
        //// 创建 ObjectMapper 对象
//        ObjectMapper objectMapper = new ObjectMapper();
//
//// 将 JSON 字符串解析为 Map
//        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
//        System.out.println(map);
    }


}