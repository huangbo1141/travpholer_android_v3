package com.denada.travpholer.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hgc on 4/16/2016.
 */
public class BaseModel {
    public String create_datetime;
    public String modify_datetime;
    public String sqlmode;
    public String sql;

    public ImageDimen imginfo;

    public int response;
    public String error;


    public class ImageDimen{
        public String tp_width;
        public String tp_height;
    }

    public static Map<String, String> getQueryMap(Object obj){
        Map<String, String> ret = new HashMap<>();
        for (Field f: obj.getClass().getDeclaredFields()){
            try {
                String name = f.getName();
                String value = (String) f.get(obj);
                if (value!=null)
                    ret.put(name,value);
            }catch (Exception ex){

            }

        }
        return ret;
    }

    public static String buildJson(Object obj){
        JSONObject jsonObject = new JSONObject();
        for (Field f: obj.getClass().getDeclaredFields()){
            try {
                String name = f.getName();
                String value = (String) f.get(obj);
                if (value!=null) {
                    jsonObject.put(name,value);
                }
            }catch (Exception ex){

            }

        }
        return jsonObject.toString();
    }
}
