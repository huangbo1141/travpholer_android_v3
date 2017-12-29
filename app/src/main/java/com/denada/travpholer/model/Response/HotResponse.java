package com.denada.travpholer.model.Response;

import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.TblBaseAch;
import com.denada.travpholer.model.TblLikes;
import com.denada.travpholer.model.TblPhotos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hgc on 4/16/2016.
 */
public class HotResponse extends BaseModel {

    public ArrayList<TblPhotos> rows;
    public ArrayList<Country> rows_country;
    public ArrayList<TblBaseAch> newachlist;

    public void parseForRanks(){
        ArrayList<TblPhotos> temp = new ArrayList<>();
        for (int i=0; i<rows.size(); i++){
            TblPhotos tblPhotos = rows.get(i);
            tblPhotos.owner.setTblBaseRank();

            tblPhotos.setOrderComments();

            temp.add(tblPhotos);
        }
        rows = temp;

    }
}
