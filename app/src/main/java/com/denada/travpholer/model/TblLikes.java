package com.denada.travpholer.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import retrofit.http.PUT;

/**
 * Created by hgc on 4/16/2016.
 */
public class TblLikes implements Serializable{
    public String tl_id;

    public String tp_id;
    public String tu_id;
    public String tu_like;
    public String create_datetime;
    public String modify_datetime;

    public String tl_name;

    public static TblLikes parseJson(String json_data){
        JSONObject obj = null;
        TblLikes model = new TblLikes();
        try {
            obj = new JSONObject(json_data);
            for (Field f: model.getClass().getDeclaredFields()){
                try {
                    String name = f.getName();
                    if (obj.has(name)){
                        String value = obj.getString(name);
                        f.set(model,value);
                    }
                }catch (Exception ex){
                    model = null;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

}
