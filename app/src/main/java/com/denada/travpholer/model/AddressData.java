package com.denada.travpholer.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by hgc on 6/10/2016.
 */
public class AddressData {
    public String address;
    public String city;
    public String country;
	public String city_hubo2;
	public String city_hubo1;

    public ArrayList<String> city_hubos = new ArrayList<>();
    public LatLng pos;

    public int type = -1;

    public void setAddressData(){

        if (country!=null && !country.isEmpty() && city_hubos.size()>0){
            String string = city_hubos.get(0);
            address = string +","+country;
        }

    }

    public String placeId;
}
