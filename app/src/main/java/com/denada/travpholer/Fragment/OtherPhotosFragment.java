package com.denada.travpholer.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denada.travpholer.Activity.CommentActivity;
import com.denada.travpholer.Doc.AppFonts;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.adapter.RecyclerAdapterHot;
import com.denada.travpholer.model.ActionType;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.PhotoManager;
import com.denada.travpholer.model.Response.ActionResponse;
import com.denada.travpholer.model.Response.HotResponse;
import com.denada.travpholer.model.SearchTerm;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.model.TblLikes;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblReportAbuse;
import com.denada.travpholer.util.http.ApiClient;
import com.denada.travpholer.view.nice.NiceSpinner;
import com.denada.travpholer.view.nice.NiceSpinnerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherPhotosFragment extends BaseFragment implements View.OnClickListener {

    ProgressBar progressBar;
    ArrayList<Object> country_data = new ArrayList<>();
    NiceSpinner spinner;
    private SearchTerm searchTerm;
    private View mRootView;
    private TextView txtCoin2;
    private RecyclerAdapterHot mAdapter;

    private ArrayList<Object> countries = new ArrayList<>();
    private SwipeRefreshLayout swipeContainer;
    private String mCountryId, mTuid, mTitle, mTpIds;
    private Integer mMode;
    private String tabIndex;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private View profileAddView;
    private boolean isLoading = false;
    private int mSpinnerPos = 0;
    private ArrayList<TblPhotos> photosarray = new ArrayList<>();
    private String queueLabel;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100: {
                    spinner.setOnItemSelectedListener(onItemSelectedListener);
                    break;
                }
                case 200: {
                    //network succ
                    HotResponse bidResponse = (HotResponse) msg.obj;
                    bidResponse.parseForRanks();
                    if (bidResponse.response == 200) {
                        if (bidResponse.rows.size() > 0) {
                            addPhotos(bidResponse.rows, searchTerm);
                        }

                        if (mCountryId == null && country_data.size() == 0) {
                            if (bidResponse.rows_country != null && bidResponse.rows_country.size() > 0 && spinner != null) {
                                int index = spinner.getSelectedIndex();
                                String countryID = null;
                                if (index > 0 && index <= spinner.getAdapter().getCount()) {
                                    Country country = (Country) ((NiceSpinnerAdapter) spinner.getAdapter()).getMyItem(index);
                                    countryID = country.countryID;
                                }


                                country_data = new ArrayList<>();
                                country_data.add(new String("All Countries"));
                                for (int i = 0; i < bidResponse.rows_country.size(); i++) {
                                    Country item = bidResponse.rows_country.get(i);
                                    country_data.add(item);
                                    if (item.countryID.equals(countryID)) {
                                        index = i;
                                    }
                                }


                                spinner.setOnItemSelectedListener(null);
                                spinner.attachDataSource(country_data);
                                if (countryID != null) {
                                    spinner.setSelectedIndex(index + 1);
                                }
                                mHandler.sendEmptyMessageDelayed(100, 2000);
                            }
                        }

                    } else {
//                        Toast.makeText(HomeActivity.this,"Username or Password is not correct",Toast.LENGTH_SHORT).show();
                        if (bidResponse.error != null)
                            Log.d("HomeActivity", bidResponse.error);
                        else
                            Log.d("HomeActivity", "eee");
                    }

                    onLoad();
                    break;
                }
                case 400: {
                    //network fail
                    RetrofitError error = (RetrofitError) msg.obj;
                    if (error.getMessage() != null) {
                        Log.e("HomeActivity", error.getMessage());
                    }

                    onLoad();
                    Toast.makeText(getActivity(), "Network Erro", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1200: {
                    //network succ
                    ActionResponse bidResponse = (ActionResponse) msg.obj;
                    if (bidResponse.response == 200 && bidResponse.tblPhotos != null) {
                        bidResponse.tblPhotos.owner.setTblBaseRank();
                        Intent intent = new Intent();
                        intent.putExtra("data", bidResponse.tblPhotos);
                        intent.putExtra("actiontype", 0);
                        if (queueLabel == null) {
                            getMyToken(OtherPhotosFragment.this.getClass().getName());
                        }
                        intent.putExtra("controller", queueLabel);
                        intent.setAction(Constants.BROADCAST_PHOTOCHANGED);
                        getActivity().sendBroadcast(intent);
                    } else {
//                        Toast.makeText(HomeActivity.this,"Username or Password is not correct",Toast.LENGTH_SHORT).show();
                        if (bidResponse.error != null)
                            Log.d("HomeActivity", bidResponse.error);
                        else
                            Log.d("HomeActivity", "eee");
                    }
                    progressBar.setVisibility(View.GONE);
                    break;
                }
                case 2200: {
                    //network succ
                    ActionResponse bidResponse = (ActionResponse) msg.obj;
                    if (bidResponse.response == 200) {
                        TblReportAbuse item = bidResponse.tblReportAbuse;
                        Intent intent = new Intent();
                        intent.putExtra("data", item);
                        intent.putExtra("actiontype", 1);
                        if (queueLabel == null) {
                            getMyToken(OtherPhotosFragment.this.getClass().getName());
                        }
                        intent.putExtra("controller", queueLabel);
                        intent.setAction(Constants.BROADCAST_PHOTOCHANGED);
                        getActivity().sendBroadcast(intent);
                    } else {
//                        Toast.makeText(HomeActivity.this,"Username or Password is not correct",Toast.LENGTH_SHORT).show();
                        if (bidResponse.error != null)
                            Log.d("HomeActivity", bidResponse.error);
                        else
                            Log.d("HomeActivity", "eee");
                    }
                    progressBar.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };
    private View.OnClickListener listViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ActionType actionType = (ActionType) v.getTag();
            final TblPhotos item = (TblPhotos) mAdapter.getItem(actionType.index);
            switch (actionType.actiontype) {
                case 0: {
                    //like action
                    // request like action for this photo

                    TblLikes tblLikes = new TblLikes();
                    tblLikes.tu_id = CGlobal.curUser.tu_id;
                    tblLikes.tp_id = item.tp_id;
                    tblLikes.tl_name = CGlobal.curUser.getUsername();
                    if (item.ilikethis == null || item.ilikethis.isEmpty() || item.ilikethis.equals("-1")) {
                        tblLikes.tu_like = "1";
                        item.ilikethis = "-1";
                        item.likescount = String.valueOf(Integer.valueOf(item.likescount) + 1);
                        if (v instanceof ImageView) {
                            ImageView tempImage = (ImageView) v;
                            tempImage.setImageResource(R.drawable.unlike);
                        }
                        if (actionType.txtLikesCount!=null){
                            actionType.txtLikesCount.setText(item.likescount);
                        }
                    } else {
                        tblLikes.tu_like = "0";
                        item.ilikethis = null;
                        item.likescount = String.valueOf(Integer.valueOf(item.likescount) - 1);
                        if (v instanceof ImageView) {
                            ImageView tempImage = (ImageView) v;
                            tempImage.setImageResource(R.drawable.like);
                        }
                        if (actionType.txtLikesCount!=null){
                            actionType.txtLikesCount.setText(item.likescount);
                        }
                    }

//                    final ArrayList<TblPhotos> list_data = photos();
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.update(list_data);
//                            Log.d("update in hot", "runonuithread");
//                        }
//                    });

                    //progressBar.setVisibility(View.VISIBLE);

                    Call<ActionResponse> call = ApiClient.getApiClient().onActionLikePic(BaseModel.getQueryMap(tblLikes));
                    call.enqueue(new retrofit2.Callback<ActionResponse>() {
                        @Override
                        public void onResponse(Call<ActionResponse> call, retrofit2.Response<ActionResponse> response) {
                            mHandler.obtainMessage(1200, response.body()).sendToTarget();
                        }

                        @Override
                        public void onFailure(Call<ActionResponse> call, Throwable t) {

                        }
                    });
                    break;
                }
                case 1: {
                    //report action
                    if (item.tu_id.equals(CGlobal.curUser.tu_id)) {
                        Toast.makeText(getActivity(), "User cannot report own photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final CharSequence[] items = {"Copyright Violation", "Spam",
                            "Offensive Materials", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Report");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 3) {
                                dialog.dismiss();
                                return;
                            }
                            report(item, actionType);
                        }
                    });
                    builder.show();
                    break;
                }
                case 2: {
                    // click name
                    ProfileUserFragment fragment = new ProfileUserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tu_id", item.owner.tu_id);
                    bundle.putString("title", item.owner.tu_firstname);
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                    break;
                }
                case 3: {
                    //share action
                    Bitmap bitmap = ((BitmapDrawable) actionType.imageView.getDrawable()).getBitmap();
                    Uri uriToImage = CGlobal.getImageUri(getActivity(), bitmap);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, "Share Image..."));
                    break;
                }
                case 4: {
                    ///commet
                    break;
                }
                case 5: {
                    if (item.tp_hyperlink == null || item.tp_hyperlink.isEmpty()) {
                        return;
                    }

                    WebFragment fragment = new WebFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", item.tp_hyperlink);
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);

                    break;
                }
                case 6: {
                    //read more
                    TblPhotos tblPhotos = (TblPhotos) mAdapter.getItem(actionType.index);
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
                    intent.putExtra("data", tblPhotos);
                    startActivity(intent);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mSpinnerPos == position) {
                return;
            }
            mSpinnerPos = position;
            searchTerm = new SearchTerm();
            searchTerm.tu_id = mTuid;
            searchTerm.visitor_id = CGlobal.curUser.tu_id;
            if (mSpinnerPos > 0 && mSpinnerPos <= ((NiceSpinnerAdapter) parent.getAdapter()).getCount()) {
                Country country = (Country) ((NiceSpinnerAdapter) parent.getAdapter()).getMyItem(mSpinnerPos);
                mCountryId = country.countryID;
                searchTerm.tp_countryid = mCountryId;
            }
            deleteAllPhotos();
            loadMore();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.BROADCAST_PHOTOCHANGED)) {
                int actiontype = intent.getIntExtra("actiontype", -1);
                String controller = intent.getStringExtra("controller");
                switch (actiontype) {
                    case 0: {
                        try {
                            TblPhotos item = (TblPhotos) intent.getSerializableExtra("data");
                            if (item != null) {
                                synchronized (photosarray) {
                                    for (TblPhotos tblPhotos : photosarray) {
                                        if (tblPhotos.tp_id.equals(item.tp_id)) {
                                            tblPhotos.ilikethis = item.ilikethis;
                                            tblPhotos.likescount = item.likescount;
                                        }
                                    }

                                    if (!controller.equals(queueLabel)) {
                                        postContentChangeNotification();
                                    }
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                    case 1: {
                        try {
                            TblReportAbuse item = (TblReportAbuse) intent.getSerializableExtra("data");
                            if (item != null) {
                                synchronized (photosarray) {
                                    ArrayList<TblPhotos> discardedItems = new ArrayList<>();
                                    for (TblPhotos tblPhotos : photosarray) {
                                        if (tblPhotos.tp_id.equals(item.tp_id)) {
                                            discardedItems.add(tblPhotos);
                                        }
                                    }
                                    if (discardedItems.size() > 0) {
                                        photosarray.removeAll(discardedItems);
                                        if (!controller.equals(queueLabel)) {
                                            postContentChangeNotification();
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                    case 4: {
                        try {
                            TblComment item = (TblComment) intent.getSerializableExtra("data");
                            String comment_count = intent.getStringExtra("comment_count");
                            if (item != null) {
                                synchronized (photosarray) {

                                    for (TblPhotos tblPhotos : photosarray) {
                                        if (tblPhotos.tp_id.equals(item.tc_tpid)) {
                                            tblPhotos.commentcount = comment_count;
                                        }
                                    }
                                    if (!controller.equals(queueLabel)) {
                                        postContentChangeNotification();
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    };

    public ArrayList<TblPhotos> photos() {
        synchronized (photosarray) {
            return photosarray;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (mCountryId == null) {

            // load other user's photo so setup combobox
            mRootView = inflater.inflate(R.layout.fragment_listview, container, false);
            spinner = (NiceSpinner) mRootView.findViewById(R.id.spinner);
            List<Object> data = new ArrayList<>();
            data.add(new String("Filter by Country"));
            spinner.attachDataSource(data);
            spinner.setVisibility(View.VISIBLE);
        } else {
            mRootView = inflater.inflate(R.layout.fragment_listview_mine, container, false);
        }


        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        RecyclerView rv = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        setupRecyclerView(rv);

        swipeContainer = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setRefreshing(false);
        swipeContainer.setEnabled(false);

        searchTerm = new SearchTerm();
        searchTerm.tu_id = mTuid;
        searchTerm.tp_countryid = mCountryId;
//        searchTerm.visitor_id = CGlobal.curUser.tu_id;

        switch (mMode) {
            case 0: {
                //to go
                String name = CGlobal.curUser.tu_firstname;
                setCaption(name + "'s Bucket-List", R.drawable.ico_back);
                break;
            }
            case 1: {
                //conquered
                String name = CGlobal.curUser.tu_firstname;
                setCaption(name + "'s Adventure", R.drawable.ico_back);
                break;
            }
            case 2: {
                // other
                setCaption(mTitle, R.drawable.ico_back);
                break;
            }
            case 3: {
                //togo
                setCaption(mTitle, R.drawable.ico_back);
                break;
            }
            case 4: {
                //conquered
                setCaption(mTitle, R.drawable.ico_back);
                break;
            }
        }

        setCaption("", R.drawable.ico_back);
        queueLabel = getMyToken(getClass().getName());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_PHOTOCHANGED);
        filter.addAction(queueLabel);
        getActivity().registerReceiver(mBroadCastReceiver, filter);

        return mRootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mBroadCastReceiver);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        ArrayList<TblPhotos> list_data = new ArrayList<>();
        mAdapter = new RecyclerAdapterHot(getActivity(), list_data);
        mAdapter.setOnClickListner(listViewListener);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTuid = this.getArguments().getString("tu_id");
            mMode = this.getArguments().getInt("mode");
            mCountryId = this.getArguments().getString("tp_countryid");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            mTitle = this.getArguments().getString("title");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            mTpIds = this.getArguments().getString("tp_ids");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            tabIndex = this.getArguments().getString("tabIndex");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initFonts() {
        Typeface openSans = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.OPENSANS_SEMIBOLD);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);

        ArrayList<TblPhotos> tblPhotoses = photos();
        if (tblPhotoses.size() == 0)
            loadMore();
        else {

            try {
                mAdapter.update(tblPhotoses);
                if (country_data.size() != 0) {
                    spinner.attachDataSource(country_data);
                    mHandler.sendEmptyMessageDelayed(100, 2000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public void loadMore() {
        isLoading = true;
        String path = null;
        switch (mMode) {
            case 0: {
                path = CGlobal.getLastFileName(Constants.ACTION_TOGO);
                break;
            }
            case 1: {
                path = CGlobal.getLastFileName(Constants.ACTION_CONQUERED);
                break;
            }
            case 2: {
                path = CGlobal.getLastFileName(Constants.ACTION_CONQUERED);
                break;
            }
            case 3: {
                path = CGlobal.getLastFileName(Constants.ACTION_TOGO_IDS);
                searchTerm.tp_ids = mTpIds;
                break;
            }
            case 4: {
                path = CGlobal.getLastFileName(Constants.ACTION_TOGO_IDS);
                searchTerm.tp_ids = mTpIds;
                break;
            }

        }
        searchTerm.visitor_id = CGlobal.curUser.tu_id;
        Call<HotResponse> call = ApiClient.getApiClient().onTemplateRequestForHotResponse(path, BaseModel.getQueryMap(searchTerm));
        call.enqueue(new Callback<HotResponse>() {
            @Override
            public void onResponse(Call<HotResponse> call, Response<HotResponse> response) {
                mHandler.obtainMessage(200, response.body()).sendToTarget();
            }

            @Override
            public void onFailure(Call<HotResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                onLoad();
            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUESTCODE_UPDATEPROFILE:
                break;
        }
    }

    private void onLoad() {
        swipeContainer.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        isLoading = false;
    }

    // Fresh

    public void onRefresh() {
        if (isLoading) {
            swipeContainer.setRefreshing(false);
            return;
        }

        ArrayList<TblPhotos> list_data = photos();
        TblPhotos item = list_data.get(0);

        searchTerm = new SearchTerm();
        searchTerm.tu_id = mTuid;
        searchTerm.tp_countryid = mCountryId;
        searchTerm.visitor_id = CGlobal.curUser.tu_id;

        if (list_data.size() > 0) {
            //pull to refresh
            //searchTerm = new SearchTerm();
            searchTerm.tp_id_viewtop = item.likescount;
            searchTerm.create_datetime = item.create_datetime;
            searchTerm.tp_fetcharrow = "1";
        }
        loadMore();
    }

    private void report(int index, ActionType actionType) {
        progressBar.setVisibility(View.VISIBLE);
        TblPhotos item = (TblPhotos) mAdapter.getItem(actionType.index);

        TblReportAbuse tblLikes = new TblReportAbuse();
        tblLikes.tu_id = CGlobal.curUser.tu_id;
        tblLikes.tp_id = item.tp_id;
        tblLikes.tr_type = String.valueOf(index);

        Call<ActionResponse> call = ApiClient.getApiClient().onActionReport(BaseModel.getQueryMap(tblLikes));
        call.enqueue(new retrofit2.Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, retrofit2.Response<ActionResponse> response) {
                mHandler.obtainMessage(2200, response.body()).sendToTarget();
            }

            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {

            }
        });
        deletePhotos(item);
    }

    public void deleteAllPhotos() {
        synchronized (photosarray) {
            photosarray.clear();
            postContentChangeNotification();
        }
    }

    public void deletePhotos(TblPhotos photo) {
        if (photo != null) {
            synchronized (photosarray) {
                List<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item : photosarray) {
                    if (item.tp_id.equals(photo.tp_id))
                        discardedItems.add(item);
                }
                photosarray.removeAll(discardedItems);
                postContentChangeNotification();
            }
        }
    }

    public void addPhoto(TblPhotos photo) {
        if (photo != null) {
            synchronized (photosarray) {
                List<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item : photosarray) {
                    if (item.tp_id.equals(photo.tp_id)) {
                        discardedItems.add(item);
                    }
                }
                photosarray.removeAll(discardedItems);

                photosarray.add(photo);
                postContentChangeNotification();
            }
        }

    }

    public void addPhotos(ArrayList<TblPhotos> photoArray, SearchTerm searchTerm) {
        if (photoArray != null && photoArray.size() > 0) {
            synchronized (photosarray) {
                HashMap<String, String> hashMap = new HashMap<>();
                for (TblPhotos item : photoArray) {
                    hashMap.put(item.tp_id, item.tp_id);
                }

                ArrayList<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item : photosarray) {
                    if (hashMap.containsKey(item.tp_id)) {
                        discardedItems.add(item);
                    }
                }

                photosarray.removeAll(discardedItems);

                if (searchTerm != null && searchTerm.tp_fetcharrow != null && searchTerm.tp_fetcharrow.equals("1")) {
                    ArrayList<TblPhotos> temp = new ArrayList<>();
                    temp.addAll(photoArray);
                    temp.addAll(photosarray);
                    photosarray = temp;
                } else {
                    photosarray.addAll(photoArray);
                }

                postContentChangeNotification();
            }
        }
    }

    public void postContentChangeNotification() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.update(photosarray);
                }
            });
        }

    }
}
