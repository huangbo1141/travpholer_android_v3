package com.denada.travpholer.model.Response;

import com.denada.travpholer.model.google.GoogleAddressComponent;

import java.util.List;

/**
 * Created by hgc on 5/5/2016.
 */
public class MapResponse {
    public List<Temp1> results;
    public class Temp1{
        public List<GoogleAddressComponent> address_components;
        public String formatted_address;
        public String place_id;
        public List<String> types;
    }
    public String status;
}
