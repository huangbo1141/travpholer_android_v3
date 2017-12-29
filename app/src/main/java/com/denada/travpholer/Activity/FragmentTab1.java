package com.denada.travpholer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.Fragment.BaseFragment;
import com.denada.travpholer.Fragment.ExploreFragment;
import com.denada.travpholer.Fragment.FreshFragment;
import com.denada.travpholer.Fragment.HotFragment;
import com.denada.travpholer.Fragment.MineFragment;
import com.denada.travpholer.Fragment.OtherPhotosFragment;
import com.denada.travpholer.Fragment.ProfileFragment;
import com.denada.travpholer.Fragment.UploadFragment;
import com.denada.travpholer.R;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblReportAbuse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by hgc on 7/30/2016.
 */
public class FragmentTab1 extends Fragment implements View.OnClickListener,MainActivity.PushFragment{

    private View mRootView;
    ProgressBar progressBar;

    Stack<String> stack = new Stack<>();

    public Class<?> getClss() {
        return clss;
    }

    public void setClss(Class<?> clss) {
        this.clss = clss;
    }

    private Class<?> clss = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_tabcontent, container, false);

        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        return  mRootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseFragment baseFragment = getChildFragment(0);
        if (baseFragment!=null)
            onPushFragment(baseFragment);
        else{
            Log.d("FragmentTab","error");
        }
    }

    BaseFragment getChildFragment(int index){
        BaseFragment fragment = null;
        switch (index){
            case 0:{
                if (clss!=null){
                    fragment = (BaseFragment) Fragment.instantiate(getActivity(),clss.getName(), null);
                }
                break;
            }
        }
        return  fragment;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

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

    @Override
    public void onPushFragment(BaseFragment fragment) {
        String classname = fragment.getClass().getName();
        String backStateName = fragment.getMyToken(classname);
        if (fragment instanceof OtherPhotosFragment){

        }else{
            backStateName = classname;
        }

        String tab = clss.getName();
        Log.d("onPush "+tab,classname);

        FragmentManager manager = getChildFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getCurrentFragment();
        if (fragment!=null){
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragmentList =  fragmentManager.getFragments();
        try{
            if (fragmentList!=null && fragmentList.size()>0){
                Fragment fragment=null;
                if (clss.equals(UploadFragment.class)){
                    fragment = fragmentList.get(0);
                }else{
                    fragment = fragmentList.get(fragmentList.size()-1);
                }
                return fragment;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }
}
