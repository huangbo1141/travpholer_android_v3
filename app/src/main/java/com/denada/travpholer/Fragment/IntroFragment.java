package com.denada.travpholer.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.denada.travpholer.Activity.IntroActivity;
import com.denada.travpholer.Activity.LoginActivity;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.model.SearchTerm;

/**
 * Created by hgc on 6/10/2016.
 */
public class IntroFragment extends Fragment implements View.OnClickListener{

    TextView topTitle;
    ImageView img_toolbar_back;

    public IntroActivity introActivity;
    int step = 1;
    ImageView img_content,img_bottom;
    View view_bottom,btn_next,btn_finish;

    public static IntroFragment getInstance(int param){
        Bundle bundle = new Bundle();
        bundle.putInt("step",param);
        IntroFragment introFragment = new IntroFragment();
        introFragment.setArguments(bundle);

        return introFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        try{
            step = bundle.getInt("step",1);
        }catch (Exception ex){

        }
    }

    View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.activity_intro_fragment, container, false);
        img_content = (ImageView) mRootView.findViewById(R.id.img_content);
        img_bottom = (ImageView) mRootView.findViewById(R.id.img_bottom);
        view_bottom = mRootView.findViewById(R.id.view_bottom);
        btn_next = mRootView.findViewById(R.id.btn_next);
        btn_finish = mRootView.findViewById(R.id.btn_finish);

        btn_finish.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        img_bottom.setVisibility(View.GONE);
        view_bottom.setVisibility(View.GONE);
        btn_finish.setVisibility(View.GONE);

        setStep();
        return mRootView;
    }

    void setStep(){
        switch (step){
            case 1:{
                img_content.setImageResource(R.drawable.phone_intro1);
                break;
            }
            case 2:{
                img_content.setImageResource(R.drawable.phone_intro2);
                break;
            }
            case 3:{
                img_content.setImageResource(R.drawable.phone_intro3);
                btn_next.setVisibility(View.GONE);

                view_bottom.setVisibility(View.VISIBLE);
                img_bottom.setVisibility(View.VISIBLE);
                btn_finish.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_finish: {
                if (introActivity!=null){
                    introActivity.goFinish(step);
                }

                break;
            }
            case R.id.btn_next:{
                if (introActivity!=null){
                    introActivity.goNext(step);
                }
                break;
            }
        }
    }
}
