package com.denada.travpholer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.Fragment.FreshFragment;
import com.denada.travpholer.Fragment.HotFragment;
import com.denada.travpholer.Fragment.IntroFragment;
import com.denada.travpholer.R;

import java.util.List;
import java.util.Vector;

/**
 * Created by hgc on 6/10/2016.
 */
public class IntroActivity extends AppCompatActivity{

    TextView topTitle;
    ImageView img_toolbar_back;

    int step = 1;
    ImageView img_content,img_bottom;
    View view_bottom,btn_next,btn_finish;

    ViewPager pager;
    PagerAdapter mPagerAdapter;
    private List<Fragment> fragments;
    private static Fragment[] fragseries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        pager = (ViewPager) findViewById(R.id.viewpager);
        fragseries = new Fragment[3];
        IntroFragment introFragment1 = IntroFragment.getInstance(1);
        IntroFragment introFragment2 = IntroFragment.getInstance(2);
        IntroFragment introFragment3 = IntroFragment.getInstance(3);

        introFragment1.introActivity = this;
        introFragment2.introActivity = this;
        introFragment3.introActivity = this;

        fragseries[0] = introFragment1;
        fragseries[1] = introFragment2;
        fragseries[2] = introFragment3;

        fragments = new Vector<Fragment>();
        fragments.add(fragseries[0]);
        fragments.add(fragseries[1]);
        fragments.add(fragseries[2]);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(mPagerAdapter);

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

    public void goNext(int prevstep){
        int step = prevstep + 1;
        pager.setCurrentItem(step-1,true);
    }

    public void goFinish(int prevstep){
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        prefEditor.putInt(Constants.KEY_INTROVIEWED, 1);
        prefEditor.commit();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
