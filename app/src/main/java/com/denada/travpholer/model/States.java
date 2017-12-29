package com.denada.travpholer.model;

/**
 * Created by hgc on 5/16/2016.
 */
public class States extends Object{
    public String stateID;
    public String stateName;
    public String countryID;
    public String latitude;
    public String longitude;

    @Override
    public String toString() {
        return stateName;
    }
}
