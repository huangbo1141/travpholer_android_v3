package com.denada.travpholer.model.google;

import java.util.List;

/**
 * Created by Administrator on 9/1/2016.
 */
public class GooglePlace {

    public List<GoogleAddressComponent> address_components;
    public String formatted_address;
    public String place_id;
    public List<String> types;

    public  GoogleGeometry geometry;
}
