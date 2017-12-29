package com.denada.travpholer.model;

import java.io.Serializable;

/**
 * Created by hgc on 5/16/2016.
 */
public class City extends Object implements Serializable{
    public String cityID;
    public String cityName;
    public String stateID;
    public String latitude;
    public String longitude;


    @Override
    public String toString() {
        return cityName;
    }
}
