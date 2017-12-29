package com.denada.travpholer.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.adapter.AutoCompleteAdapter;
import com.denada.travpholer.model.google.GoogleAddressComponent;
import com.denada.travpholer.model.AddressData;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.Response.HotResponse;
import com.denada.travpholer.model.TblBaseAch;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.google.GooglePlaceDetailResult;
import com.denada.travpholer.util.Exif;
import com.denada.travpholer.util.Storage;
import com.denada.travpholer.util.exif.ExifInterface;
import com.denada.travpholer.util.http.ApiClient;
import com.denada.travpholer.util.http.FileUploadService;
import com.denada.travpholer.view.DelayAutoCompleteTextView;
import com.denada.travpholer.view.autocomplete.rest.model.Prediction;
import com.denada.travpholer.view.autocomplete.rest.model.Terms;
import com.denada.travpholer.view.nice.NiceSpinner;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFragment extends BaseFragment implements View.OnClickListener {

    public static final int REQUEST_CAMERA = 10;
    public static final int SELECT_FILE = 11;
    static final int REQUEST_TAKE_PHOTO = 1;
    ProgressBar progressBar;
    LinearLayout layAutocomplete;
    AutoCompleteAdapter mAutoCompleteAdapter;
    Prediction mPrediction;
    DelayAutoCompleteTextView mEditCity;
    TextView txtLocation;
    NiceSpinner niceSpinner;
    int p = 1;
    String mCurrentPhotoPath;
    private View mRootView;
    private View view_takephoto, view_selectphoto;
    private EditText edtTitle, edtLink;
    private ImageView img_marker, img_pic;
    private View view_submit;
    private String mCountryId = null;
    private boolean isSubmitting = false;
    private TblPhotos mInsertData;
    private Integer sampleWidth;
    private Integer sampleHeight;
    private Uri mUriPhoto;
    private String mQuestionPhotoPath;
    private String mQuestionPhotoPath_filenamepart;
    private Integer mQuestionPhotoOrientation = 0;
    private boolean mQuestionPhotoMirror = false;
    private Integer mQuestionSource = 0;
    private Integer mOrientation;

    //------------------------------------------------------------------------
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("selection", String.valueOf(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public static String getRealPathFromURI(Activity act, Uri contentURI) {
        Cursor cursor = act.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            String ss = savedInstanceState.getString("title", "ddd");
            Log.e("ss onCreateView", ss);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("onCreateView", "onCreateView");

        mRootView = inflater.inflate(R.layout.fragment_upload, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        view_takephoto = mRootView.findViewById(R.id.view_takephoto);
        view_selectphoto = mRootView.findViewById(R.id.view_selectphoto);
        view_submit = mRootView.findViewById(R.id.view_submit);
        img_marker = (ImageView) mRootView.findViewById(R.id.img_location);

        edtTitle = (EditText) mRootView.findViewById(R.id.edtTitle);
        edtLink = (EditText) mRootView.findViewById(R.id.edt_hyperlink);
        img_pic = (ImageView) mRootView.findViewById(R.id.img_pic);
        txtLocation = (TextView) mRootView.findViewById(R.id.txt_location);

        TextView textView = (TextView) mRootView.findViewById(R.id.tv_terms);
        setTextViewHTML(textView, "By clicking [Submit], you agree to our <a href=\"http://denada.hk/en/content/3-terms-and-conditions-of-use\">End User License Agreement</a>,there is no tolerance for objectionable content.");

        img_marker.setOnClickListener(this);
        view_takephoto.setOnClickListener(this);
        view_selectphoto.setOnClickListener(this);
        view_submit.setOnClickListener(this);

        img_pic.setOnClickListener(this);

        setCaption("Upload", -1);
        layAutocomplete = (LinearLayout) mRootView.findViewById(R.id.layAutoComplete);
        layAutocomplete.removeAllViews();
        View viewAutoComplete = inflater.inflate(R.layout.item_autocomplete, null, false);
        mEditCity = (DelayAutoCompleteTextView) viewAutoComplete.findViewById(R.id.edtLocation);
        layAutocomplete.addView(viewAutoComplete);
        if (CGlobal.addressData != null) {
            mEditCity.setText(CGlobal.addressData.address);
        }else{
            mEditCity.setText("");
        }

        mEditCity.setThreshold(1);
        mAutoCompleteAdapter = new AutoCompleteAdapter(this.getActivity());
        mEditCity.setAdapter(mAutoCompleteAdapter);

//        mEditCity.setText("数码港 ddd");
        mEditCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mPrediction = mAutoCompleteAdapter.getItem_data(position);

                AddressData addressData = new AddressData();
                addressData.address = mPrediction.getDescription();
                ArrayList<Terms> terms = mPrediction.getTerms();
                addressData.city = terms.get(0).getValue();
                addressData.country = terms.get(terms.size() - 1).getValue();

                Country country = CGlobal.dbManager.getCountryFromName(addressData.country);
                if (country != null) {
                    addressData.pos = new LatLng(Double.valueOf(country.latitude), Double.valueOf(country.longitude));
                }

                addressData.type = 1;
                addressData.placeId = mPrediction.getPlaceID();
                CGlobal.addressData = addressData;

                mEditCity.setText(addressData.address);
                //mEditCity.setSelection(mEditCity.getText().length());
            }
        });

//        mEditCity.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                        clickAddLocation();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        edtLink.setText("http://");
        Selection.setSelection(edtLink.getText(), edtLink.getText().length());


        edtLink.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("http://")) {
                    edtLink.setText("http://");
                    Selection.setSelection(edtLink.getText(), edtLink.getText().length());

                }

            }
        });

//        niceSpinner = (NiceSpinner) mRootView.findViewById(R.id.spinner_sample);
//        niceSpinner.setVisibility(View.GONE);
//        List<Object> data = new ArrayList<>();
//        data.add(new String("Filter by Country"));
//        data.addAll(CGlobal.hotCountry);
//        niceSpinner.attachDataSource(data);
//        niceSpinner.setOnItemSelectedListener(onItemSelectedListener);
//        niceSpinner.attachDataSource(dataset);

        return mRootView;
    }

    private void clickAddLocation() {

    }

    @Override
    public void onDetach() {

        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String ss = savedInstanceState.getString("title", "ddd");
            Log.e("ss onCreate", ss);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("onCreate", "onCreate");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            String ss = savedInstanceState.getString("title", "ddd");
            Log.e("ss onViewCreated", ss);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("onViewCreated", "onViewCreated");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_location: {

//                if (mPrediction != null && mPrediction.getDescription().equals(mEditCity.getText().toString())) {
//                    AddressData addressData = new AddressData();
//                    addressData.address = mPrediction.getDescription();
//                    ArrayList<Terms> terms = mPrediction.getTerms();
//                    addressData.city = terms.get(0).getValue();
//                    addressData.country = terms.get(terms.size() - 1).getValue();
//
//                    Country country = CGlobal.dbManager.getCountryFromName(addressData.country);
//                    addressData.pos = new LatLng(Double.valueOf(country.latitude), Double.valueOf(country.longitude));
//
//                    addressData.type = 1;
//                    CGlobal.addressData = addressData;
//
//                } else if (CGlobal.addressData != null && CGlobal.addressData.type == 2) {
//
//                } else {
//                    CGlobal.addressData = null;
//                }
                mEditCity.setAdapter(null);
                mEditCity.setOnItemClickListener(null);
                layAutocomplete.removeAllViews();
                MapPositionFragment mapPositionFragment = new MapPositionFragment();
                replaceFragment(mapPositionFragment);
                break;
            }
            case R.id.view_selectphoto: {
                Set<String> missingPermissions = new HashSet<>();
                if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    missingPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!missingPermissions.isEmpty()) {
                    Toast.makeText(getActivity(),"To continue uploading, please enable TravPholer’s access to your Photo Album",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                getActivity().startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        SELECT_FILE);
                break;
            }
            case R.id.view_takephoto: {
                Set<String> missingPermissions = new HashSet<>();
                if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    missingPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!hasPermission(Manifest.permission.CAMERA)) {
                    missingPermissions.add(Manifest.permission.CAMERA);
                }
                if (!missingPermissions.isEmpty()) {
                    Toast.makeText(getActivity(),"To continue uploading, please enable TravPholer’s access to your Photo Album and Camera",Toast.LENGTH_SHORT).show();
                    return;
                }
                dispatchTakePictureIntent();
//                Country country = CGlobal.dbManager.getCountryFromName("中国");
//                Log.e("sss",country.countryName);
                break;
            }
            case R.id.view_submit: {

                switch (-1) {
                    case 1: {
                        int i = niceSpinner.getSelectedIndex();
                        Object obj = (niceSpinner.getAdapter()).getMyItem(i);
                        Toast.makeText(getActivity(), String.valueOf(i), Toast.LENGTH_SHORT).show();

                        niceSpinner.setSelectedIndex(2);
                        break;
                    }
                    case 2: {

                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sampleimage);
                        Bitmap bm = MapTogoFragment.getMarkerBitmap1(largeIcon, getActivity(), String.valueOf(p));
                        img_pic.setImageBitmap(bm);
                        p += 10;
                        break;
                    }
                    default: {
                        if (checkValidate()) {

                            progressBar.setVisibility(View.VISIBLE);
                            doSubmit();

                        }
                    }
                }

            }
        }
    }

    public void doSubmit() {
        if (isSubmitting) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        isSubmitting = true;
        if (CGlobal.addressData == null)
        {
            Toast.makeText(getActivity(),"Invalid Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if (CGlobal.addressData.type == 1 && mInsertData.tp_lat == null) {

            Call<GooglePlaceDetailResult> googlePlaceDetailResultCall =  ApiClient.getIpClient("https://maps.googleapis.com/maps/").onGetPlaceDetail(CGlobal.addressData.placeId,Constants.google_browserkey,"en");
            googlePlaceDetailResultCall.enqueue(new Callback<GooglePlaceDetailResult>() {
                @Override
                public void onResponse(Call<GooglePlaceDetailResult> call, Response<GooglePlaceDetailResult> response) {
                    boolean fetch_failed = true;

                    GooglePlaceDetailResult gresult = response.body();
                    if (gresult.status.toLowerCase().equals("ok")){
                        mInsertData.tp_lat = gresult.result.geometry.location.lat;
                        mInsertData.tp_lon = gresult.result.geometry.location.lng;
                    }else{
                        doSubmit();
                        return;
                    }

                    if (gresult.result !=null){
                        List<GoogleAddressComponent> components = gresult.result.address_components;
                        for (GoogleAddressComponent component : components) {

                            if (component.types.indexOf("country") >= 0) {
                                Country country = CGlobal.dbManager.getCountryFromName(component.long_name);
                                if (country!=null){
                                    mInsertData.tp_countryid = country.countryID;
                                    mInsertData.tp_country = country.countryName;
                                    fetch_failed = false;
                                }
                                break;
                            }
                        }
                        if (fetch_failed){
                            mInsertData.tp_country = "Other";
                            mInsertData.tp_countryid = "ZZ1";
                        }
                        fetch_failed = false;
                    }
                    if (fetch_failed){
                        Country country = CGlobal.dbManager.getCountryFromLocalName("Other");
                        mInsertData.tp_country = "Other";
                        mInsertData.tp_countryid = "ZZ1";
                        mInsertData.tp_lat = country.latitude;
                        mInsertData.tp_lon = country.longitude;
                    }
                    uploadFile(0);
                }

                @Override
                public void onFailure(Call<GooglePlaceDetailResult> call, Throwable t) {
                    doSubmit();
                }
            });
        } else {
            uploadFile(0);
        }
    }

    private void uploadFile(int count) {
//        if (count == 0){
//            return;
//        }

        if (mInsertData.tp_countryid == null || mInsertData.tp_countryid.isEmpty()){
            doSubmit();
            return;
        }

        // create upload service client
        FileUploadService service =
                ApiClient.createService(FileUploadService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(mQuestionPhotoPath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<BaseModel> call = service.upload(description, body);
        call.enqueue(new retrofit2.Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, retrofit2.Response<BaseModel> response) {
                try {
                    if (response.body().response == 200) {

                        response.raw().toString();
                        String path1 = Constants.url + "assets/uploads/" + mQuestionPhotoPath_filenamepart + ".jpg";
                        String path2 = Constants.url + "assets/uploads/thumbnail/" + mQuestionPhotoPath_filenamepart + ".jpg";
                        path1 = mQuestionPhotoPath_filenamepart + ".jpg";
                        path2 = mQuestionPhotoPath_filenamepart + ".jpg";
                        mInsertData.tp_width = response.body().imginfo.tp_width;
                        mInsertData.tp_height = response.body().imginfo.tp_height;
                        makePost(path1, path2);
                    } else {
                        Toast.makeText(getActivity(), "Error on Uploading Image", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        isSubmitting = false;
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "Error on Uploading Image", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    isSubmitting = false;
                }

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error on Uploading Image", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                isSubmitting = false;
            }
        });
    }

    private void makePost(String path1, String path2) {
        mInsertData.tp_picpath = path1;
        mInsertData.tp_thumb = path2;

        Call<HotResponse> call = ApiClient.getApiClient().onActionMakePost(BaseModel.getQueryMap(mInsertData));
        call.enqueue(new retrofit2.Callback<HotResponse>() {
            @Override
            public void onResponse(Call<HotResponse> call, retrofit2.Response<HotResponse> response) {
                Toast.makeText(getActivity(), "Submit Done", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                makeDefault();

                ArrayList<TblBaseAch> newachlist = response.body().newachlist;
                if (newachlist != null && newachlist.size() > 0) {
                    TblBaseAch ach = newachlist.get(0);
                    String message = "New Travel Achievement Unlocked – " + ach.tba_name + "!";
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Congratulations")
                            .setMessage(message);

                    builder.show();
                }
                isSubmitting = false;
            }

            @Override
            public void onFailure(Call<HotResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                isSubmitting = false;
            }
        });
    }

    private void makeDefault() {
        edtTitle.setText("");
        edtLink.setText("");
        mEditCity.setText("");
        mQuestionPhotoPath = null;
        img_pic.setImageBitmap(null);
        CGlobal.addressData = null;
        mQuestionPhotoPath_filenamepart = null;
    }
    public static final int PERMISSIONS_REQUEST_UPLOAD = 2;
    private boolean checkValidate() {
        String title = edtTitle.getText().toString();
        String location = mEditCity.getText().toString();
        String hyperlink = edtLink.getText().toString();
        Country country;

        Set<String> missingPermissions = new HashSet<>();
        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            missingPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!missingPermissions.isEmpty()) {
            Toast.makeText(getActivity(),"To continue uploading, please enable TravPholer’s access to your Photo Album",Toast.LENGTH_SHORT).show();
            return false;
        }



        if (title.isEmpty()) {
            Toast.makeText(getActivity(), "Type title", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (location.isEmpty()) {
            Toast.makeText(getActivity(), "Type location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mQuestionPhotoPath == null) {
            Toast.makeText(getActivity(), "Pick Photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (hyperlink != null && !hyperlink.isEmpty() && !hyperlink.equals("http://")) {
            if (Patterns.WEB_URL.matcher(hyperlink).matches() == false) {
                Toast.makeText(getActivity(), "Type Valid Hyperlink", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (hyperlink.equals("http://")) {
            hyperlink = "";
        }

        mInsertData = new TblPhotos();
        mInsertData.tu_id = CGlobal.curUser.tu_id;
        mInsertData.tp_title = title;
        mInsertData.tp_location = location;
        mInsertData.tp_hyperlink = hyperlink;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(mQuestionPhotoPath, options);
        int width = options.outWidth;
        int height = options.outHeight;
        mInsertData.tp_width = String.valueOf(width);
        mInsertData.tp_height = String.valueOf(height);

        String p = mEditCity.getText().toString();
        if (CGlobal.addressData != null && p.equals(CGlobal.addressData.address)) {
            country = CGlobal.dbManager.getCountryFromName(CGlobal.addressData.country);
            if (CGlobal.addressData.type == 2) {
                mInsertData.tp_lat = String.valueOf(CGlobal.addressData.pos.latitude);
                mInsertData.tp_lon = String.valueOf(CGlobal.addressData.pos.longitude);
            }
            if (country == null && CGlobal.addressData.placeId == null) {
                country = CGlobal.dbManager.getCountryFromName("Other");
                mInsertData.tp_country = "Other";
                mInsertData.tp_countryid = "ZZ1";
                mInsertData.tp_lat = country.latitude;
                mInsertData.tp_lon = country.longitude;
            } else {
                if (country != null) {
                    mInsertData.tp_country = country.countryName;
                    mInsertData.tp_countryid = country.countryID;
                }

            }
        } else {

            if (CGlobal.addressData == null){
                Toast.makeText(getActivity(),"Location not detected",Toast.LENGTH_SHORT).show();
                return false;
            }
            country = CGlobal.dbManager.getCountryFromName("Other");
            mInsertData.tp_country = "Other";
            mInsertData.tp_countryid = "ZZ1";
            mInsertData.tp_lat = country.latitude;
            mInsertData.tp_lon = country.longitude;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                (permissions.length < 2 || grantResults[1] == PackageManager.PERMISSION_GRANTED)){
//            if (checkValidate()) {
//                progressBar.setVisibility(View.VISIBLE);
//                doSubmit();
//            }
//        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (CGlobal.photoFile != null && CGlobal.photoFile.exists()) {
                        //startPhotoZoom(Uri.fromFile(CGlobal.photoFile));
                        if (CGlobal.photoFile != null && CGlobal.photoFile.exists()) {
                            //startPhotoZoom(Uri.fromFile(CGlobal.photoFile));
                            mUriPhoto = Uri.fromFile(CGlobal.photoFile);

                            mQuestionPhotoPath = getRealPathFromURI(getActivity(), mUriPhoto);
                            File mFile = new File(mQuestionPhotoPath);

                            mQuestionPhotoOrientation = 0;
                            mQuestionPhotoMirror = false;
                            mQuestionSource = requestCode;
                            mOrientation = 90;

                            try {
                                android.media.ExifInterface exifInterface = new android.media.ExifInterface(mFile.getPath());
                                int rotation = exifInterface.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_NORMAL);
                                int rotationInDegrees = CGlobal.exifToDegrees(rotation);
                                mOrientation = rotationInDegrees;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //Log.e("CreateBid_Decode", String.valueOf(++CGlobal.logcount));
                            if (mQuestionPhotoPath_filenamepart != null)
                                Log.e("CreateBid_Decode", mQuestionPhotoPath_filenamepart);
                            DecodeTask decodeTask = new DecodeTask(mQuestionPhotoPath, mOrientation, false, requestCode);
                            decodeTask.execute();
                        }
                        ;
                    }
                } else {
                    //Toast.makeText(this,"Failed To Take Picture",Toast.LENGTH_SHORT).show();
                }
                break;
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();

                    mQuestionPhotoPath = getRealPathFromURI(getActivity(), selectedImageUri);

                    File mFile = new File(mQuestionPhotoPath);

                    mQuestionPhotoOrientation = 0;
                    mQuestionPhotoMirror = false;
                    mQuestionSource = requestCode;
                    mOrientation = 90;

                    try {
                        android.media.ExifInterface exifInterface = new android.media.ExifInterface(mFile.getPath());
                        int rotation = exifInterface.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = CGlobal.exifToDegrees(rotation);
                        mOrientation = rotationInDegrees;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    DecodeTask task = new DecodeTask(mQuestionPhotoPath, mOrientation, mQuestionPhotoMirror, requestCode);
                    task.execute();

                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            CGlobal.photoFile = null;
            try {
                CGlobal.photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (CGlobal.photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(CGlobal.photoFile));
                getActivity().startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Signup Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.hgc.reigndate.Activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Signup Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.hgc.reigndate.Activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {

            String ss = savedInstanceState.getString("title", "ddd");
            Log.e("ss onActivityCreated", ss);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (mQuestionPhotoPath != null && !mQuestionPhotoPath.isEmpty()) {
            final BitmapFactory.Options opts = new BitmapFactory.Options();
            // Down sample the image
            opts.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(mQuestionPhotoPath, opts);
            img_pic.setImageBitmap(bitmap);
        }

        // check permission
        Set<String> missingPermissions = new HashSet<>();
        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            missingPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!hasPermission(Manifest.permission.CAMERA)) {
            missingPermissions.add(Manifest.permission.CAMERA);
        }
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!missingPermissions.isEmpty()) {
            requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), PERMISSIONS_REQUEST_UPLOAD);
//            Toast.makeText(getActivity(),"To continue uploading, please enable TravPholer’s access to your Photo Album",Toast.LENGTH_SHORT).show();
            return;
        }


        Log.e("onActivityCreated", "onActivityCreated");
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return true;
        }
        return getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            String ss = savedInstanceState.getString("title", "ddd");
            Log.e("ss onViewStateRestored", ss);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("onViewStateRestored", "onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", edtTitle.getText().toString());
        try {
            String ss = outState.getString("title", "ddd");
            Log.e("ss onSaveInstanceState", ss);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("onSaveInstanceState", "onSaveInstanceState");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("onLowMemory", "onLowMemory");
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                WebFragment fragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", "http://denada.hk/en/content/3-terms-and-conditions-of-use");
                fragment.setArguments(bundle);
                replaceFragment(fragment);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private class DecodeTask extends AsyncTask<Void, Void, Bitmap> {
        private static final int DOWN_SAMPLE_FACTOR = 4;

        private final String mPath;
        private int mOrientation;
        private boolean mMirror;
        private int mSource;
        private double defWidth = 1600;
//        private double defHeight = 1200.0;

        //private double defRt      = 0.87;
        public DecodeTask(String path, int orientation, boolean mirror, int source) {
            mPath = path;
            mOrientation = orientation;
            mMirror = mirror;
            mSource = source;
        }

        private int getDownSample(double imgRatio, double orRatio, double width, double height) {
            int downsample = 0;
            // orRatio = height / width
            //보는 시점에서 높이를 너비로 나눈 비률을 가지고 처리한다.  imgRatio,orRatio다  이런 각도에서 본 비률이다.
            //이때 보는 시점에서의 너비와 높이가 파라메터로 들어온다.

            downsample = (int) (width / (defWidth));

            return downsample;
        }

        private Bitmap getScaledBitmap() {
            Bitmap retBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            //Returns null, sizes are in the options variable
            BitmapFactory.decodeFile(mPath, options);
            int width = options.outWidth;
            int height = options.outHeight;

            double imgRatio = (double) height / (double) width;
            //  mOrientation    0   90  180 270
            //  0   180         width bigger
            //  90  270         height bigger
            Matrix m = new Matrix();
            double orRatio = 1.33;
            Bitmap bitmap = null;
            int downsample = 0;
            switch (mSource) {
                case REQUEST_CAMERA:
                    if ((mOrientation != 0 || mMirror)) {
                        if (mMirror) {
                            // Flip horizontally
                            m.setScale(-1f, 1f);
                        }
                        m.preRotate(mOrientation);
                    }
                    if (mOrientation == 0 || mOrientation == 180) {
                        orRatio = 1.0 / orRatio;
                        downsample = getDownSample(imgRatio, orRatio, width, height);
                    } else {
                        downsample = getDownSample(1.0 / imgRatio, 1.0 / orRatio, height, width);
                    }
                    break;
                default:
                case SELECT_FILE:
                    orRatio = 1 / orRatio;
                    downsample = getDownSample(imgRatio, orRatio, width, height);
                    break;
            }
            //  assume that orRatio =  height / width

            bitmap = downSample(mPath, downsample);
            int imgW = bitmap.getWidth();
            int imgH = bitmap.getHeight();
            retBitmap = Bitmap.createBitmap(bitmap, 0, 0, imgW, imgH, m, false);

            return retBitmap;
        }

        public Bitmap downSample(String path, int downSampleFactor) {
            final BitmapFactory.Options opts = new BitmapFactory.Options();
            // Down sample the image
            opts.inSampleSize = downSampleFactor;
            return BitmapFactory.decodeFile(path, opts);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            // Decode image in background.
            Bitmap bitmap = null;

            try {
                return getScaledBitmap();
            } catch (Exception e) {
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] jpegData = baos.toByteArray();
                ExifInterface mJpegExif = Exif.getExif(jpegData);

                Long tsLong = System.currentTimeMillis() / 1000;
                mQuestionPhotoPath_filenamepart = tsLong.toString();
                String path = Storage.addImage(Storage.getCameraDirectory(getActivity()), jpegData, mJpegExif, mQuestionPhotoPath_filenamepart);

                if (path != null) {
                    mQuestionPhotoPath = path;
                    img_pic.setImageBitmap(bitmap);
                }
            } else {
                mQuestionPhotoPath = null;
            }
        }
    }
}

//            Places.GeoDataApi.getPlaceById(MainActivity.mGoogleApiClient, CGlobal.addressData.placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
//                @Override
//                public void onResult(PlaceBuffer places) {
//                    boolean fetch_failed = true;
//                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
//                        final Place myPlace = places.get(0);
//                        mInsertData.tp_lat = String.valueOf(myPlace.getLatLng().latitude);
//                        mInsertData.tp_lon = String.valueOf(myPlace.getLatLng().longitude);
//                        Log.i("Upload Fragment", "Place found: " + myPlace.getName());
//
//
//                        String latlng = String.valueOf(mInsertData.tp_lat) +","+ String.valueOf(mInsertData.tp_lon);
//                        Call<MapResponse> call =  ApiClient.getIpClient("http://maps.googleapis.com/maps/").onGetCityCountry(latlng, "false");
//                        call.enqueue(new Callback<MapResponse>() {
//                            @Override
//                            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
//                                MapResponse mapResponse = response.body();
//                                boolean found = false;
//                                Country country = null;
//                                if (mapResponse.status.toLowerCase().equals("ok")) {
//
//                                    List<GoogleAddressComponent> components = mapResponse.results.get(0).address_components;
//                                    for (GoogleAddressComponent component : components) {
//
//                                        if (component.types.indexOf("country") >= 0) {
//                                            country = CGlobal.dbManager.getCountryFromName(component.long_name);
//                                            break;
//                                        }
//                                    }
//                                }
//                                if (country == null) {
//                                    country = CGlobal.dbManager.getCountryFromName("Other");
//                                }
//                                mInsertData.tp_country = country.countryName;
//                                mInsertData.tp_countryid = country.countryID;
//
//                                uploadFile(0);
//                            }
//
//                            @Override
//                            public void onFailure(Call<MapResponse> call, Throwable t) {
//                                Toast.makeText(getActivity(), "Failed to Get Address", Toast.LENGTH_SHORT).show();
//                                Country country = CGlobal.dbManager.getCountryFromName("Other");
//                                mInsertData.tp_country = "Other";
//                                mInsertData.tp_countryid = "ZZ1";
//                                uploadFile(0);
//                            }
//                        });
//                    }else {
//                        Country country = CGlobal.dbManager.getCountryFromName("Other");
//                        mInsertData.tp_country = "Other";
//                        mInsertData.tp_countryid = "ZZ1";
//                        mInsertData.tp_lat = country.latitude;
//                        mInsertData.tp_lon = country.longitude;
//                        Log.e("Upload Fragment", "Place not found");
//                    uploadFile(0);
//                    }
//                    places.release();
//                }
//            });