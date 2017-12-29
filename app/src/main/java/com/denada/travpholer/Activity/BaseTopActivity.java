package com.denada.travpholer.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denada.travpholer.Doc.AppFonts;
import com.denada.travpholer.R;

/**
 * Created by hgc on 6/26/2015.
 */
public class BaseTopActivity extends AppCompatActivity {

    protected ImageView imgHome;
    protected ImageView imgLogo;
    protected TextView topTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("restored", 1);
    }
    private void initFonts() {
        Typeface openSans = Typeface.createFromAsset(getAssets(), AppFonts.OPENSANS_SEMIBOLD);
        topTitle .setTypeface(openSans);
    }

    protected void setBackImage(){
        imgHome.setImageResource(R.mipmap.ic_action_back);
    }
}
