package com.denada.travpholer.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.denada.travpholer.R;

import java.util.List;
import java.util.Vector;

public class MineFragment extends BaseFragment implements View.OnClickListener {


    private static Fragment[] fragseries;
    PagerSlidingTabStrip mTabs;
    private View mRootView;
    private PagerAdapter mPagerAdapter;
    private List<Fragment> fragments;
    private String mCountryId = null;
    private ViewPager pager;
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        private Integer curPage = 0;
        private Integer lastPageIndex = -1;
        private Integer selcttime = 0;
        private String[] titles = {"ToGo", "Conquered"};

        @Override
        public void onPageScrolled(int i, float v, int i2) {
            //Log.e("onPageScrolled ",String.valueOf(i));
        }

        @Override
        public void onPageSelected(int i) {
            lastPageIndex = i;
            setCaption(titles[i], -1);
            Log.e("onPageSelected ", String.valueOf(i));

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);

        pager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        mTabs = (PagerSlidingTabStrip) mRootView.findViewById(R.id.tabs);
        setCaption("Maps", -1);

        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCountryId = this.getArguments().getString("tp_countryid");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragseries = new Fragment[2];
        fragseries[0] = new MapTogoFragment();
        fragseries[1] = new MapConqueredFragment();

        fragments = new Vector<Fragment>();
        fragments.add(fragseries[0]);
        fragments.add(fragseries[1]);
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        pager.setAdapter(mPagerAdapter);

        mTabs.setViewPager(pager);
//        pager.setOnPageChangeListener(onPageChangeListener);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = {"Bucket-List", "Uploaded"};
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
}
