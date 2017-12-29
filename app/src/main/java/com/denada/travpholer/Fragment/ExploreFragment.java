package com.denada.travpholer.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.R;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.Response.HotResponse;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblReportAbuse;
import com.denada.travpholer.view.nice.NiceSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit.RetrofitError;

public class ExploreFragment extends BaseFragment implements View.OnClickListener,PagerSlidingTabStrip.TabTapListener {

    PagerSlidingTabStrip pagerSlidingTabStrip;

    private View mRootView;
    private PagerAdapter mPagerAdapter;
    private List<Fragment> fragments;
    private static Fragment[] fragseries;
    PagerSlidingTabStrip mTabs;
    public NiceSpinner spinner1,spinner2;

    private HotFragment hotFragment;
    private FreshFragment freshFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_explore, container, false);

        pager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        mTabs = (PagerSlidingTabStrip) mRootView.findViewById(R.id.tabs);
        setCaption("Explore", -1);

        spinner1 = (NiceSpinner) mRootView.findViewById(R.id.spinner1);
        List<Object> data = new ArrayList<>();
        data.add(new String("All Countries"));
        spinner1.attachDataSource(data);
        spinner1.setVisibility(View.VISIBLE);

        spinner2 = (NiceSpinner) mRootView.findViewById(R.id.spinner2);
        List<Object> data2 = new ArrayList<>();
        data2.add(new String("All Countries"));
        spinner2.attachDataSource(data);
        spinner2.setVisibility(View.GONE);

        pagerSlidingTabStrip = (PagerSlidingTabStrip) mRootView.findViewById(R.id.tabs);
        pagerSlidingTabStrip.setTabTapListener(this);

        return mRootView;
    }


    private String mCountryId = null;
    String token;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            mCountryId  = this.getArguments().getString("tp_countryid");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        hotFragment = new HotFragment();
        freshFragment = new FreshFragment();
        fragseries = new Fragment[2];
        fragseries[0] = hotFragment;
        fragseries[1] = freshFragment;
        ((HotFragment)fragseries[0]).parentFragment = this;

        ((FreshFragment)fragseries[1]).parentFragment = this;

        fragments = new Vector<Fragment>();
        fragments.add(fragseries[0]);
        fragments.add(fragseries[1]);

        String classname = getClass().getName();
        token = getMyToken(classname);
        Log.d("Explore Create ",token);
    }

    private ViewPager pager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        pager.setAdapter(mPagerAdapter);

        mTabs.setViewPager(pager);
        mTabs.setOnPageChangeListener(onPageChangeListener);
//        pager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void onLongClick(int position) {
        try{
            if (position == 0){
                ((HotFragment)fragseries[0]).loadMore();
            }else{
                ((FreshFragment)fragseries[1]).loadMore();
            }

        }catch (Exception ex){

        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = {"Hot","Fresh"};
        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            // TODO Auto-generated constructor stub
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return this.fragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return this.fragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        private Integer curPage = 0;
        private Integer lastPageIndex = -1;
        private Integer selcttime = 0;
        private String[] titles = {"Hot","Fresh"};
        @Override
        public void onPageScrolled(int i, float v, int i2) {
            //Log.e("onPageScrolled ",String.valueOf(i));
        }

        @Override
        public void onPageSelected(int i) {
            lastPageIndex = i;
            //setCaption(titles[i],-1);
            //Log.e("onPageSelected ", String.valueOf(i));
            switch (i){
                case 0:{
                    spinner1.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.GONE);
                    break;
                }
                case 1:{
                    spinner1.setVisibility(View.GONE);
                    spinner2.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
