package com.denada.travpholer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by hgc on 5/28/2016.
 */
public class TblBaseAch implements Serializable {
    public String tba_id;
    public String tba_name;
    public String tba_desc;
    public String tba_icon;
    public String tba_path;

    public static TblBaseAch parseJson(String json_data){
        JSONObject obj = null;
        TblBaseAch model = new TblBaseAch();
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
    
    public static ArrayList<TblBaseAch> parseJsonArray(String json_data){
        JSONObject obj = null;
        ArrayList<TblBaseAch> list = new ArrayList<>();
        try {

            JSONArray array = new JSONArray(json_data);
            for(int i=0; i<array.length(); i++){
                obj = array.getJSONObject(i);

                TblBaseAch model = new TblBaseAch();
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
                list.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
