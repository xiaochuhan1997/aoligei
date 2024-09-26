package com.example.swagger.utils;


/**
 * designed by xch
 * 输入: 需要去除"$ref:"{}的字符串(String类型)
 * 返回: String
 **/

public class JsonRefRemover {
    public static String modifyInput(String input) {

        //寻找""$ref":{"
        int index = input.indexOf("\"$ref\":{");
        while (index != -1) {
            // 删除 "$ref":{
            input = input.substring(0, index) + input.substring(index + 8);
            //设立flag，用于寻找"$ref": 后"{" 对应的 "}" 这里默认入参的json中"{"的个数等于"}"
            int flag = 1;
            int i = index;
            while (flag != 0 && i < input.length()) {
                char currentChar = input.charAt(i);
                i++;

                if (currentChar == '{') {
                    flag++;
                } else if (currentChar == '}') {
                    flag--;
                }
            }

            // 删除匹配的 "}"
            input = input.substring(0, i - 1) + input.substring(i);
            // 继续寻找下一个 "$ref": {
            index = input.indexOf("\"$ref\":{");


        }


//    }
        return input;
    }
}
