package com.example.swagger.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test1 {
    public static List<String> globalList = new ArrayList<>();
    public static Map<String, String> globalMap = new HashMap<>();

    public static void traverse(JSONObject json, String path) {
        for (String key : json.keySet()) {
            Object value = json.get(key);
            String newPath = path + "." + key;
            if (value instanceof JSONObject) {
                traverse((JSONObject) value, newPath);
            } else if (value instanceof JSONArray) {

                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.size(); i++) {
                    Object obj = array.get(i);
                    if (obj instanceof JSONObject) {
                        traverse((JSONObject) obj, newPath + "[" + i + "]");
                    } else {
                        // do something for non-JSON object in array
                        newPath = newPath.replace(".$ref.properties", "").replace(".$ref", "").replaceFirst("\\.", "");
                        ;
                        globalMap.put(newPath, (String) array.get(i));
//                        System.out.println("array newPath:"+newPath);
//                        System.out.println("array:"+array.get(i));
                    }
                }
            } else {
                // do something for non-JSON object
                newPath = newPath.replace(".$ref.properties", "").replace(".$ref", "").replaceFirst("\\.", "");

                globalMap.put(newPath, (String) json.get(key));
//                System.out.println("object newPath:"+newPath);
//                System.out.println("object:"+json.get(key));
            }
        }
    }

    public static String getJson() {
        String jsonStr = "";
        try {
            File file = new File("D:\\swagger_java\\swagger\\src\\main\\resources\\333.json");
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

    public static void ccc(JSONObject json) {
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                if (key.equals("data")) {
                    JSONArray arrA = new JSONArray();
                    arrA.set(0,value);
                    json.put("data",arrA);
                    System.out.println("ok");
                    System.out.println(value);
                    System.out.println(json);
                }
                ccc((JSONObject) value);
            }
        }
    }

    public static void main(String[] args) {
        String jsonString = getJson();
        JSONObject json = JSON.parseObject(jsonString, Feature.DisableCircularReferenceDetect);
        traverse(json, "");
        System.out.println(globalMap);
        JSONObject aaa = new JSONObject();
        JSONObject bbb = new JSONObject();

        for (Map.Entry<String, String> entry : globalMap.entrySet()) {
            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
            JSONObject jsonObj = new JSONObject();
            if (entry.getValue().equals("object")) System.out.println("object");
            if (entry.getValue().equals("array")) System.out.println("array");
            String[] keys = entry.getKey().split("\\.");
            JSONObject parentObj = jsonObj;
            for (int i = 0; i < keys.length - 1; i++) {
                String key = keys[i];
                JSONObject childObj = new JSONObject();
                parentObj.put(key, childObj);
                parentObj = childObj;


            }
            parentObj.put(keys[keys.length - 1], entry.getValue());

            aaa = jsonMerge(jsonObj, aaa);
//            System.out.println(aaa);
//            System.out.println("-----------------");
//          获取outputparam功能
            JSONObject jsonObjTmp = new JSONObject();


            String replace = entry.getKey();
            if (entry.getKey().endsWith("type") || entry.getKey().endsWith("title") || entry.getKey().endsWith("description") || entry.getKey().endsWith("required")) {
                replace = replace.replace("type", "").replace("title", "").replace("description", "").replace("required", "");
            }
            if (replace.isEmpty()) {
                continue;
            }
            String[] keysReplace = replace.split("\\.");
            JSONObject parentObjTmp = jsonObjTmp;
            for (int i = 0; i < keysReplace.length - 1; i++) {
                String keyReplace = keysReplace[i];
                JSONObject childObj = new JSONObject();
                parentObjTmp.put(keyReplace, childObj);
                parentObjTmp = childObj;
            }
            parentObjTmp.put(keysReplace[keysReplace.length - 1], "");

//            System.out.println(jsonObjTmp);
            bbb = jsonMerge(jsonObjTmp, bbb);

//            System.out.println(bbb);
//            System.out.println("--------------");


//             aaa = jsonMerge(jsonObj, aaa);

        }

        ccc(bbb);
//        System.out.println(aaa);
        System.out.println(bbb);

    }

    // 覆盖原有json功能
    public static JSONObject jsonMerge(JSONObject source, JSONObject target) {
        // 覆盖目标JSON为空，直接返回覆盖源
        if (target == null) {
            return source;
        }

        for (String key : source.keySet()) {
            Object value = source.get(key);
            if (!target.containsKey(key)) {
                target.put(key, value);
            } else {
                if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject) value;
                    JSONObject targetValue = jsonMerge(valueJson, target.getJSONObject(key));
                    target.put(key, targetValue);
                } else if (value instanceof JSONArray) {
                    JSONArray valueArray = (JSONArray) value;
                    for (int i = 0; i < valueArray.size(); i++) {
                        JSONObject obj = (JSONObject) valueArray.get(i);
                        JSONObject targetValue = jsonMerge(obj, (JSONObject) target.getJSONArray(key).get(i));
                        target.getJSONArray(key).set(i, targetValue);
                    }
                } else {
                    if (!target.containsKey(key)) {
                        target.put(key, value);
                    }
                }
            }
        }
        return target;
    }

}
