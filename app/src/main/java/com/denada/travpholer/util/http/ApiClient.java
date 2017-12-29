// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 5/18/2015 5:57:30 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package com.denada.travpholer.util.http;


import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Response.CommentResponse;
import com.denada.travpholer.model.Response.HotResponseForNotification;
import com.denada.travpholer.model.Response.RegionResponse;
import com.denada.travpholer.model.Response.ChatListResponse;
import com.denada.travpholer.model.Response.FaqResponse;
import com.denada.travpholer.model.Response.IpResponse;
import com.denada.travpholer.model.Response.LoginResponse;
import com.denada.travpholer.model.Response.MapResponse;
import com.denada.travpholer.model.Response.ActionResponse;
import com.denada.travpholer.model.Response.HotResponse;
import com.denada.travpholer.model.Response.SearchResponse;
import com.denada.travpholer.model.google.GooglePlaceDetailResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit.mime.TypedFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public class ApiClient {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Constants.url)
                    .addConverterFactory(GsonConverterFactory.create());

    public static interface ApiInterface
    {
        @GET(Constants.ACTION_USERINFO)
        Call<LoginResponse> onUserInfo(@QueryMap Map<String, String> query1);

    	@GET(Constants.ACTION_LOGIN)
        Call<LoginResponse> onLogin(@QueryMap Map<String, String> query1,@Query("action") String action);

        @Multipart
        @POST(Constants.ACTION_UPLOAD)
        Call<BaseModel> onUploadFile(@Part("uploaded_file") TypedFile file);

        @GET(Constants.ACTION_HOT)
        Call<HotResponse>  onLoadHot(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_FRESH)
        Call<HotResponse>   onLoadFresh(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_TOGO)
        Call<HotResponse>   onLoadTogo(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_CONQUERED)
        Call<HotResponse>   onLoadConquered(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_CONQUERED_COUNTRY)
        Call<RegionResponse>   onLoadConqueredCountry(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_TOGO_COUNTRY)
        Call<RegionResponse> onLoadTogoCountry(@QueryMap Map<String, String> query1);



        @GET(Constants.ACTION_LIKEPIC)
        Call<ActionResponse>  onActionLikePic(@QueryMap Map<String, String> query1);



        @GET(Constants.ACTION_REPORT)
        Call<ActionResponse> onActionReport(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_MAKEPOST)
        Call<HotResponse> onActionMakePost(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_COMMENT)
        Call<ActionResponse> onActionComment(@QueryMap Map<String, String> query1);

        @GET(Constants.ACTION_LOADNOTI)
        Call<HotResponseForNotification> onActionLoadNoti(@QueryMap Map<String, String> query1);


        @GET("assets/rest/apns/{phpfile}")
        Call<HotResponse> onTemplateRequestForHotResponse(@Path("phpfile") String phpfile,@QueryMap Map<String, String> query1);

        @GET("assets/rest/apns/{phpfile}")
        Call<LoginResponse> onTemplateRequest(@Path("phpfile") String phpfile,@QueryMap Map<String, String> query1);

        @GET("assets/rest/apns/{phpfile}")
        Call<ActionResponse> onTemplateRequestForActionResponse(@Path("phpfile") String phpfile,@QueryMap Map<String, String> query1);

        @GET("assets/rest/apns/{phpfile}")
        Call<CommentResponse> onTemplateRequestForCommentResponse(@Path("phpfile") String phpfile,@QueryMap Map<String, String> query1);
    }


    public ApiClient()
    {
    }

//    public static String url = "http://54.251.62.201";
//    public static String url = "http://cwcadmin.cwcnetwork.com.my";
//      public static String url = "http://cwcnetwork.com.my";



    private static int delaytimeout = 120;
    private static int connecttimeout = 10;

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static ApiInterface getApiClient()
    {
        if(apiService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.url)
                    .addConverterFactory(GsonConverterFactory.create())
                            .build();
            apiService = retrofit.create(ApiInterface.class);

//            apiService = (ApiInterface)(new retrofit.RestAdapter.Builder()).setEndpoint(Constants.url).setClient(new OkClient(okhttpclient)).build().create(ApiInterface.class);
        }
        return apiService;
    }

    public static ApiIpInterface getIpClient(String url)
    {
        if(url == null)
            return null;
        ApiIpInterface apiIpInterface = apiServices.get(url);
        if(apiIpInterface == null)
        {


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
            apiIpInterface = retrofit.create(ApiIpInterface.class);


            apiServices.put(url,apiIpInterface);
        }
        return apiIpInterface;
    }
    private static ApiInterface apiService;
    private static Map<String, ApiIpInterface> apiServices = new HashMap<>();
    public static interface ApiIpInterface {
        @GET("json")
        Call<IpResponse> onGetLocation();

        @GET("api/geocode/json")
        Call<MapResponse> onGetLocationByMap(@Query("address") String address,@Query("sensor") String sensor, Callback<MapResponse> callback);

        @GET("api/geocode/json")
        Call<MapResponse> onGetCityCountry(@Query("latlng") String latlng,@Query("sensor") String sensor,@Query("language") String lang);

        @GET("api/place/details/json")
        Call<GooglePlaceDetailResult> onGetPlaceDetail(@Query("placeid") String placeid, @Query("key") String key, @Query("language") String lang);
    }
}
//OkHttpClient okhttpclient = new OkHttpClient.Builder()
//        .readTimeout(delaytimeout, TimeUnit.SECONDS)
//        .connectTimeout(connecttimeout, TimeUnit.SECONDS)
//        .build();