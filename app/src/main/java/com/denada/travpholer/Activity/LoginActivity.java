package com.denada.travpholer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.R;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Response.LoginResponse;
import com.denada.travpholer.model.TblUser;
import com.denada.travpholer.util.http.ApiClient;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import retrofit2.Call;

public class LoginActivity extends BaseTopActivity implements View.OnClickListener {

    ProgressBar progressBar;
    LoginButton loginButton;
    View btnFacebook, btnTwitter;
    AccessTokenTracker mAccessTokenTracker;
    AccessToken mAccessToken;
    ProfileTracker mProfileTracker;
    LoginButton facebook_loginButton;
    TwitterAuthClient mTwitterAuthClient;
    Collection<String> permissions = Arrays.asList("public_profile", "email");//"public_profile", "user_friends", "email", "user    @Override
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100: {
                    //network succ
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                }
                case 200: {
                    //network succ
                    if (msg.obj!=null && msg.obj instanceof LoginResponse){
                        LoginResponse loginResponse = (LoginResponse) msg.obj;
                        if (loginResponse.response == 200) {
                            CGlobal.gotoMainWindow(LoginActivity.this, loginResponse);
                        } else {
                            Toast.makeText(LoginActivity.this, "Username or Password is not correct", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                    }

                    progressBar.setVisibility(View.GONE);
                    break;
                }
                case 400:
                    //network fail
                    Toast.makeText(LoginActivity.this, "Can't connect Server", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                case 1200: {
                    BaseModel loginResponse = (BaseModel) msg.obj;
                    if (loginResponse.response == 200) {
                        Toast.makeText(LoginActivity.this, "Password has been sent to your email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Fail to Send", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    break;
                }
                case 2200: {//facebook
                    if (msg.obj == null) {
                        Toast.makeText(LoginActivity.this, "Fail to get Facebook Info", Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle bundle = (Bundle) msg.obj;
                        TblUser user = new TblUser();

                        user.tu_username = "fb_" + bundle.getString("email");
                        if (user.tu_username.equals("fb_null")){
                            //Toast.makeText(LoginActivity.this, "Fail to get Facebook Username", Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance().logOut();
                            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissions);
                            return;
                        }
                        user.tu_type = "0";
                        user.tu_email = bundle.getString("email");
                        user.tu_firstname = bundle.getString("first_name");
                        user.tu_lastname = bundle.getString("last_name");
                        if (user.tu_firstname == null || user.tu_firstname.isEmpty()){
                            Toast.makeText(LoginActivity.this, "Fail to get Firstname", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        user.tu_pic = bundle.getString("profile_pic");
                        user.tu_password = bundle.getString("email");

                        callApiLogin(user);
                    }
                    break;
                }
            }
        }
    };

    void callApiLogin(final TblUser user){
        Call<LoginResponse> call = ApiClient.getApiClient().onLogin(BaseModel.getQueryMap(user), "sign");
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                Object obj = response.body();
                if (obj!=null && obj instanceof LoginResponse){
                    LoginResponse loginResponse = (LoginResponse) obj;
                    if (loginResponse.response == 200) {
                        CGlobal.gotoMainWindow(LoginActivity.this, loginResponse);
                    } else {
                        Toast.makeText(LoginActivity.this, "Username or Password is not correct", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    callApiLogin(user);
                    return;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Can't connect Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Profile mProfile = null;
    private TwitterLoginButton twitter_loginButton;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            String accessToken = loginResult.getAccessToken().getToken();
            Log.i("accessToken", accessToken);

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    // Get facebook data from login
                    Bundle bFacebookData = getFacebookData(object);

                    mHandler.obtainMessage(2200, bFacebookData).sendToTarget();
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
//            Profile profile = Profile.getCurrentProfile();
//            setProfile(profile);

        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            Log.d("error",exception.toString());
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this,"Your network seems slow, Connecting to Facebook Error",Toast.LENGTH_SHORT).show();
                }
            });

            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissions);
            // App code
        }
    };
    private com.twitter.sdk.android.core.Callback<TwitterSession> mTwitterCallback = new com.twitter.sdk.android.core.Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            // The TwitterSession is also available through:
            // Twitter.getInstance().core.getSessionManager().getActiveSession()
            final TwitterSession session = result.data;
            // TODO: Remove toast and use the TwitterSession's userID
            // with your app's user model


//                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new com.twitter.sdk.android.core.Callback<User>() {
                @Override
                public void success(final Result<User> result) {
                    TwitterAuthClient authClient = new TwitterAuthClient();
                    authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
                        @Override
                        public void success(Result<String> result2) {
                            String url = result.data.profileImageUrl;


                            TblUser user = new TblUser();


                            user.tu_type = "1";
                            user.tu_email = result2.data;
                            user.tu_username = "tw_" + result2.data;

                            if (user.tu_username.equals("tw_null")){
                                Toast.makeText(LoginActivity.this, "Failed to get Information", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            user.tu_firstname = result.data.screenName;
                            user.tu_lastname = "";
                            user.tu_pic = result.data.profileImageUrl;
                            user.tu_password = result2.data;

                            callApiLogin(user);
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            exception.printStackTrace();
                        }
                    });


                }

                @Override
                public void failure(TwitterException e) {

                }
            });

//            mTwitterAuthClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
//                @Override
//                public void success(final Result<String> result1) {
//
//
//                }
//
//                @Override
//                public void failure(TwitterException exception) {
//                    Log.d("Log", "Failed to get Email");
//                }
//            });


        }

        @Override
        public void failure(TwitterException exception) {
            Log.d("TwitterKit", "Login with Twitter failure", exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        CGlobal.g_callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);


        //twitter logout
//        CookieSyncManager.createInstance(this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeSessionCookie();
//        Twitter.getSessionManager().clearActiveSession();
//        Twitter.logOut();
//
//        LoginManager.getInstance().logOut();

        //facebook
        LoginManager.getInstance().registerCallback(CGlobal.g_callbackManager, mCallBack);


//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
//        facebook_loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
//        facebook_loginButton.registerCallback(CGlobal.g_callbackManager,mCallBack);

        btnFacebook = findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(this);
        btnFacebook.setVisibility(View.VISIBLE);

        //twitter
//        twitter_loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
//        twitter_loginButton.setCallback();

        btnTwitter = findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(this);
        btnTwitter.setVisibility(View.VISIBLE);

    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            if (object.has("id"))
                bundle.putString("id", object.getString("id"));

            return bundle;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnFacebook: {
                LoginManager.getInstance().logInWithReadPermissions(this, permissions);
                break;
            }
            case R.id.btnTwitter: {
//                Twitter.getInstance().logIn(this,mTwitterCallback);
                mTwitterAuthClient = new TwitterAuthClient();
                mTwitterAuthClient.authorize(this, mTwitterCallback);
                break;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CGlobal.g_callbackManager != null) {
            CGlobal.g_callbackManager.onActivityResult(requestCode, resultCode, data);
        }

//        twitter_loginButton.onActivityResult(requestCode, resultCode, data);
        if (mTwitterAuthClient != null) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }


    }
}


