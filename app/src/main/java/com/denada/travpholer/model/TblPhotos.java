package com.denada.travpholer.model;

import com.denada.travpholer.mapmarker.MarkerManager;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hgc on 4/16/2016.
 */
public class TblPhotos implements Serializable{
    public String tp_id;
    public String tp_title;
    public String tp_location;

    public String tp_picpath;
    public String tu_id;
    public String tp_width;
    public String tp_height;
    public String tp_thumb;
    public String tp_hyperlink;
    public String tp_hyperlink_clicks;
    public String tp_countryid;
    public String tp_country;
    public String tp_share_clicks;
    public String create_datetime;
    public String modify_datetime;

    public String tp_lat;
    public String tp_lon;


    public TblLikes project;


    public String likes;
    public String likescount;
    public String ilikethis;
    public TblUser owner;

    public String action;

//    public List<TblComment> comments = new ArrayList<>();

    public TblPhotos(TblPhotos tblPhotos){
        for (Field f: tblPhotos.getClass().getDeclaredFields()){
            try {
                String name = f.getName();
                Object value = f.get(tblPhotos);
                if (value!=null) {
                    Field p = this.getClass().getDeclaredField(name);
                    p.set(this,value);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    public TblPhotos(){

    }

    public void setOrderComments(){
//        if (comments.size()>0){
//            Collections.sort(comments, new Comparator<TblComment>() {
//                @Override
//                public int compare(TblComment lhs, TblComment rhs) {
//                    int first = Integer.valueOf(lhs.tc_id);
//                    int second =Integer.valueOf(rhs.tc_id);
//
//                    return first-second;
//                }
//            });
//        }
    }

    public String commentcount;
}

