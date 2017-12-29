package com.denada.travpholer.Fragment;

/**
 * Created by Hgc on 6/2/2015.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.mapmarker.clustering.Cluster;
import com.denada.travpholer.mapmarker.clustering.ClusterManager;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.Marker.Person;
import com.denada.travpholer.model.Marker.PersonRenderer;
import com.denada.travpholer.model.Response.HotResponse;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblUser;
import com.denada.travpholer.util.http.ApiClient;
import com.denada.travpholer.view.nice.NiceSpinner;
import com.denada.travpholer.view.nice.NiceSpinnerAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;


public class MapTogoFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback
        , ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterInfoWindowClickListener<Person>, ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterItemInfoWindowClickListener<Person> {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public NiceSpinner spinner1;
    String TAG = getClass().getName();
    Geocoder geocoder;
    ImageView imageView, img_map;
    int count = 0;
    LocationManager locationManager;
    ArrayList<Object> countryData;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private Marker marker = null;
    private String mAddress;
    private View mRootView;
    private LatLng mPos;
    private View viewAction;
    private List<Person> mData = new ArrayList<>();
    private ProgressBar progressBar;
    private List<Marker> markers = new ArrayList<>();
    private ClusterManager<Person> mClusterManager;
    private int mSpinnerPos = 0;
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSpinnerPos = position;
            String countryID = null;
            if (mSpinnerPos > 0 && mSpinnerPos <= ((NiceSpinnerAdapter) parent.getAdapter()).getCount()) {
                Country country = (Country) ((NiceSpinnerAdapter) parent.getAdapter()).getMyItem(mSpinnerPos);
                countryID = country.countryID;
            }
            OtherPhotosFragment otherPhotosFragment = new OtherPhotosFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tu_id", CGlobal.curUser.tu_id);
            bundle.putInt("mode", 0);
            if (countryID != null)
                bundle.putString("tp_countryid", countryID);
            bundle.putString("tabIndex", Constants.BOTTOM_TAB_MINE);
            otherPhotosFragment.setArguments(bundle);
            replaceFragment(otherPhotosFragment);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 100: {
                        spinner1.setOnItemSelectedListener(onItemSelectedListener);
                        break;
                    }
                }
            } catch (Exception ex) {

            }

        }
    };

    public static Fragment getInstance(int indicator) {
        Fragment fragment = new MapTogoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("indicator", indicator);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Bitmap getMarkerBitmap1(Bitmap bm, Context context, String number) {
        Resources resources = context.getResources();
        int textsize = 14;
        float scale = resources.getDisplayMetrics().density;
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        String gText = String.valueOf(number);

        paint.setStyle(Paint.Style.STROKE);
        // paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(Color.rgb(61, 61, 61));
        paint.setTextSize((int) (textsize * scale));


        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);

        int bw = bounds.width();
        int bh = bounds.height();

        int radius;
        if (bw < bh)
            radius = bh / 2;
        else
            radius = bw / 2;
        radius += 8;


        int sm, cw, tw, th;
        sm = 4;
        cw = radius;
        tw = 120;
        th = 120;
        int dw = tw - 2 * sm - cw;
        int dh = tw - 2 * sm - cw;
        int thumb_width = bm.getWidth();
        int thumb_height = bm.getHeight();
        int realwidth, realheight;
        if (thumb_width > thumb_height) {
            realwidth = dw;
            realheight = (int) (((float) realwidth / (float) thumb_width) * thumb_height);
        } else {
            realheight = dh;
            realwidth = (int) (((float) realheight / (float) thumb_height) * thumb_width);
        }
        Bitmap bitmap = Bitmap.createBitmap(tw, th, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255, 255, 255, 255);
        canvas.drawRect(0, cw, sm * 2 + dw, th, paint);

        paint.setFilterBitmap(true);
        paint.setDither(true);

        int cx = (dw - realwidth) / 2 + sm, cy = (dh - realheight) / 2 + sm + cw;
        Rect dst = new Rect(cx, cy, cx + realwidth, cy + realheight);
        canvas.drawBitmap(bm, new Rect(0, 0, thumb_width, thumb_height), dst, paint);

        if (Integer.valueOf(gText) > 1) {
            //draw circle number
            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(255, 255, 0, 0);
            canvas.drawCircle(tw - cw - 5, cw + 5, radius, paint);

            Paint plantxtpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            plantxtpaint.setColor(Color.rgb(255, 255, 255));
            plantxtpaint.setTextSize((int) (textsize * scale));


            canvas.drawText(gText, (tw - cw - 5) - bw / 2, (cw + 5) + bh / 3, plantxtpaint);
        }


        return bitmap;
    }

    public void downloadImages(final int index) {
        if (index == mData.size()) {
            progressBar.setVisibility(View.GONE);
            updateMarkers();
            return;
        }
        final Person country = mData.get(index);

        Glide.with(getActivity())
                .load(CGlobal.getThumbPhotoPath(country.tp_thumb))
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
                        try {
                            int p = index + 1;
                            Bitmap newBmp = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                            country.map_bitmap = newBmp;
                            mData.set(index, country);
                            downloadImages(p);
                        } catch (Exception ex) {

                        }


                        return false;
                    }
                })
                .into(imageView);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (container == null) {
            return null;
        }
        try {
            if (mRootView != null) {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                if (parent != null)
                    parent.removeView(mRootView);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            mRootView = inflater.inflate(R.layout.fragment_map, container, false);
            imageView = (ImageView) mRootView.findViewById(R.id.imageView);

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
            viewAction = mRootView.findViewById(R.id.view_action);
            viewAction.setOnClickListener(this);
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            spinner1 = (NiceSpinner) mRootView.findViewById(R.id.spinner1);
            List<Object> data = new ArrayList<>();
            data.add(new String("All Countries"));
            spinner1.attachDataSource(data);
            spinner1.setVisibility(View.VISIBLE);

            img_map = (ImageView) mRootView.findViewById(R.id.img_map);
            img_map.setVisibility(View.VISIBLE);
            img_map.setOnClickListener(this);

        } catch (Exception ex) {
            ex.printStackTrace();
            fetchData();
        }

        initActionbar();

        return mRootView;
    }

    private void initActionbar() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            locationManager = (LocationManager) mAct.getSystemService(Context.LOCATION_SERVICE);
            count = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.view_action: {

                break;
            }
            case R.id.img_map: {
                OtherPhotosFragment otherPhotosFragment = new OtherPhotosFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tu_id", CGlobal.curUser.tu_id);
                bundle.putInt("mode", 0);
                bundle.putString("tabIndex", Constants.BOTTOM_TAB_MINE);
                otherPhotosFragment.setArguments(bundle);
                replaceFragment(otherPhotosFragment);
                break;
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPos != null) {
            outState.putDouble("lat", mPos.latitude);
            outState.putDouble("lon", mPos.longitude);
        }
    }

    private void updateMarkers() {
        mClusterManager.clearItems();
        mClusterManager.addItems(mData);
        mClusterManager.cluster();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            //setIndicator(mRootView,0);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Map Settings

            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            MapsInitializer.initialize(mAct);

            LatLng position = CGlobal.defaultPos;
            CameraUpdate update;
            update = CameraUpdateFactory.newLatLngZoom(position, 3);

            //Use map.animateCamera(update) if you want moving effect
            mMap.moveCamera(update);

            startDemo();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        // Show a toast with some info when the cluster is clicked.
        Person item = null;
        String ids = "";
        for (Person p : cluster.getItems()) {
            item = p;
            ids = ids + p.tp_id + ",";
        }


        if (ids.length() > 0) {
            ids = ids.substring(0, ids.length() - 1);
            OtherPhotosFragment otherPhotosFragment = new OtherPhotosFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tu_id", CGlobal.curUser.tu_id);
            bundle.putInt("mode", 3);
            bundle.putString("tp_countryid", item.tp_countryid);
            bundle.putString("tabIndex", Constants.BOTTOM_TAB_MINE);
            bundle.putString("tp_ids", ids);
            String name = CGlobal.curUser.tu_firstname;
            bundle.putString("title",name + "'s Bucket-List");

            otherPhotosFragment.setArguments(bundle);
            replaceFragment(otherPhotosFragment);
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Person item) {
        // Does nothing, but you could go into the user's profile page, for example.
        OtherPhotosFragment otherPhotosFragment = new OtherPhotosFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tu_id", CGlobal.curUser.tu_id);
        bundle.putInt("mode", 3);
        bundle.putString("tp_countryid", item.tp_countryid);
        bundle.putString("tabIndex", Constants.BOTTOM_TAB_MINE);
        bundle.putString("tp_ids", item.tp_id);
        String name = CGlobal.curUser.tu_firstname;
        bundle.putString("title",name + "'s Bucket-List");

        otherPhotosFragment.setArguments(bundle);
        replaceFragment(otherPhotosFragment);

        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    protected void startDemo() {

        mClusterManager = new ClusterManager<Person>(getActivity(), mMap);
        mClusterManager.setRenderer(new PersonRenderer(getActivity(), mMap, mClusterManager));
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);


        fetchData();
    }
    void fetchData(){
        TblUser user = new TblUser();

        user.tu_id = CGlobal.curUser.tu_id;
        progressBar.setVisibility(View.VISIBLE);

        Call<HotResponse> call = ApiClient.getApiClient().onTemplateRequestForHotResponse(CGlobal.getLastFileName(Constants.ACTION_TOGO_DATA), BaseModel.getQueryMap(user));
        call.enqueue(new retrofit2.Callback<HotResponse>() {
            @Override
            public void onResponse(Call<HotResponse> call, retrofit2.Response<HotResponse> response) {
                HotResponse hotResponse = response.body();
                if (hotResponse.response == 200) {
                    HotResponse bidResponse = hotResponse;
                    if (bidResponse.rows_country != null && bidResponse.rows_country.size() > 0) {
                        int index = spinner1.getSelectedIndex();
                        String countryID = null;
                        if (index > 0 && index <= spinner1.getAdapter().getCount()) {
                            Country country = (Country) ((NiceSpinnerAdapter) spinner1.getAdapter()).getMyItem(index);
                            countryID = country.countryID;
                        }


                        ArrayList<Object> data = new ArrayList<>();
                        data.add(new String("All Countries"));
                        for (int i = 0; i < bidResponse.rows_country.size(); i++) {
                            Country item = bidResponse.rows_country.get(i);
                            data.add(item);
                            if (item.countryID.equals(countryID)) {
                                index = i;
                            }
                        }


                        spinner1.setOnItemSelectedListener(null);
                        countryData = data;
                        spinner1.attachDataSource(data);
                        if (countryID != null) {
                            spinner1.setSelectedIndex(index + 1);
                        }
                        mHandler.sendEmptyMessageDelayed(100, 2000);
                    }

                    if (hotResponse.rows != null && hotResponse.rows.size() > 0) {
                        mData = new ArrayList<Person>();
                        for (int i = 0; i < hotResponse.rows.size(); i++) {
                            TblPhotos tblPhotos = hotResponse.rows.get(i);
                            Person myItem = new Person(tblPhotos);
                            mData.add(myItem);
                        }
                        downloadImages(0);
                        return;
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<HotResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}