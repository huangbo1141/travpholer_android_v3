package com.denada.travpholer.model;

import java.io.Serializable;

/**
 * Created by hgc on 4/16/2016.
 */
public class SearchTerm implements Serializable{

    public String radius;
    public String tp_id_viewtop;
    public String tp_id_viewbottom;
    public String tp_steps = "200";
    public String tp_fetcharrow;
    public String tp_countryid;
	public String visitor_id;

    public String create_datetime;
    public String tu_id;

    public String tp_ids;
}
