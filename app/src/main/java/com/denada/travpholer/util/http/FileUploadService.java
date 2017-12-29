package com.denada.travpholer.util.http;

import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.model.BaseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by hgc on 5/28/2016.
 */
public interface FileUploadService {
    @Multipart
    @POST(Constants.ACTION_UPLOAD)
    Call<BaseModel> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);
}
