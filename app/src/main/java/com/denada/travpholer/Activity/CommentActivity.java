package com.denada.travpholer.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denada.travpholer.Fragment.CommentFragment;
import com.denada.travpholer.R;
import com.denada.travpholer.model.TblPhotos;

/**
 * Created by hgc on 6/10/2016.
 */
public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    TextView topTitle;
    ImageView img_toolbar_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentcontainer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar);
        topTitle = (TextView) findViewById(R.id.mytext);
        img_toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        topTitle.setText("Comment");

        img_toolbar_back.setImageResource(R.drawable.ico_back);
        img_toolbar_back.setOnClickListener(this);

        TblPhotos photos = null;
        try{
            photos = (TblPhotos) getIntent().getSerializableExtra("data");
            if (photos == null)
                finish();

        }catch (Exception ex){
            finish();
        }

        Fragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",photos);
        fragment.setArguments(bundle);
        FragmentManager manager         =   getSupportFragmentManager();
        FragmentTransaction ft            =   manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.realtabcontent, fragment);
        ft.commit();
    }

    public  void setTopTitle(String string,int toolbar_back){
        if (string!=null&& !string.isEmpty())
            topTitle.setText(string);
        if (toolbar_back>0){
            img_toolbar_back.setImageResource(toolbar_back);
            img_toolbar_back.setVisibility(View.VISIBLE);
        }else{
            img_toolbar_back.setImageResource(android.R.color.transparent);
            img_toolbar_back.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        switch (id) {
            case R.id.toolbar_back: {
                onBackPressed();
                break;
            }
        }
    }
}
