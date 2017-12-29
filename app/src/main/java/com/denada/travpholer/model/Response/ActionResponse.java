package com.denada.travpholer.model.Response;

import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.model.TblLikes;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblReportAbuse;
import com.denada.travpholer.model.TblUser;

/**
 * Created by hgc on 4/16/2016.
 */
public class ActionResponse extends BaseModel {
    public TblLikes tblLikes;
    public TblReportAbuse tblReportAbuse;
    public TblPhotos tblPhotos;

    public TblComment tblComment;

}
