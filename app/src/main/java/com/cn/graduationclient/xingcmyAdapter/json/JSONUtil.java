package com.cn.graduationclient.xingcmyAdapter.json;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.List;

public class JSONUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转化为json字符串
     * @param data
     * @return
     */
    public static String ObjectToJson(Object data) throws IOException {
        try{
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json字符串转化为对象
     * @param jsonData
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T> T JsonToObject(String jsonData, Class<T> beanType){
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json字符串数据转化为list对象
     * @param jsonData
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToListObject(String jsonData, Class<T> beanType){
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
