package com.denada.travpholer.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by hgc on 5/16/2016.
 */
public class Country extends Object implements Serializable{
    public String countryID;
    public String countryName;
    public String localName;
    public String webCode;
    public String region;
    public String continent;
    public String latitude;
    public String longitude;
    public String surfaceArea;
    public String population;
    public String photocount;
    public String tp_picpath;
    public String tp_thumb;

    public Bitmap map_bitmap;

    @Override
    public String toString() {
        return countryName;
    }
}
