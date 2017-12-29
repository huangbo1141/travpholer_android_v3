package com.denada.travpholer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.denada.travpholer.Doc.CGlobal;

/**
 * Created by hgc on 5/17/2016.
 */
public class MyGridView extends LinearLayout{
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int itemheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 60, CGlobal.displayMetrics);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()+itemheight); //Snap to width
    }
}
