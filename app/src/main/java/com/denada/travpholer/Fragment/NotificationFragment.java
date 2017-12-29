package com.denada.travpholer.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.adapter.RecyclerAdapterNotification;
import com.denada.travpholer.model.ActionType;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Response.HotResponseForNotification;
import com.denada.travpholer.model.TblNotificationTrack;
import com.denada.travpholer.model.TblUser;
import com.denada.travpholer.util.http.ApiClient;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends BaseFragment implements View.OnClickListener {


    ProgressBar progressBar;
    SwipeRefreshLayout swipeContainer;
    RecyclerAdapterNotification mAdapter;
    List<Object> list_data = new ArrayList<>();
    private View mRootView;
    private View.OnClickListener listViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ActionType actionType = (ActionType) v.getTag();
            final TblNotificationTrack item = (TblNotificationTrack) mAdapter.getItem(actionType.index);
            switch (actionType.actiontype) {
                default: {
                    OtherPhotosFragment otherPhotosFragment = new OtherPhotosFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tu_id", CGlobal.curUser.tu_id);
                    bundle.putInt("mode", 3);
                    bundle.putString("tp_countryid", item.tp_countryid);
                    bundle.putString("tp_ids", item.tp_id);
                    bundle.putString("tabIndex", Constants.BOTTOM_TAB_PROFILE);
                    otherPhotosFragment.setArguments(bundle);
                    replaceFragment(otherPhotosFragment);
                    break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_listview_mine, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        setCaption(null, R.drawable.ico_back);

        RecyclerView rv = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        setupRecyclerView(rv);

        swipeContainer = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
            }
        });

        rv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.BLACK)
                        .size(2)
                        .margin(0, 0)
                        .build());

        return mRootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mAdapter = new RecyclerAdapterNotification(getActivity(), list_data);
        mAdapter.setOnClickListner(listViewListener);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TblUser user = new TblUser();
        user.tu_id = CGlobal.curUser.tu_id;
        Call<HotResponseForNotification> call = ApiClient.getApiClient().onActionLoadNoti(BaseModel.getQueryMap(user));

        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<HotResponseForNotification>() {
            @Override
            public void onResponse(Call<HotResponseForNotification> call, Response<HotResponseForNotification> response) {
                HotResponseForNotification bidResponse = response.body();
                if (bidResponse.response == 200) {
                    if (bidResponse.rows != null && bidResponse.rows.size() > 0) {
                        List<Object> array = new ArrayList<Object>();
                        for (int i = 0; i < bidResponse.rows.size(); i++) {
                            array.add(bidResponse.rows.get(i));
                        }
                        list_data = array;
                        mAdapter.update(list_data);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HotResponseForNotification> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onClick(View v) {

    }
}
