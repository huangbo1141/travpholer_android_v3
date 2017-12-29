package com.denada.travpholer.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.denada.travpholer.Activity.MainActivity;


/**
 * Created by Hgc on 6/2/2015.
 */
public class BaseFragment extends Fragment {
    protected MainActivity mAct;

    protected Long timeStamp;

    public boolean onBackPressed() {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (timeStamp == null)
            timeStamp = System.currentTimeMillis();


        try {
            mAct = (MainActivity) getActivity();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected void setCaption(String string, int toolbar_back) {
        mAct.setTopTitle(string, toolbar_back);
    }

    public String getMyToken(String prefix) {
        if (timeStamp == null)
            timeStamp = System.currentTimeMillis();
        String ts = prefix + timeStamp.toString();
        return ts;
    }

    protected void replaceFragment(BaseFragment fragment) {

        Fragment fragment1 = this.getParentFragment();
        if (fragment1 instanceof MainActivity.PushFragment) {
            MainActivity.PushFragment pushFragment = (MainActivity.PushFragment) fragment1;
            pushFragment.onPushFragment(fragment);
        } else {
            Fragment fragment2 = fragment1.getParentFragment();
            if (fragment2 instanceof MainActivity.PushFragment) {
                MainActivity.PushFragment pushFragment = (MainActivity.PushFragment) fragment2;
                pushFragment.onPushFragment(fragment);
            }
        }

    }
}
