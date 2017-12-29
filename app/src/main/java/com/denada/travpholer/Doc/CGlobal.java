package com.denada.travpholer.Doc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.media.ExifInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.denada.travpholer.Activity.MainActivity;
import com.denada.travpholer.Activity.TestActivity;
import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.denada.travpholer.R;
import com.denada.travpholer.model.AddressData;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.TblBaseAch;
import com.denada.travpholer.model.TblBaseRank;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.model.TblLikes;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.model.TblUser;
import com.denada.travpholer.model.Response.LoginResponse;
import com.denada.travpholer.model.SearchTerm;
import com.denada.travpholer.util.database.ImageDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by hgc on 6/26/2015.
 */
public class CGlobal {

    public static void resetData(){
        photoFile = null;
        cameraPosition_conq= null;
        cameraPosition_togo = null;
        addressData = null;
    }
    public static GoogleApiClient googleApiClient = null;

    public static File photoFile;
    public static CameraPosition cameraPosition_togo;
    public static CameraPosition cameraPosition_conq;

    public static CallbackManager g_callbackManager;
    public static DisplayMetrics displayMetrics;
    public static Display display;
    public static ImageDatabaseHelper dbManager;
    public static ArrayList<Object> listCountry = new ArrayList<>();
    public static ArrayList<Object> listState = new ArrayList<>();
    public static ArrayList<Object> listCity = new ArrayList<>();

    public static ArrayList<TblComment> tempComments = new ArrayList<>();
    public static TblPhotos tempPhoto;
    public static Country currentCountry;
//    public static Address address;
//    public static LatLng mPos;

    public static AddressData addressData;

//    public static ArrayList<Object> hotCountry  = new ArrayList<>();
//    public static ArrayList<Object> freshCountry  = new ArrayList<>();
    public static ArrayList<Object> tempCountry  = new ArrayList<>();
    public static List<TblBaseAch> rows_base_ach;
    public static List<TblBaseRank> rows_base_rank;

    public  static void initGlobal(Activity activity){
        CGlobal.displayMetrics  =   activity.getResources().getDisplayMetrics();
        CGlobal.display           = activity.getWindowManager().getDefaultDisplay();
        CGlobal.dbManager =  new ImageDatabaseHelper(activity);

        listCountry = dbManager.getCountries();
    }

    public  static  LatLng defaultPos = new LatLng(47.0,2.0);
    public static final int maxBufferSize           = 6000 ;
    public static Bitmap photo;

    public static TblUser curUser;
    public static LoginResponse loginResponse;


    public static SearchTerm searchTerm;
    public static TblLikes curProject;
    public static TblUser selectUser;

    public static void gotoMainWindow(Activity activity,LoginResponse loginResponse){
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit();

        prefEditor.putInt(Constants.KEY_HASLOGIN, 1);
        prefEditor.putString("tu_username", loginResponse.row.tu_username);
        prefEditor.putString("tu_password", loginResponse.row.tu_password);
        prefEditor.putString("tu_type", loginResponse.row.tu_type);
        prefEditor.commit();

        CGlobal.curUser = loginResponse.row;
        CGlobal.loginResponse = loginResponse;
        CGlobal.rows_base_ach = loginResponse.rows_base_ach;
        CGlobal.rows_base_rank = loginResponse.rows_base_rank;

        Intent intent = new Intent(activity,MainActivity.class);
        activity.startActivity(intent);
        activity.finish();

    }
    public static String getStringFromNumberForCurrency(String number){
        String ret;
        DecimalFormat df = new DecimalFormat("#,##0");
        ret = df.format(Integer.valueOf(number));

        return ret;
    }
    public static Drawable getDrawableFromName(String prefix,String icon,Context context){

        Resources res = context.getResources();
        int id = res.getIdentifier(prefix + icon, "drawable", context.getPackageName());
        if (id == 0) {
            return res.getDrawable(0);      /// error
        } else
            return res.getDrawable(id);
    }
    public static String getBaseIconPath(String prefix,String icon,Context context){

        String ret = "";
        ret = Constants.url + "assets/base/"+ prefix + icon +".png";

        return ret;
    }

    public static String getNumberFromStringForCurrency(String string){
        String ret;
        ret = string.replace(",", "");
        return ret;
    }

    public static String getAgoTime(String string){
        String ret = "";
        SimpleDateFormat dateFormatUCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatUCT.setTimeZone(TimeZone.getTimeZone("gmt"));
        try {
            Date deadlineDate = dateFormatUCT.parse(string);
            Date currentDate = new Date();
            if(currentDate.before(deadlineDate)) {
                return "Just Now";
            }

            long timeDiff = currentDate.getTime()-deadlineDate.getTime();
            int hour = (int) (timeDiff/(60*60*1000));
            timeDiff = timeDiff % (60*60*1000);
            int min = (int) (timeDiff/(60*1000));
            timeDiff = timeDiff % (60*1000);
            int sec = (int) (timeDiff/1000);

            if (hour == 0 && min == 0){
                return "Just Now";
            }
            ret = String.format("%d hours %d minutes ago",hour,min);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static int ageFromBirthday(String birthday){
        Date curDate = new Date();
        SimpleDateFormat dateFormatUCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curDate_str = dateFormatUCT.format(curDate);

        int age = Integer.valueOf(curDate_str.substring(0,4)) - Integer.valueOf(birthday.substring(0,4));

        return age;
    }

    public static String getGmtTime(Calendar calendar){
        SimpleDateFormat dateFormatUCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatUCT.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = dateFormatUCT.format(calendar.getTime());

        return gmtTime;
    }

    public static String checkTimeForCreateBid(String string){
        String ret = null;
        SimpleDateFormat dateFormatUCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatUCT.setTimeZone(TimeZone.getTimeZone("gmt"));
        try {
            Date suggestTime = dateFormatUCT.parse(string);
            Date currentDate = new Date();

            long timeDiff = currentDate.getTime() - suggestTime.getTime();
            int hour = (int) (timeDiff/(60*60*1000));
            timeDiff = timeDiff % (60*60*1000);
            int min = (int) (timeDiff/(60*1000));
            timeDiff = timeDiff % (60*1000);
            int sec = (int) (timeDiff/1000);

            if (hour<24){
                long time1 = 24*60*60*1000;
                timeDiff = time1 - (currentDate.getTime() - suggestTime.getTime());
                hour = (int) (timeDiff/(60*60*1000));
                timeDiff = timeDiff % (60*60*1000);
                min = (int) (timeDiff/(60*1000));
                timeDiff = timeDiff % (60*1000);
                sec = (int) (timeDiff/1000);
                ret = String.format("%02d:%02d:%02d",hour,min,sec);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getPrettyBirthday(String param){
        String ret = "";
        try{
            SimpleDateFormat dateFormatUCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormatUCT.parse(param);
//            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
            ret = sdf.format(date);
        }catch (Exception ex){

        }
        return ret;
    }

    public static ArrayList<Object> getYearArray(){
        ArrayList<Object> array = new ArrayList<>();
        int lastyear = 1998;
        for(int i=lastyear; i>=1900; i--){
            String year = String.valueOf(i);
            array.add(year);
        }
        return array;
    }
    public static ArrayList<Object> getMonthArray(){
        ArrayList<Object> array = new ArrayList<>();
        for(int i=1; i<=12; i++){
            String year = String.format("%02d", i);
            array.add(year);
        }
        return array;
    }
    public static ArrayList<Object> getDayArray(String year,String month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.valueOf(year));
        calendar.set(Calendar.MONTH, Integer.valueOf(month)-1);
        int limit = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        ArrayList<Object> array = new ArrayList<>();
        for(int i=1; i<=limit; i++){
            String tmp = String.format("%02d",i);
            array.add(tmp);
        }
        return array;
    }
    public static String getThumbPhotoPath(String filename){
        String ret = "";
        ret = Constants.url + "assets/uploads/thumbnail/"+ filename;

        return ret;
    }
    public static String getPhotoPath(String filename){
        String ret = "";
        ret = Constants.url + "assets/uploads/"+ filename;

        return ret;
    }
    public static  List<TblPhotos> removeDuplicates(List<TblPhotos> source,List<TblPhotos> compdata){
        HashMap<String,Integer> hashMap = new HashMap<>();
        for (int i=0; i<compdata.size(); i++){
            TblPhotos item = compdata.get(i);
            hashMap.put(item.tp_id,i);
        }
        List<TblPhotos> temp_data = source;
        for (int i=0; i<source.size(); i++){
            TblPhotos item = source.get(i);
            if (hashMap.containsKey(item.tp_id)){
                temp_data.remove(item);
            }
        }
        return temp_data;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }
    public static Bitmap getMarkerBitmap1(int number,Context context,Bitmap bm){

        String gText = String.valueOf(number);
        int spellsize = (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 14, displayMetrics));
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float scale = resources.getDisplayMetrics().density;
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(61, 61, 61));
        paint.setTextSize(spellsize);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);

        int bw = bounds.width();
        int bh = bounds.height();

        int radius;
        if(bw<bh)
            radius = bh/2;
        else
            radius = bw/2;
        radius +=5;

        int sm,cw,tw,th;
        sm = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 1, displayMetrics);;
        cw = radius;
        tw = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 60, displayMetrics);
        th = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 60, displayMetrics);
        int dw = tw-2*sm-cw;
        int dh = tw-2*sm-cw;
        int thumb_width = bm.getWidth();
        int thumb_height = bm.getHeight();
        int realwidth,realheight;
        if (thumb_width>thumb_height){
            realwidth = dw;
            realheight = (int) (((float)realwidth/(float)thumb_width)*thumb_height);
        }else{
            realheight = dh;
            realwidth =  (int) (((float)realheight/(float)thumb_height)*thumb_width);
        }
        Bitmap bitmap = Bitmap.createBitmap(tw, th, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawRect(new Rect(0,cw,sm*2+dw,th),paint);
        int cx = (dw-realwidth)/2+sm,cy = (dh-realheight)/2+sm+cw;
        Rect dst = new Rect(cx,cy,realwidth,realheight);
        canvas.drawBitmap(bm,new Rect(0,0,bm.getWidth(),bm.getHeight()),dst,null);

        if (number>1){
            RectF circle_rect = new RectF(tw-cw*2, 0, 2*cw, 2*cw);
            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(255, (int) (0.29 * 256), (int) (0.60 * 256), (int) (0.85 * 256));
            canvas.drawCircle(cx, cy, radius, paint);
            canvas.drawOval(circle_rect, paint);

            Paint plantxtpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            plantxtpaint.setColor(Color.rgb(255, 255, 255));
            paint.setTextSize(spellsize);

            Rect text_rect = new Rect((int) ( (circle_rect.width()-bw)/2+circle_rect.left) ,(int) ((circle_rect.height()-bh)/2+circle_rect.top),bw,bh);
//            canvas.drawText(gText, cx - bw / 4, cy + bh / 4, plantxtpaint);
            canvas.drawText(gText, text_rect.left, text_rect.top, plantxtpaint);
        }

        return bitmap;
    }
    public static Bitmap getMarkerBitmap(String gText,Context context){
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(61, 61, 61));
        paint.setTextSize((int) (14 * scale));

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);

        int bw = bounds.width();
        int bh = bounds.height();

        int radius;
        if(bw<bh)
            radius = bh/2;
        else
            radius = bw/2;
        radius +=5;
        int cx = radius;
        int cy = radius;
        int w = 2*radius;
        int h = 2*radius;

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Random rnd = new Random();

        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        canvas.drawCircle(cx, cy, radius, paint);

        Paint plantxtpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        plantxtpaint.setColor(Color.rgb(255, 255, 255));
        paint.setTextSize((int) (14 * scale));

        canvas.drawText(gText, cx - bw / 4, cy + bh / 4, plantxtpaint);

        return bitmap;
    }

    public static  String getLastFileName(String param1){
        int index = param1.lastIndexOf("/");
        String last = param1.substring(index+1);
        return last;
    }
    public static void showBallon(String string,View sourceView, final Activity activity){

        if (sourceView!=null){
            LayoutInflater inflater = (LayoutInflater)
                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_example, null);
            TextView textView = (TextView) popupView.findViewById(R.id.textView);
            textView.setText(string);
            final PopupWindow pw = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Rect location = CGlobal.locateView(sourceView);

            Resources resources = activity.getResources();
            float scale = resources.getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            // paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setColor(Color.rgb(61, 61, 61));
            paint.setTextSize((int) (12 * scale));

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(string, 0, string.length(), bounds);

            int width = bounds.width();
            int width12dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 12, CGlobal.displayMetrics);
            width12dp = (int)(12*scale);
            width = width + width12dp;
            int width1 = location.right - location.left;
            int offset = (width - width1)/2;
//        pw.showAtLocation(imageview, Gravity.TOP|Gravity.CENTER_HORIZONTAL, location.left, location.bottom);
            pw.showAsDropDown(sourceView,-1*offset,10);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try{
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pw.dismiss();
                            }
                        });
                    }catch (Exception ex){

                    }

                }
            }).start();
        }
    }
}
