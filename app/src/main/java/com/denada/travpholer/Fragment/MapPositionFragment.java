package com.denada.travpholer.Fragment;

/**
 * Created by Hgc on 6/2/2015.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.model.google.GoogleAddressComponent;
import com.denada.travpholer.model.AddressData;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.Response.MapResponse;
import com.denada.travpholer.util.http.ApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapPositionFragment extends BaseFragment implements View.OnClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback{

    String TAG = getClass().getName();

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;


    private Marker marker = null;
    private String mAddress;
    private View mRootView;

    public static Fragment getInstance(int indicator){
        Fragment fragment = new MapPositionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("indicator",indicator);
        fragment.setArguments(bundle);
        return fragment;
    }
    Geocoder geocoder;
    private View viewAction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (container == null) {
            return null;
        }
        try {
            mRootView = inflater.inflate(R.layout.fragment_map_pos, container, false);

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            viewAction = mRootView.findViewById(R.id.view_action);
            viewAction.setVisibility(View.VISIBLE);
            viewAction.setOnClickListener(this);

            geocoder  = new Geocoder(getActivity(), Locale.getDefault());

        }catch(Exception ex){
            ex.printStackTrace();
        }

        initActionbar();
        setCaption(null, R.drawable.ico_back);

       return mRootView;
    }
//    GoogleMap googleMap;
//    private void initilizeMap() {
//        googleMap = ((MapFragment) this.getActivity(). getFragmentManager().findFragmentById(
//                R.id.map1)).getMap();
//
//        // check if map is created successfully or not
//        if (googleMap == null) {
//            Toast.makeText(this.getActivity(),
//                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                    .show();
//        }
//
//        if (googleMap != null)
//        {
//            /*if (Globals.g_contact.mLat != null && Globals.g_contact.mLong!= null
//                    && !Globals.g_contact.mLat.equals("") && !Globals.g_contact.mLong.equals(""))
//            {*/
//            //LatLng latLng = new LatLng(Double.parseDouble(Globals.g_contact.mLat),Double.parseDouble(Globals.g_contact.mLong));
//            LatLng latLng = new LatLng(32.294164,-64.782628);
//            googleMap.clear();
//            googleMap.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .title("My Spot")
//                    .snippet("This is my spot!")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//            googleMap.getUiSettings().setCompassEnabled(true);
//            googleMap.getUiSettings().setZoomControlsEnabled(true);
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
//            //}
//        }
//
//
//
//    }
    private void initActionbar(){

    }

    private  Address  getAdressFromPoint(LatLng position){
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(position.latitude);
        location.setLongitude(position.longitude);

        List<Address> addresses = null;
        Address ret = null;
        String errorMessage="";
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
        } else {
            ret = addresses.get(0);
//
//
//            // Fetch the address lines using getAddressLine,
//            // join them, and send them to the thread.
//            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
            Log.i(TAG, getString(R.string.address_found));
        }

        return ret;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    int count = 0;
    LocationManager locationManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            locationManager = (LocationManager) mAct.getSystemService(Context.LOCATION_SERVICE);
            count = 0;
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private void configureNormal(final LatLng param){
//        map.setMyLocationEnabled(true);
        if(param == null)
        {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location location) {
                    if (count != 0)
                        return;
                    //Get current location
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

                    //Add a marker with an image to current location

                    Address address = getAdressFromPoint(param);
                    String strlocation = "";
                    if (address!=null){
                        if(address.getMaxAddressLineIndex()>0){
                            strlocation = address.getAddressLine(0);
                            strlocation = address.getCountryName();
                        }
                    }
                    if (strlocation.isEmpty())
                        return;

//                    param = position;
                    count = 1;
                    if (marker != null) marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(position)
                            .title("Current location")
                            .snippet(strlocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin)));


                    //Zoom parameter is set to 14
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 8);

                    //Use map.animateCamera(update) if you want moving effect
                    mMap.moveCamera(update);
                    //mapView.onResume();


                    //autoCompleteAdapter.setLat(40.770689f);
                    //autoCompleteAdapter.setLon(-73.977278f);
                }
            });
        }
        else
        {
            LatLng position = param;
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 8);

            //Use map.animateCamera(update) if you want moving effect
            mMap.moveCamera(update);
        }


    }

    @Override
    public void onResume(){
        super.onResume();
//        try {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//                Criteria criteria = new Criteria();
//                criteria.setAccuracy(Criteria.ACCURACY_FINE);
//
//                for (String provider : locationManager.getProviders(criteria, true))
//                {
//                    if(provider.contains("gps"))
//                    {
//                        if(mapView!=null){
//                            mapView.onResume();
//                        }
//                        return;
//                    }
//                }
//                // if gps is not enabled
//                //Toast.makeText(this, getResources().getString(R.string.str_message_gpsisnotenabled), Toast.LENGTH_LONG).show();
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    @Override
    public void onMapClick(LatLng position) {

        try{
            if(marker!=null) marker.remove();

//            Address address = getAdressFromPoint(position);
//            String strlocation = "";
//            if (address!=null){
//                if(address.getMaxAddressLineIndex()>0){
//                    strlocation = address.getAddressLine(0);
//                    strlocation = address.getCountryName();
//                }
//            }
//            if (strlocation.isEmpty())
//                return;
            marker  =   mMap.addMarker(new MarkerOptions().position(position)
                    .title("Current location")
//                    .snippet(strlocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin)));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



    @Override
    public void onPause(){
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
        switch(id){
            case R.id.view_action:{
                if (marker!=null){
                    String latlng = String.valueOf(marker.getPosition().latitude) +","+ String.valueOf(marker.getPosition().longitude);
                    Call<MapResponse> call =  ApiClient.getIpClient("http://maps.googleapis.com/maps/").onGetCityCountry(latlng, "false","en");
                    call.enqueue(new Callback<MapResponse>() {
                        @Override
                        public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                            MapResponse mapResponse = response.body();
                            boolean found = false;
                            if (mapResponse.status.toLowerCase().equals("ok")) {

                                AddressData addressData = new AddressData();
                                List<GoogleAddressComponent> components = mapResponse.results.get(0).address_components;
                                for (GoogleAddressComponent component : components) {

                                    if (component.types.indexOf("administrative_area_level_2") >= 0) {
                                        addressData.city_hubos.add(component.long_name);
                                        addressData.city_hubo2 = component.long_name;
                                        continue;
                                    }
									if (component.types.indexOf("administrative_area_level_1") >= 0) {
                                        addressData.city_hubo1 = component.long_name;
                                        addressData.city_hubos.add(component.long_name);
                                        continue;
                                    }
									if (component.types.indexOf("locality") >= 0) {
                                        addressData.city_hubos.add(component.long_name);
                                        continue;
                                    }
									if (component.types.indexOf("neighborhood") >= 0) {
                                        addressData.city_hubos.add(component.long_name);
                                        continue;
                                    }
                                    if (component.types.indexOf("country") >= 0) {
                                        addressData.country = component.long_name;
                                        continue;
                                    }
                                }
                                addressData.pos = marker.getPosition();
                                addressData.type = 2;
                                addressData.setAddressData();
                                if (addressData.address!=null){
                                    found = true;
                                    CGlobal.addressData = addressData;
                                }

                            }
                            if (found == false){
                                Country country = CGlobal.dbManager.getCountryFromName("Other");
                                AddressData addressData = new AddressData();
                                addressData.type = 2;
                                addressData.address = "Other";
                                addressData.pos = new LatLng(Float.valueOf(country.latitude),Float.valueOf(country.longitude));
                                CGlobal.addressData = addressData;
                                Log.e("Upload Fragment", "Place not found");
                            }
//                            ((MainActivity) getActivity()).popFragments();
                            MapPositionFragment.this.getActivity().onBackPressed();
                        }

                        @Override
                        public void onFailure(Call<MapResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Failed to Get Address", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


//                Address address = getAdressFromPoint();
//                CGlobal.address = address;
//                String strlocation = "";
//                String country = "";
//                if (address!=null){
//                    if(address.getMaxAddressLineIndex()>0){
//                        strlocation = address.getAddressLine(0);
//                        country = address.getCountryName();
//                    }
//                }

//                Intent intent = new Intent();
//                intent.putExtra("country",country);
//                intent.putExtra("country",country);
//                intent.setAction(Constants.BROADCAST_MAP_PICKLOCATION);
//                getActivity().sendBroadcast(intent);


                break;
            }
        }
    }

    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
//        if(CGlobal.mPos!=null) {
//            outState.putDouble("lat", CGlobal.mPos.latitude);
//            outState.putDouble("lon", CGlobal.mPos.longitude);
//        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        try {
            //setIndicator(mRootView,0);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Map Settings
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

            map.setOnMapClickListener(this);

            LatLng position = CGlobal.defaultPos;
//            if (CGlobal.addressData!=null){
//                position = CGlobal.addressData.pos;
//            }
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, Constants.MAP_ZOOM);

            //Use map.animateCamera(update) if you want moving effect
            mMap.moveCamera(update);

//            configureNormal();
            MapsInitializer.initialize(mAct);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
//            PermissionUtils.requestPermission((AppCompatActivity)getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


}