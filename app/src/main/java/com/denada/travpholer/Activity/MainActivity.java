package com.denada.travpholer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.Fragment.BaseFragment;
import com.denada.travpholer.Fragment.ExploreFragment;
import com.denada.travpholer.Fragment.MineFragment;
import com.denada.travpholer.Fragment.ProfileFragment;
import com.denada.travpholer.Fragment.UploadFragment;
import com.denada.travpholer.GCM_Config;
import com.denada.travpholer.R;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.PhotoManager;
import com.denada.travpholer.model.Response.IpResponse;
import com.denada.travpholer.model.Response.LoginResponse;
import com.denada.travpholer.model.TblUser;
import com.denada.travpholer.util.http.ApiClient;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.places.Places;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static GoogleApiClient mGoogleApiClient;
    public static String REG_EMAIL = "user_email";
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    protected int mCurrentTab = 0;
    String[] tabIds = {Constants.BOTTOM_TAB_EXPLORE, Constants.BOTTOM_TAB_MINE, Constants.BOTTOM_TAB_UPLOAD, Constants.BOTTOM_TAB_PROFILE};
    Integer[] tabImageIds_white = {R.drawable.explore_white, R.drawable.conquered, R.drawable.upload_white, R.drawable.profile_white};
    Integer[] tabImageIds_blue = {R.drawable.explore_blue, R.drawable.conquered_blue, R.drawable.upload_blue, R.drawable.profile_green};
    TextView topTitle;
    ImageView img_toolbar_back;
    LoginButton loginButton;
    PhotoManager photoManager;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    boolean is_app_init;
    /* Your Tab host */
    private Context appContext;
    private View view_bottombar1, view_bottombar2, view_bottombar3, view_bottombar4;
    private ImageView imageview_bottombar1, imageview_bottombar2, imageview_bottombar3, imageview_bottombar4;
    private TextView txt_bottombar1, txt_bottombar2, txt_bottombar3, txt_bottombar4;
    private ArrayList<ImageView> array_bottombar_imageviews = new ArrayList<>();
    private ArrayList<TextView> array_bottombar_textviews = new ArrayList<>();
    private ArrayList<View> array_bottombar_views = new ArrayList<>();
    private TabInfo mLastTab;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private Context mContext;
    private boolean mAttached;
    /**
     * gcm part
     */

    private GoogleCloudMessaging gcm;
    private String regId;
    private ProgressDialog dialog;

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo thisiveNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return thisiveNetworkInfo != null && thisiveNetworkInfo.isConnected();
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());

        }

        photoManager = PhotoManager.getInstance(this);
        CGlobal.g_callbackManager = CallbackManager.Factory.create();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        setContentView(R.layout.activity_apptabmainactivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		getSupportActionBar().setCustomView(R.layout.actionbar);
        topTitle = (TextView) toolbar.findViewById(R.id.mytext);
        img_toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        topTitle.setText("Hot");

        img_toolbar_back.setOnClickListener(this);

//		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        view_bottombar1 = findViewById(R.id.view_bottombar1);
        view_bottombar2 = findViewById(R.id.view_bottombar2);
        view_bottombar3 = findViewById(R.id.view_bottombar3);
        view_bottombar4 = findViewById(R.id.view_bottombar4);

        imageview_bottombar1 = (ImageView) findViewById(R.id.imgExplore);
        imageview_bottombar2 = (ImageView) findViewById(R.id.imgMine);
        imageview_bottombar3 = (ImageView) findViewById(R.id.imgUpload);
        imageview_bottombar4 = (ImageView) findViewById(R.id.imgProfile);

        txt_bottombar1 = (TextView) findViewById(R.id.txtExplore);
        txt_bottombar2 = (TextView) findViewById(R.id.txtMine);
        txt_bottombar3 = (TextView) findViewById(R.id.txtUpload);
        txt_bottombar4 = (TextView) findViewById(R.id.txtProfile);

        array_bottombar_imageviews.add(imageview_bottombar1);
        array_bottombar_imageviews.add(imageview_bottombar2);
        array_bottombar_imageviews.add(imageview_bottombar3);
        array_bottombar_imageviews.add(imageview_bottombar4);

        array_bottombar_textviews.add(txt_bottombar1);
        array_bottombar_textviews.add(txt_bottombar2);
        array_bottombar_textviews.add(txt_bottombar3);
        array_bottombar_textviews.add(txt_bottombar4);

        array_bottombar_views.add(view_bottombar1);
        array_bottombar_views.add(view_bottombar2);
        array_bottombar_views.add(view_bottombar3);
        array_bottombar_views.add(view_bottombar4);

        view_bottombar1.setOnClickListener(this);
        view_bottombar2.setOnClickListener(this);
        view_bottombar3.setOnClickListener(this);
        view_bottombar4.setOnClickListener(this);


        appContext = this;


        ApiClient.ApiIpInterface client = ApiClient.getIpClient(Constants.APISERVICE_IP_URL);
        try {
            Call<IpResponse> call = client.onGetLocation();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Call<IpResponse> call = client.onGetLocation();
        call.enqueue(new Callback<IpResponse>() {
            @Override
            public void onResponse(Call<IpResponse> call, Response<IpResponse> response) {
                if (response.body().status.equals("success")) {
                    if (CGlobal.dbManager != null) {
                        CGlobal.currentCountry = CGlobal.dbManager.getCountryFromWebCode(response.body().countryCode);
                    }
                } else {
//					mHandler.obtainMessage(1200, ipResponse).sendToTarget();
                }
            }

            @Override
            public void onFailure(Call<IpResponse> call, Throwable t) {
//				mHandler.obtainMessage(1400, retrofitError).sendToTarget();
            }
        });

        CGlobal.initGlobal(this);
        initReg();
        requestLocationPermission();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        Bundle bundle = new Bundle();
        bundle.putString("tu_id", CGlobal.curUser.tu_id);
        bundle.putInt("mode", 0);
        //bundle.putString("tp_countryid", "");
        bundle.putString("tabIndex", Constants.BOTTOM_TAB_MINE);
        //bundle.putString("tp_ids",ids);
//        otherPhotosFragment.setArguments(bundle);

        mFragmentManager = getSupportFragmentManager();
        mContext = this;

        addTab(FragmentTab1.class, null, Constants.BOTTOM_TAB_EXPLORE);
        addTab(FragmentTab1.class, null, Constants.BOTTOM_TAB_MINE);
        addTab(FragmentTab1.class, bundle, Constants.BOTTOM_TAB_UPLOAD);
        addTab(FragmentTab1.class, bundle, Constants.BOTTOM_TAB_PROFILE);

        mContainerId = R.id.realtabcontent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mGoogleApiClient.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            photoManager.dealloc();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mGoogleApiClient = null;

    }

    @SuppressLint("InflateParams")
    private View createTabView(final int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageResource(id);
        return view;
    }

    private void setTabBackground(int index, boolean isselected) {
        //selection style
        int white_color = Color.argb(0xFF, 0xff, 0xff, 0xff);        //white
        int blue_color = Color.argb(0xFF, 0x00, 0x99, 0xCC);
        int menu_color = Color.argb(0xFF, 0xeb, 0xeb, 0xeb);        //white
        View view = array_bottombar_views.get(index);
        ImageView imageView = array_bottombar_imageviews.get(index);
        TextView textView = array_bottombar_textviews.get(index);

        if (isselected) {
            //blue back
            view.setBackgroundColor(blue_color);
            imageView.setImageResource(tabImageIds_white[index]);
            textView.setTextColor(white_color);
        } else {
            //white back
            view.setBackgroundColor(menu_color);
            imageView.setImageResource(tabImageIds_blue[index]);
            textView.setTextColor(blue_color);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Fragment f = getSupportFragmentManager()
                .findFragmentByTag(getCurrentTabTag());
        if (f != null && f instanceof FragmentTab1) {
            FragmentTab1 tabChild = (FragmentTab1) f;
            tabChild.onActivityResult(requestCode, resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        switch (id) {
            case R.id.toolbar_back: {
                onBackPressed();
                break;
            }
            case R.id.view_bottombar1: {
                if (mAttached) {
                    FragmentTransaction ft = doTabChanged(Constants.BOTTOM_TAB_EXPLORE, null);
                    if (ft != null) {
                        ft.commit();
                    }
                }
                mCurrentTab = 0;
                break;
            }
            case R.id.view_bottombar2: {
                if (mAttached) {
                    FragmentTransaction ft = doTabChanged(Constants.BOTTOM_TAB_MINE, null);
                    if (ft != null) {
                        ft.commit();
                    }
                }
                mCurrentTab = 1;
                break;
            }
            case R.id.view_bottombar3: {
                if (mAttached) {
                    FragmentTransaction ft = doTabChanged(Constants.BOTTOM_TAB_UPLOAD, null);
                    if (ft != null) {
                        ft.commit();
                    }
                }
                mCurrentTab = 2;
                break;
            }
            case R.id.view_bottombar4: {
                if (mAttached) {
                    FragmentTransaction ft = doTabChanged(Constants.BOTTOM_TAB_PROFILE, null);
                    if (ft != null) {
                        ft.commit();
                    }
                }
                mCurrentTab = 3;
                break;
            }
        }
    }

    private boolean requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "ACCESS_FINE_LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                }
                break;
            case UploadFragment.PERMISSIONS_REQUEST_UPLOAD:{
                Fragment f = getSupportFragmentManager()
                        .findFragmentByTag(getCurrentTabTag());
                if (f != null && f instanceof FragmentTab1) {
                    FragmentTab1 tabChild = (FragmentTab1) f;
//            tabChild.onActivityResult(requestCode, resultCode, data);
                    tabChild.onRequestPermissionsResult(requestCode,permissions,grantResults);
                    return;
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void setTopTitle(String string, int toolbar_back) {
        if (string != null && !string.isEmpty())
            topTitle.setText(string);
        if (toolbar_back > 0) {
            img_toolbar_back.setImageResource(toolbar_back);
            img_toolbar_back.setVisibility(View.VISIBLE);
        } else {
            img_toolbar_back.setImageResource(android.R.color.transparent);
            img_toolbar_back.setVisibility(View.GONE);
        }
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.KEY_HASLOGIN, -1);
        editor.commit();

        CGlobal.resetData();

        TblUser user = new TblUser();
        user.tu_id = CGlobal.curUser.tu_id;
        user.tu_gcmid = "0";

        Call<LoginResponse> call = ApiClient.getApiClient().onTemplateRequest(CGlobal.getLastFileName(Constants.ACTION_UPDATEPROFILE), BaseModel.getQueryMap(user));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse.response == 200) {
                    Log.d("Gcm Save", "Success");
                } else {
                    Log.d("Gcm Save", "Fail");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("Gcm Save", "Fail");
            }
        });

        CGlobal.curUser = null;

        //twitter logout
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();

        LoginManager.getInstance().logOut();


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    private void initReg() {
        sp = this.getSharedPreferences("login", this.MODE_PRIVATE);
        is_app_init = false;

        if (is_app_init) {

        } else {

            if (isNetworkAvailable(this.getApplicationContext())) {

                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(this.getBaseContext());

                // Showing status
                if (status == ConnectionResult.SUCCESS) {
                    registerInBackground();
                } else {
                    int requestCode = 10;
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                            status, this, requestCode);
                    dialog.show();
                }
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setCancelable(true);
                alert.setTitle("Oops!!!");
                alert.setNeutralButton("Okay!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        // this.finish();
                    }
                });
                alert.setMessage("Check internet connection to get notification");
                alert.show();
            }
            // register for GCm serviece
            // Check device for Play Services APK. If check
            // succeeds, proceed with GCM registration.
        }
    }

    private void registerInBackground() {

        // Log.d("In ASYNC OF registration", s_id);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging
                                .getInstance(MainActivity.this.getApplicationContext());
                    }
                    regId = gcm.register(GCM_Config.SENDER_ID);
                    Log.e("GCM REGISTRATION ID", regId);
                    // You should send the registration ID to your server
                    // over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.


                    if (!regId.isEmpty()) {
                        sendRegistrationIdToBackend(regId);
                    }

                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();

                    ex.printStackTrace();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (msg != null && !msg.isEmpty())
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        }.execute(null, null, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CGlobal.curUser != null) {
            TblUser user = new TblUser();
            user.tu_id = CGlobal.curUser.tu_id;
            user.action = "update_opentime";

            Call<LoginResponse> call = ApiClient.getApiClient().onTemplateRequest(CGlobal.getLastFileName(Constants.ACTION_UPDATEPROFILE), BaseModel.getQueryMap(user));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.response == 200) {
                        Log.d("OpenTime Resume", "Success");
                    } else {
                        Log.d("OpenTime Resume", "Fail");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("OpenTime Resume", "Fail");
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (CGlobal.curUser != null) {
            TblUser user = new TblUser();
            user.tu_id = CGlobal.curUser.tu_id;
            user.action = "update_opentime";

            Call<LoginResponse> call = ApiClient.getApiClient().onTemplateRequest(CGlobal.getLastFileName(Constants.ACTION_UPDATEPROFILE), BaseModel.getQueryMap(user));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.response == 200) {
                        Log.d("OpenTime Stop", "Success");
                    } else {
                        Log.d("OpenTime Stop", "Fail");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("OpenTime Stop", "Fail");
                }
            });
        }
    }

    private void sendRegistrationIdToBackend(final String s_id) {
        //
        TblUser user = new TblUser();
        user.tu_id = CGlobal.curUser.tu_id;
        user.tu_gcmid = s_id;

        Call<LoginResponse> call = ApiClient.getApiClient().onTemplateRequest(CGlobal.getLastFileName(Constants.ACTION_UPDATEPROFILE), BaseModel.getQueryMap(user));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse.response == 200) {
                    Log.d("Gcm Save", "Success");
                } else {
                    Log.d("Gcm Save", "Fail");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("Gcm Save", "Fail");
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(MainActivity.class.getName(), "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(MainActivity.class.getName(), "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient = null;
        Log.d(MainActivity.class.getName(), "onConnectionFailed");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        String currentTab = getCurrentTabTag();

        // Go through all tabs and make sure their fragments match
        // the correct state.
        FragmentTransaction ft = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.fragment != null && !tab.fragment.isDetached()) {
                if (tab.tag.equals(currentTab)) {
                    // The fragment for this tab is already there and
                    // active, and it is what we really want to have
                    // as the current tab.  Nothing to do.
                    mLastTab = tab;
                } else {
                    // This fragment was restored in the active state,
                    // but is not the current tab.  Deactivate it.
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.detach(tab.fragment);
                }
            }
        }

        // We are now ready to go.  Make sure we are switched to the
        // correct tab.
        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commit();
            mFragmentManager.executePendingTransactions();
        }
    }

    public String getCurrentTabTag() {
        if (mCurrentTab >= 0 && mCurrentTab < mTabs.size()) {
            return mTabs.get(mCurrentTab).getTag();
        }
        return null;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void addTab(Class<?> clss, Bundle args, String tag) {

        TabInfo info = new TabInfo(tag, clss, args);

        if (mAttached) {
            // If we are already attached to the window, then check to make
            // sure this tab's fragment is inactive if it exists.  This shouldn't
            // normally happen.
            info.fragment = mFragmentManager.findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }
        }

        mTabs.add(info);
    }

    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mContext,
                            newTab.clss.getName(), newTab.args);
                    String tag = newTab.tag;
                    switch (tag) {
                        case Constants.BOTTOM_TAB_EXPLORE: {
                            ((FragmentTab1) newTab.fragment).setClss(ExploreFragment.class);
                            break;
                        }
                        case Constants.BOTTOM_TAB_MINE: {
                            ((FragmentTab1) newTab.fragment).setClss(MineFragment.class);
                            break;
                        }
                        case Constants.BOTTOM_TAB_UPLOAD: {
                            ((FragmentTab1) newTab.fragment).setClss(UploadFragment.class);
                            break;
                        }
                        case Constants.BOTTOM_TAB_PROFILE: {
                            ((FragmentTab1) newTab.fragment).setClss(ProfileFragment.class);
                            break;
                        }
                    }
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }

            mLastTab = newTab;
        }
        return ft;
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager()
                .findFragmentByTag(getCurrentTabTag());
        if (f != null && f instanceof FragmentTab1) {
            FragmentTab1 tabChild = (FragmentTab1) f;
            if (tabChild.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public interface PushFragment {
        public void onPushFragment(BaseFragment fragment);
    }

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }

        public String getTag() {
            return tag;
        }
    }
}
