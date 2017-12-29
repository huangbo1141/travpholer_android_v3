package com.denada.travpholer.model;

import com.denada.travpholer.model.google.GoogleAddressComponent;

import java.util.ArrayList;

/**
 * Created by hgc on 7/21/2016.
 */
public class GooglePlace {

    public String reference;
    public String place_id;
    public String formatted_address;
    public String formatted_phone_number;
    public String lat;
    public String lon;
    public String icon;
    public String name;
    public ArrayList<String> types;
    public String xid;
    public ArrayList<GoogleAddressComponent> address_components;

    public String  googlePicture;
    public String distance;

}
