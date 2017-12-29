/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.denada.travpholer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Response.LoginResponse;
import com.denada.travpholer.model.TblUser;
import com.denada.travpholer.util.http.ApiClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;


public class SplashActivity extends Activity {

    int seconds = 100;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        CGlobal.initGlobal(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        switch (-1){
            case 3:{
                Intent intent = new Intent(SplashActivity.this, TestActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case 2: {
                TblUser tblUser = new TblUser();
                tblUser.tu_username = "tw3307091960";
                tblUser.tu_password = "3307091960";

                tblUser.tu_username = "tw_bohuang29@hotmail.com";
                tblUser.tu_password = "bohuang29@hotmail.com";

                Call<LoginResponse> call = ApiClient.getApiClient().onLogin(BaseModel.getQueryMap(tblUser), "sign");

                call.enqueue(new retrofit2.Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                        String p = response.raw().toString();
                        p = response.body().toString();
                        mHandler.obtainMessage(200,response.body()).sendToTarget();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(SplashActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case 1: {
                Intent intent = new Intent(SplashActivity.this, BasicMapDemoActivity.class);
                startActivity(intent);
                finish();

                break;
            }
            default:{
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Integer hasLogin = prefs.getInt(Constants.KEY_HASLOGIN, -1);
                if (hasLogin == 1){
                    //progressBar.setVisibility(View.VISIBLE);
                    TblUser tblUser = new TblUser();
                    tblUser.tu_username = prefs.getString("tu_username","username");
                    tblUser.tu_password = prefs.getString("tu_password","password");
                    Call<LoginResponse> call = ApiClient.getApiClient().onLogin(BaseModel.getQueryMap(tblUser), "sign");

                    call.enqueue(new retrofit2.Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                            mHandler.obtainMessage(200,response.body()).sendToTarget();
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            //Toast.makeText(SplashActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });


                }else{
                    //progressBar.setVisibility(View.GONE);
                    int introviewed = prefs.getInt(Constants.KEY_INTROVIEWED,-1);
                    if (introviewed == 1)
                        mHandler.sendEmptyMessageDelayed(100,seconds);
                    else
                        mHandler.sendEmptyMessageDelayed(500,seconds);
                }
            }
        }



    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100: {
                    //network succ
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 200: {
                    //network succ
                    if (msg.obj!=null && msg.obj instanceof LoginResponse){
                        LoginResponse loginResponse = (LoginResponse) msg.obj;
                        if (loginResponse.response == 200) {
                            CGlobal.gotoMainWindow(SplashActivity.this, loginResponse);
                        } else {
                            Toast.makeText(SplashActivity.this, "Username or Password is not correct", Toast.LENGTH_SHORT).show();
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                    break;
                }
                case 400:{
                    //network fail
                    Toast.makeText(SplashActivity.this,"Can't connect Server",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                }
                case 500:{
                    Intent intent = new Intent(SplashActivity.this,IntroActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }
    };

}