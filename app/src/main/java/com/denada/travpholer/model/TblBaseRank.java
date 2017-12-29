package com.denada.travpholer.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by hgc on 5/28/2016.
 */
public class TblBaseRank implements Serializable{
    public String tbr_id;
    public String tbr_like;
    public String tbr_rank;
    public String tbr_title;
    public String tbr_icon;
    public String tbr_path;

    public static TblBaseRank parseJson(String json_data){
        JSONObject obj = null;
        TblBaseRank model = new TblBaseRank();
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
