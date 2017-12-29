package com.denada.travpholer.model.Response;

import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblUser;

import java.util.List;

/**
 * Created by hgc on 4/16/2016.
 */
public class RegionResponse extends BaseModel {
    public List<Country> rows;
}
