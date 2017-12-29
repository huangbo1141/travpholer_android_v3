package com.denada.travpholer.model;

import com.denada.travpholer.Doc.CGlobal;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hgc on 4/16/2016.
 */
public class TblUser extends BaseModel implements Serializable{

    public String tu_id;
    public String tu_username;

    public String tu_firstname;
    public String tu_lastname;

    public String tu_email;
    public String tu_password;
    public String tu_type;
    public String tu_apnid;
    public String tu_gcmid;
    public String tu_pic;
    public String tu_role;

    public String action;
    public String tu_likesrecv;

    public TblBaseRank tblBaseRank;

    public String getUsername(){
        if (tu_firstname==null)
            tu_firstname = "";
        if (tu_lastname==null)
            tu_lastname = "";
        return tu_firstname +" "+ tu_lastname;
    }

    public void setTblBaseRank(){
//        return;
        int likesrecv = 0;
        if (tu_likesrecv != null){
            likesrecv = Integer.valueOf(tu_likesrecv);
        }
        for (int i=0; i< CGlobal.rows_base_rank.size(); i++){
            TblBaseRank item = CGlobal.rows_base_rank.get(i);
            int itemlikes = Integer.valueOf(item.tbr_like);
            if (likesrecv < itemlikes){
                if (i == 0){
                    tblBaseRank = CGlobal.rows_base_rank.get(0);
                }else{
                    tblBaseRank = CGlobal.rows_base_rank.get(i-1);
                }
                break;
            }
        }
    }
}
