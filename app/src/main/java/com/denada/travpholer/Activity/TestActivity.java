/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.denada.travpholer.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.Fragment.ExploreFragment;
import com.denada.travpholer.Fragment.OtherPhotosFragment;
import com.denada.travpholer.Fragment.ProfileFragment;
import com.denada.travpholer.R;

import java.util.ArrayList;


public class TestActivity extends AppCompatActivity implements  View.OnClickListener{

    int seconds = 100;
    ProgressBar progressBar;

    View btnExplore,btnProfile,btnOthers;
    ExploreFragment exploreFragment;
    ProfileFragment profileFragment;
    OtherPhotosFragment otherPhotosFragment;
    private boolean mAttached;

    Fragment[] fragments = new Fragment[3];

    private int currenttab = -1;
//    private List<TabHost.TabSpec> mTabSpecs = new ArrayList<TabHost.TabSpec>(2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        CGlobal.initGlobal(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        btnExplore = findViewById(R.id.btnExplore);
        btnProfile = findViewById(R.id.btnProfile);
        btnOthers = findViewById(R.id.btnOthers);

        btnExplore.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnOthers.setOnClickListener(this);


//        exploreFragment = new ExploreFragment();
//        profileFragment = new ProfileFragment();
//        otherPhotosFragment = new OtherPhotosFragment();

        Bundle bundle = new Bundle();
        bundle.putString("tu_id", CGlobal.curUser.tu_id);
        bundle.putInt("mode", 0);
        //bundle.putString("tp_countryid", "");
        bundle.putString("tabIndex", Constants.BOTTOM_TAB_MINE);
        //bundle.putString("tp_ids",ids);
//        otherPhotosFragment.setArguments(bundle);

        mFragmentManager = getSupportFragmentManager();
        mContext = this;

        addTab(TestFragment1.class,null,"first");
        addTab(TestFragment2.class, null, "second");
        addTab(TestFragment3.class, bundle, "third");

        mContainerId = R.id.content;

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.btnExplore:{
                if (mAttached) {
                    FragmentTransaction ft = doTabChanged("first", null);
                    if (ft != null) {
                        ft.commit();
                    }
                }
                mCurrentTab = 0;
                break;
            }
            case R.id.btnProfile:{
                if (mAttached) {
                    FragmentTransaction ft = doTabChanged("second", null);
                    if (ft != null) {
                        ft.commit();
                    }
                }
                mCurrentTab = 1;
                break;
            }
            case R.id.btnOthers:{
//                if (mAttached) {
//                    FragmentTransaction ft = doTabChanged("third", null);
//                    if (ft != null) {
//                        ft.commit();
//                    }
//                }
                Intent intent = new Intent(this,CommentActivity.class);

                startActivity(intent);

                mCurrentTab = 2;
                break;
            }
        }
    }

    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private TabInfo mLastTab;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private Context mContext;

    protected int mCurrentTab = 0;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        String currentTab = getCurrentTabTag();

        // Go through all tabs and make sure their fragments match
        // the correct state.
        FragmentTransaction ft = null;
        for (int i=0; i<mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.fragment != null && !tab.fragment.isDetached()) {
                if (tab.tag.equals(currentTab)) {
                    // The fragment for this tab is already there and
                    // active, and it is what we really want to have
                    // as the current tab.  Nothing to do.
                    mLastTab = tab;
                } else {
                    // This fragment was restored in the active state,
                    // but is not the current tab.  Deactivate it.
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.detach(tab.fragment);
                }
            }
        }

        // We are now ready to go.  Make sure we are switched to the
        // correct tab.
        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commit();
            mFragmentManager.executePendingTransactions();
        }
    }
    public String getCurrentTabTag() {
        if (mCurrentTab >= 0 && mCurrentTab < mTabs.size()) {
            return mTabs.get(mCurrentTab).getTag();
        }
        return null;
    }
    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void addTab(Class<?> clss, Bundle args,String tag) {

        TabInfo info = new TabInfo(tag, clss, args);

        if (mAttached) {
            // If we are already attached to the window, then check to make
            // sure this tab's fragment is inactive if it exists.  This shouldn't
            // normally happen.
            info.fragment = mFragmentManager.findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }
        }

        mTabs.add(info);
    }
    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i=0; i<mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mContext,
                            newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }

            mLastTab = newTab;
        }
        return ft;
    }

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }

        public String getTag(){
            return tag;
        }
    }

//    private void replaceFragment (Fragment fragment,int pos){
//
//        String backStateName = fragment.getClass().getName();
//        Fragment newFragment = null;
//        for (int i=0; i<tabs.size(); i++){
//            if (i == pos){
//                newFragment = tabs.get(i);
//            }
//        }
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
//
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTabHost
//        FragmentTransaction tr = manager.beginTransaction();
//
//
//        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
//
//        if (!fragmentPopped){ //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content, fragment);
//            ft.addToBackStack(backStateName);
//            ft.commit();
//        }
//    }
//    private void replaceFragment (Fragment fragment){
//        String backStateName = fragment.getClass().getName();
//
//        FragmentManager manager = getSupportFragmentManager();
//        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
//
//        if (!fragmentPopped){ //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content, fragment);
//            ft.addToBackStack(backStateName);
//            ft.commit();
//        }
//    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager()
                .findFragmentByTag(getCurrentTabTag());
        if (f != null && f instanceof TestFragment1) {
            TestFragment1 tabChild = (TestFragment1) f;
            if (tabChild.onBackPressed()) {
                return;
            }
        }else if (f != null && f instanceof TestFragment2) {
            TestFragment2 tabChild = (TestFragment2) f;
            if (tabChild.onBackPressed()) {
                return;
            }
        }else if (f != null && f instanceof TestFragment3) {
            TestFragment3 tabChild = (TestFragment3) f;
            if (tabChild.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }
}