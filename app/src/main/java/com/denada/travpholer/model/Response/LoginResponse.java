package com.denada.travpholer.model.Response;

import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.TblBaseAch;
import com.denada.travpholer.model.TblBaseRank;
import com.denada.travpholer.model.TblUser;

import java.util.List;

/**
 * Created by hgc on 4/16/2016.
 */
public class LoginResponse extends BaseModel{
    public TblUser row;

    public TblBaseRank info_rank;
    public TblBaseRank info_rank_next;

    public List<TblBaseAch> achievements;

    public String likes_left;
    public String upload_photo;
    public String conqueredcount;
    public String likes_recv;
    public String likedplaces;
    public String shareclicks;

    public String action = "";

    public List<TblBaseAch> rows_base_ach;
    public List<TblBaseRank> rows_base_rank;


}
