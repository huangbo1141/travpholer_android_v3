package com.denada.travpholer.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.R;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Response.LoginResponse;
import com.denada.travpholer.model.TblBaseAch;
import com.denada.travpholer.model.TblUser;
import com.denada.travpholer.util.http.ApiClient;
import com.facebook.login.widget.LoginButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.RetrofitError;
import retrofit2.Call;

public class ProfileUserFragment extends BaseFragment implements View.OnClickListener {

    ProgressBar progressBar;
    CircleImageView circleImageView;
    ProgressBar progressBar_profile_image;
    TextView txt_rank_number, txt_likedplaces, txt_upload_photo, txt_conquered_count;
    TextView txt_current_level_name;
    LinearLayout scrollAch;
    View subRoot;
    View btnAction;
    LoginButton loginButton;
    String userid, title;
    String tabIndex;
    private View mRootView;
    private TextView txtName;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 200: {

                        //network succ
                        LoginResponse bidResponse = (LoginResponse) msg.obj;
                        if (bidResponse.response == 200) {
                            txtName.setText(bidResponse.row.tu_username);
                            String path = bidResponse.row.tu_pic;
                            progressBar_profile_image.setVisibility(View.VISIBLE);
                            Glide.with(getActivity())
                                    .load(path)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                            progressBar_profile_image.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                            progressBar_profile_image.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(circleImageView);

                            subRoot.setVisibility(View.VISIBLE);
                            txtName.setText(bidResponse.row.getUsername());
                            txt_rank_number.setText(bidResponse.info_rank.tbr_rank);
                            txt_likedplaces.setText(bidResponse.likedplaces);
                            txt_upload_photo.setText(bidResponse.upload_photo);
                            txt_conquered_count.setText(bidResponse.conqueredcount);

                            txt_current_level_name.setText(bidResponse.info_rank.tbr_title);

                            addViews(scrollAch, bidResponse.achievements);


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
                    case 400: {
                        //network fail
                        RetrofitError error = (RetrofitError) msg.obj;
                        if (error.getMessage() != null) {
                            Log.e("HomeActivity", error.getMessage());
                        }
                        Toast.makeText(getActivity(), "Network Erro", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        break;
                    }

                }
            } catch (Exception ex) {

            }

        }
    };
    private String mCountryId = null;

    private void addViews(LinearLayout root, List<TblBaseAch> achList) {
        root.removeAllViews();
        if (achList == null)
            return;
        for (int i = 0; i < achList.size(); i++) {
            View localView = LayoutInflater.from(getActivity()).inflate(R.layout.item_pic, null);
            ImageView imageView = (ImageView) localView.findViewById(R.id.img_icon);

            TblBaseAch item = achList.get(i);
            String path = CGlobal.getBaseIconPath("ach", item.tba_id, getActivity());
            Glide.with(getActivity())
                    .load(path)
                    .into(imageView);

            imageView.setOnClickListener(this);
            imageView.setTag(item);

            root.addView(localView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_profile_user, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        subRoot = mRootView.findViewById(R.id.scrollViewRoot);
        subRoot.setVisibility(View.GONE);

        btnAction = mRootView.findViewById(R.id.btnAction);
        btnAction.setOnClickListener(this);

        progressBar_profile_image = (ProgressBar) mRootView.findViewById(R.id.progressBar_profile_image);
        progressBar_profile_image.setVisibility(View.GONE);

        txt_rank_number = (TextView) mRootView.findViewById(R.id.txt_rank_number);
        txt_likedplaces = (TextView) mRootView.findViewById(R.id.txt_likedplaces);
        txt_upload_photo = (TextView) mRootView.findViewById(R.id.txt_upload_photo);
        txt_conquered_count = (TextView) mRootView.findViewById(R.id.txt_conquered_count);
        txtName = (TextView) mRootView.findViewById(R.id.txtName);

        txt_current_level_name = (TextView) mRootView.findViewById(R.id.txt_current_level_name);


        scrollAch = (LinearLayout) mRootView.findViewById(R.id.scrollAch);


        circleImageView = (CircleImageView) mRootView.findViewById(R.id.profile_image);


        setCaption(title, R.drawable.ico_back);

        btnAction = mRootView.findViewById(R.id.btnAction);
        btnAction.setOnClickListener(this);

        View img_rank = mRootView.findViewById(R.id.img_rank);
        img_rank.setOnClickListener(this);
        img_rank = mRootView.findViewById(R.id.img_likedplace);
        img_rank.setOnClickListener(this);
        img_rank = mRootView.findViewById(R.id.img_uploadphoto);
        img_rank.setOnClickListener(this);
        img_rank = mRootView.findViewById(R.id.img_conqueredcount);
        img_rank.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        try {
            userid = bundle.getString("tu_id");
            title = bundle.getString("title");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            tabIndex = bundle.getString("tabIndex");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TblUser user = new TblUser();
        user.tu_id = userid;
        user.action = "userinfo";
        progressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> call = ApiClient.getApiClient().onUserInfo(BaseModel.getQueryMap(user));
        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                mHandler.obtainMessage(200, response.body()).sendToTarget();

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        boolean processed = false;
        if (v.getTag() instanceof TblBaseAch) {
            TblBaseAch tblBaseAch = (TblBaseAch) v.getTag();
            CGlobal.showBallon(tblBaseAch.tba_name, v, getActivity());
            processed = true;

        }
        if (processed == false) {
            switch (id) {
                case R.id.btnAction: {
                    OtherPhotosFragment otherPhotosFragment = new OtherPhotosFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tu_id", userid);
                    bundle.putInt("mode", 2);
                    bundle.putString("tabIndex", tabIndex);
                    otherPhotosFragment.setArguments(bundle);
                    replaceFragment(otherPhotosFragment);

                    break;
                }
                case R.id.img_rank: {
                    CGlobal.showBallon("Rank", v, getActivity());
                    break;
                }

                case R.id.img_conqueredcount: {
                    CGlobal.showBallon("Countries Visited", v, getActivity());
                    break;
                }
                case R.id.img_uploadphoto: {
                    CGlobal.showBallon("Uploaded Photos", v, getActivity());
                    break;
                }
                case R.id.img_likedplace: {
                    CGlobal.showBallon("Bucket-List", v, getActivity());
                    break;
                }
            }
        }

    }

}
