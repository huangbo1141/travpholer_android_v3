package com.denada.travpholer.Fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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
import com.denada.travpholer.Activity.MainActivity;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
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

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    ProgressBar progressBar;

    LoginResponse bidResponse;
    CircleImageView circleImageView;
    ProgressBar progressBar_profile_image;
    View btnLogout;
    TextView txt_rank_number, txt_likedplaces, txt_upload_photo, txt_conquered_count, txt_likes_left;
    TextView txt_current_level_name, txt_next_level_name, txt_current_level_name_lbl;
    ImageView img_cur_icon, img_next_icon;
    LinearLayout scrollAch;
    View subRoot, btnNotification;
    LoginButton loginButton;
    private View mRootView;
    private TextView txtName;
    private ImageView img_pross;
    private View view_pross;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 200: {

                        //network succ
                        bidResponse = (LoginResponse) msg.obj;
                        if (bidResponse.response == 200) {

                            subRoot.setVisibility(View.VISIBLE);
                            txtName.setText(bidResponse.row.getUsername());
                            txt_rank_number.setText(bidResponse.info_rank.tbr_rank);
                            txt_likedplaces.setText(bidResponse.likedplaces);
                            txt_upload_photo.setText(bidResponse.upload_photo);
                            txt_conquered_count.setText(bidResponse.conqueredcount);

                            txt_current_level_name.setText(bidResponse.info_rank.tbr_title);
                            Glide.with(getActivity())
                                    .load(CGlobal.getBaseIconPath("rank", bidResponse.info_rank.tbr_id, getActivity()))
                                    .into(img_cur_icon);

                            img_cur_icon.setOnClickListener(ProfileFragment.this);

                            txt_next_level_name.setText(bidResponse.info_rank_next.tbr_title);
                            Glide.with(getActivity())
                                    .load(CGlobal.getBaseIconPath("rank", bidResponse.info_rank_next.tbr_id, getActivity()))
                                    .into(img_next_icon);

                            img_next_icon.setOnClickListener(ProfileFragment.this);
                            txt_likes_left.setText(bidResponse.likes_left);
                            addViews(scrollAch, bidResponse.achievements);

                            //calculate width for progress
                            float width = Integer.valueOf(bidResponse.info_rank_next.tbr_like) - Integer.valueOf(bidResponse.info_rank.tbr_like);
                            float xpos = Integer.valueOf(bidResponse.likes_recv) - Integer.valueOf(bidResponse.info_rank.tbr_like);
                            float percentage = xpos / width;
                            float view_prossWidth = view_pross.getWidth();
                            int newwidth = (int) (view_prossWidth * percentage);
                            ViewGroup.LayoutParams layoutParams = img_pross.getLayoutParams();
                            layoutParams.width = newwidth;
                            img_pross.setLayoutParams(layoutParams);

                            Activity activity = getActivity();

                            Resources resources = activity.getResources();
                            float scale = resources.getDisplayMetrics().density;
                            int textsize = (int) (14 * scale);
                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            paint.setStyle(Paint.Style.STROKE);
                            // paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                            paint.setColor(Color.rgb(61, 61, 61));
                            paint.setTextSize(textsize);
                            Rect bounds = new Rect();
                            String string = bidResponse.info_rank_next.tbr_title;
                            paint.getTextBounds(string, 0, string.length(), bounds);
                            int txtnextlevelnamewidth = (int) (CGlobal.display.getWidth() - scale * 80 * 2);
                            int pp = bounds.width();
                            Rect rankbound = new Rect();
                            paint.getTextBounds("Rank", 0, "Rank".length(), rankbound);
                            pp = rankbound.width();
                            if (bounds.width() + rankbound.width() >= txtnextlevelnamewidth) {
                                txt_current_level_name_lbl.setGravity(Gravity.CENTER);
                                txt_current_level_name.setHeight(textsize * 2 + 10);
                                txt_current_level_name.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                            } else {
                                txt_current_level_name_lbl.setGravity(Gravity.LEFT);
                                txt_current_level_name.setGravity(Gravity.RIGHT);
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
                    case 2999: {
                        int viewid = (int) msg.obj;
                        switch (viewid) {
                            case R.id.img_rank: {

                                break;
                            }
                        }
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
            View localView = LayoutInflater.from(getActivity()).inflate(R.layout.item_ach, null);
            TextView title = (TextView) localView.findViewById(R.id.txt_title);
            TextView desc = (TextView) localView.findViewById(R.id.txt_desc);
            ImageView imageView = (ImageView) localView.findViewById(R.id.img_icon);
            ;

            TblBaseAch item = achList.get(i);
            title.setText(item.tba_name);
            desc.setText(item.tba_desc);

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

        mRootView = inflater.inflate(R.layout.fragment_profile, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        subRoot = mRootView.findViewById(R.id.scrollViewRoot);
//        subRoot.setVisibility(View.GONE);

        progressBar_profile_image = (ProgressBar) mRootView.findViewById(R.id.progressBar_profile_image);
        progressBar_profile_image.setVisibility(View.GONE);

        txt_rank_number = (TextView) mRootView.findViewById(R.id.txt_rank_number);
        txt_likedplaces = (TextView) mRootView.findViewById(R.id.txt_likedplaces);
        txt_upload_photo = (TextView) mRootView.findViewById(R.id.txt_upload_photo);
        txt_conquered_count = (TextView) mRootView.findViewById(R.id.txt_conquered_count);
        txtName = (TextView) mRootView.findViewById(R.id.txtName);

        txt_current_level_name = (TextView) mRootView.findViewById(R.id.txt_current_level_name);
        txt_current_level_name_lbl = (TextView) mRootView.findViewById(R.id.txt_current_level_name_lbl);
        txt_next_level_name = (TextView) mRootView.findViewById(R.id.txt_next_level_name);
        txt_likes_left = (TextView) mRootView.findViewById(R.id.txt_likes_left);

        img_cur_icon = (ImageView) mRootView.findViewById(R.id.img_current_icon);
        img_next_icon = (ImageView) mRootView.findViewById(R.id.img_next_icon);

        scrollAch = (LinearLayout) mRootView.findViewById(R.id.scrollAch);

        view_pross = mRootView.findViewById(R.id.view_pross_container);
        img_pross = (ImageView) mRootView.findViewById(R.id.img_pross_value);
        circleImageView = (CircleImageView) mRootView.findViewById(R.id.profile_image);


        setCaption("Profile", -1);

        btnLogout = mRootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);


        View img_rank = mRootView.findViewById(R.id.img_rank);
        img_rank.setOnClickListener(this);
        img_rank = mRootView.findViewById(R.id.img_likedplace);
        img_rank.setOnClickListener(this);
        img_rank = mRootView.findViewById(R.id.img_uploadphoto);
        img_rank.setOnClickListener(this);
        img_rank = mRootView.findViewById(R.id.img_conqueredcount);
        img_rank.setOnClickListener(this);

        btnNotification = mRootView.findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(this);


        return mRootView;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtName.setText(CGlobal.curUser.getUsername());
        String path = CGlobal.curUser.tu_pic;
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

        TblUser user = new TblUser();
        user.tu_id = CGlobal.curUser.tu_id;
        user.action = "userinfo";
        progressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> call = ApiClient.getApiClient().onUserInfo(BaseModel.getQueryMap(user));
        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                //subRoot.setVisibility(View.VISIBLE);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(200, response.body()), 1000);

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
        Object object = v.getTag();
        boolean processed = false;
        if (object != null) {
            if (object instanceof TblBaseAch) {
                TblBaseAch tblBaseAch = (TblBaseAch) object;
                CGlobal.showBallon(tblBaseAch.tba_name, v, getActivity());
                processed = true;
            }
        }
        if (processed == false) {
            try {
                switch (id) {
                    case R.id.btnNotification: {
                        NotificationFragment notificationFragment = new NotificationFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("tu_id", CGlobal.curUser.tu_id);
                        bundle.putString("tabIndex", Constants.BOTTOM_TAB_PROFILE);
                        notificationFragment.setArguments(bundle);
                        replaceFragment(notificationFragment);
                        break;
                    }
                    case R.id.btnLogout: {
                        ((MainActivity) getActivity()).logout();
                        break;
                    }
                    case R.id.img_current_icon: {
                        if (bidResponse != null)
                            CGlobal.showBallon(bidResponse.info_rank.tbr_title, v, getActivity());
                        break;
                    }
                    case R.id.img_next_icon: {
                    /*if(tooltip_img_next_icon){
                        return;
                    }
                    tooltip_img_next_icon = true;
                    showBallon(id,"Next");*/
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


}
