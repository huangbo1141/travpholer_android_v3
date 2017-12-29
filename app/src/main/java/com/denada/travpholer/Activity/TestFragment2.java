package com.denada.travpholer.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.Fragment.FreshFragment;
import com.denada.travpholer.Fragment.HotFragment;
import com.denada.travpholer.Fragment.NotificationFragment;
import com.denada.travpholer.Fragment.OtherPhotosFragment;
import com.denada.travpholer.R;

/**
 * Created by hgc on 7/30/2016.
 */
public class TestFragment2 extends Fragment implements View.OnClickListener{

    private View mRootView;
    View btnExplore,btnProfile,btnOthers;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_test, container, false);

        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        btnExplore = mRootView.findViewById(R.id.btnExplore);
        btnProfile =  mRootView.findViewById(R.id.btnProfile);
        btnOthers =  mRootView.findViewById(R.id.btnOthers);

        btnExplore.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnOthers.setOnClickListener(this);
        return  mRootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Fragment getChildFragment(int index){
        Fragment fragment = null;
        switch (index){
            case 0:{
                NotificationFragment notificationFragment = new NotificationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tu_id", CGlobal.curUser.tu_id);
                bundle.putString("tabIndex", Constants.BOTTOM_TAB_PROFILE);
                notificationFragment.setArguments(bundle);

                fragment = notificationFragment;
                break;
            }
            case 1:{
                fragment = new FreshFragment();
                break;
            }
            case 2:{
                OtherPhotosFragment otherPhotosFragment = new OtherPhotosFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tu_id", CGlobal.curUser.tu_id);
                bundle.putInt("mode", 0);
                //bundle.putString("tp_countryid", "");
                bundle.putString("tabIndex", Constants.BOTTOM_TAB_MINE);
                //bundle.putString("tp_ids",ids);
                otherPhotosFragment.setArguments(bundle);
                fragment = otherPhotosFragment;
                break;
            }
        }
        return  fragment;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnExplore:{

                replaceFragment(getChildFragment(0));
                break;
            }
            case R.id.btnProfile:{
                replaceFragment(getChildFragment(1));
                break;
            }
            case R.id.btnOthers:{
                replaceFragment(getChildFragment(2));
                break;
            }
        }
    }

    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getChildFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
    public boolean onBackPressed() {
        FragmentManager fm = getChildFragmentManager();
        if (fm.getBackStackEntryCount() == 1) {
            return false;
        } else {
            fm.popBackStack();
            return true;
        }
    }
}
