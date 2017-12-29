package com.denada.travpholer.model.Response;

import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.City;
import com.denada.travpholer.model.TblLikes;
import com.denada.travpholer.model.States;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hgc on 4/16/2016.
 */
public class SearchResponse extends BaseModel {
    public List<TblLikes> rows;
    public ArrayList<States> states;
    public ArrayList<City> cities;
}
