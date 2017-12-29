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
import com.denada.travpholer.model.TblLikes;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblReportAbuse;
import com.denada.travpholer.util.http.ApiClient;
import com.denada.travpholer.view.nice.NiceSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit2.Call;

;

public class HotFragment extends BaseFragment implements View.OnClickListener {


    public ExploreFragment parentFragment;
    public ArrayList<Object> hotCountry = new ArrayList<>();
    ProgressBar progressBar;
    String token;
    private SearchTerm searchTerm;
    private View mRootView;
    private TextView txtCoin2;
    private RecyclerAdapterHot mAdapter;
    BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.BROADCAST_CONTENTCHANGED_HOT)) {
                PhotoManager photoManager = PhotoManager.getInstance(getActivity());
                final ArrayList<TblPhotos> list = photoManager.photosHot();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.update(list);
                    }
                });

            }
        }
    };
    private SwipeRefreshLayout swipeContainer;
    private boolean isLoading = false;
    private int mSpinnerPos = 0;
    private View.OnClickListener listViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ActionType actionType = (ActionType) v.getTag();
            final TblPhotos item = (TblPhotos) mAdapter.getItem(actionType.index);
            switch (actionType.actiontype) {
                case 0: {
                    //like action
                    // request like action for this photo
                    //progressBar.setVisibility(View.VISIBLE);


                    TblLikes tblLikes = new TblLikes();
                    tblLikes.tu_id = CGlobal.curUser.tu_id;
                    tblLikes.tp_id = item.tp_id;
                    tblLikes.tl_name = CGlobal.curUser.getUsername();
                    if (item.ilikethis == null || item.ilikethis.isEmpty() || item.ilikethis.equals("-1")) {
                        tblLikes.tu_like = "1";
                        item.ilikethis = "-1";
                        item.likescount = String.valueOf(Integer.valueOf(item.likescount) + 1);
                        if (v instanceof ImageView){
                            ImageView tempImage = (ImageView)v;
                            tempImage.setImageResource(R.drawable.unlike);
                        }
                        if (actionType.txtLikesCount!=null){
                            actionType.txtLikesCount.setText(item.likescount);
                        }
                    } else {
                        tblLikes.tu_like = "0";
                        item.ilikethis = null;
                        item.likescount = String.valueOf(Integer.valueOf(item.likescount) - 1);
                        if (v instanceof ImageView){
                            ImageView tempImage = (ImageView)v;
                            tempImage.setImageResource(R.drawable.like);
                        }
                        if (actionType.txtLikesCount!=null){
                            actionType.txtLikesCount.setText(item.likescount);
                        }
                    }

//                    PhotoManager photoManager = PhotoManager.getInstance(getActivity());
//                    final ArrayList<TblPhotos> list_data = photoManager.photosHot();
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.update(list_data);
//                            Log.d("update in hot", "runonuithread");
//                        }
//                    });


                    Call<ActionResponse> call = ApiClient.getApiClient().onActionLikePic(BaseModel.getQueryMap(tblLikes));
                    call.enqueue(new retrofit2.Callback<ActionResponse>() {
                        @Override
                        public void onResponse(Call<ActionResponse> call, retrofit2.Response<ActionResponse> response) {

                            mHandler.obtainMessage(1200, response.body()).sendToTarget();
                            Log.d("update in hot", "callback");
                        }

                        @Override
                        public void onFailure(Call<ActionResponse> call, Throwable t) {
                            Log.d("fail","fail");
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
                    bundle.putString("tabIndex", Constants.BOTTOM_TAB_EXPLORE);
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

//                    Intent intent = new Intent(getActivity(), CommentActivity.class);
//                    intent.putExtra("photo", item);
//                    startActivity(intent);

                    break;
                }
                case 4: {
                    //commet
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
            searchTerm.tu_id = CGlobal.curUser.tu_id;
            if (mSpinnerPos > 0 && mSpinnerPos <= ((NiceSpinnerAdapter) parent.getAdapter()).getCount()) {
                Country country = (Country) ((NiceSpinnerAdapter) parent.getAdapter()).getMyItem(mSpinnerPos);
                searchTerm.tp_countryid = country.countryID;
            }
            PhotoManager photoManager = PhotoManager.getInstance(getActivity());
            photoManager.deleteAllPhotosHot();
            loadMore();
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
                        parentFragment.spinner1.setOnItemSelectedListener(onItemSelectedListener);
                        break;
                    }
                    case 200: {
                        //network succ
                        HotResponse bidResponse = (HotResponse) msg.obj;
                        bidResponse.parseForRanks();
                        if (bidResponse.response == 200) {
                            if (bidResponse.rows.size() > 0) {
                                PhotoManager photoManager = PhotoManager.getInstance(getActivity());
                                photoManager.addPhotosHot(bidResponse.rows, searchTerm);
                            }
                            if (bidResponse.rows_country != null && bidResponse.rows_country.size() > 0 ) {
                                int index = parentFragment.spinner1.getSelectedIndex();
                                String countryID = null;
                                if (index > 0 && index <= parentFragment.spinner1.getAdapter().getCount()) {
                                    Country country = (Country) ((NiceSpinnerAdapter) parentFragment.spinner1.getAdapter()).getMyItem(index);
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

                                hotCountry = data;
                                parentFragment.spinner1.setOnItemSelectedListener(null);
                                parentFragment.spinner1.attachDataSource(hotCountry);
                                if (countryID != null) {
                                    parentFragment.spinner1.setSelectedIndex(index + 1);
                                }
                                mHandler.sendEmptyMessageDelayed(100, 2000);
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

                        if (bidResponse.response == 200) {
                            TblLikes item = bidResponse.tblLikes;
                            if (bidResponse.tblPhotos != null && bidResponse.tblPhotos != null) {

                                bidResponse.tblPhotos.owner.setTblBaseRank();
                                Intent intent = new Intent();
                                intent.putExtra("data", bidResponse.tblPhotos);
                                intent.putExtra("actiontype", 0);
                                intent.putExtra("controller", "hot");
                                intent.setAction(Constants.BROADCAST_PHOTOCHANGED);
                                getActivity().sendBroadcast(intent);
                            }

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
                            intent.putExtra("controller", "hot");
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    };

    //    NiceSpinnerAdapter adapter;
//    NiceSpinnerAdapter<T> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_cheese_list, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        RecyclerView rv = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        setupRecyclerView(rv);

        swipeContainer = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                HotFragment.this.onRefresh();
            }
        });

        searchTerm = new SearchTerm();
        searchTerm.tu_id = CGlobal.curUser.tu_id;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_CONTENTCHANGED_HOT);
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
        List<TblPhotos> data = new ArrayList<>();
        mAdapter = new RecyclerAdapterHot(getActivity(), data);
        mAdapter.setOnClickListner(listViewListener);
        recyclerView.setAdapter(mAdapter);
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

        PhotoManager photoManager = PhotoManager.getInstance(getActivity());
        ArrayList<TblPhotos> tblPhotoses = photoManager.photosHot();
        if (tblPhotoses.size() == 0)
            loadMore();
        else {
            mAdapter.update(tblPhotoses);
        }
        try {

            if (hotCountry.size() != 0) {
                parentFragment.spinner1.attachDataSource(hotCountry);
                mHandler.sendEmptyMessageDelayed(100, 2000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadMore() {
        isLoading = true;

        progressBar.setVisibility(View.VISIBLE);
        Call<HotResponse> call = ApiClient.getApiClient().onLoadHot(BaseModel.getQueryMap(searchTerm));

        call.enqueue(new retrofit2.Callback<HotResponse>() {
            @Override
            public void onResponse(Call<HotResponse> call, retrofit2.Response<HotResponse> response) {
                mHandler.obtainMessage(200, response.body()).sendToTarget();
            }

            @Override
            public void onFailure(Call<HotResponse> call, Throwable t) {

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

//    public String getMyToken(){
//        return "hot";
//    }

    public void onRefresh() {
        if (isLoading) {
            swipeContainer.setRefreshing(false);
            return;
        }
        searchTerm = new SearchTerm();
        searchTerm.tu_id = CGlobal.curUser.tu_id;
        PhotoManager pmanager = PhotoManager.getInstance(getActivity());
        ArrayList<TblPhotos> list_data = pmanager.photosHot();
        if (list_data.size() > 0) {
            //pull to refresh
            TblPhotos item = list_data.get(0);
            searchTerm.tp_id_viewtop = item.likes;
            searchTerm.create_datetime = item.create_datetime;
            searchTerm.tp_fetcharrow = "1";
            searchTerm.tp_steps = "10";
        }
        if (mSpinnerPos > 0 && hotCountry.size() > 0 && hotCountry.size() > mSpinnerPos - 1) {
            Country country = (Country) hotCountry.get(mSpinnerPos - 1);
            searchTerm.tp_countryid = country.countryID;
        }
        loadMore();
    }

    private void report(int index, ActionType actionType) {
        progressBar.setVisibility(View.VISIBLE);
        TblPhotos item = mAdapter.getItem(actionType.index);

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

        PhotoManager photoManager = PhotoManager.getInstance(getActivity());
        photoManager.deletePhotosHot(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String classname = getClass().getName();
        token = getMyToken(classname);

        Log.d("Fresh Create ", token);
    }
}
