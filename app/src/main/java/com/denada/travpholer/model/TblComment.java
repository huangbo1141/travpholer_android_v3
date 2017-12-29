package com.denada.travpholer.model;

import java.io.Serializable;

/**
 * Created by hgc on 4/16/2016.
 */
public class TblComment extends BaseModel implements Serializable{

    public String tc_id;
    public String tc_content;
    public String tc_tuid;
    public String tc_tpid;

    public String tc_name;

    public String action;

}
