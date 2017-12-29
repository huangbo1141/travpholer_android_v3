package com.denada.travpholer.model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ListView;

import com.denada.travpholer.Doc.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hgc on 8/1/2016.
 */
public class PhotoManager {

    private ArrayList<TblPhotos> photosarrayHot = new ArrayList<>();
    private ArrayList<TblPhotos> photosarrayFresh = new ArrayList<>();

    Object concurrentHot;
    Object concurrentFresh;
    private static final String concurrentInstance = "concurrentInstance";

    private static PhotoManager photoManager;

    Activity mActivity = null;
    Context context = null;
    public static PhotoManager getInstance(Activity context){
        synchronized (concurrentInstance){
            if (photoManager == null){
                photoManager = new PhotoManager(context);
            }
            return photoManager;
        }

    }
    PhotoManager(Activity context){
        this.context = context;
        this.mActivity = context;
        registerReceiver();
    }
    public void dealloc(){
        try{
            unregisterReceiver();

        }catch (Exception ex){

        }
        photoManager = null;
        mActivity = null;
        context = null;
    }

    public void registerReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_PHOTOCHANGED);
        this.mActivity.registerReceiver(mBroadCastReceiver,filter);
    }
    public void unregisterReceiver(){
        this.mActivity.unregisterReceiver(mBroadCastReceiver);
    }

    public ArrayList<TblPhotos> photosHot(){
        synchronized (photosarrayHot){
            return photosarrayHot;
        }
    }

    public ArrayList<TblPhotos> photosFresh(){
        synchronized (photosarrayFresh){
            return photosarrayFresh;
        }
    }

    public void deleteAllPhotosHot(){
        synchronized (photosarrayHot){
            photosarrayHot.clear();
            postContentChangeNotificationHot();
        }
    }
    public void deletePhotosHot(TblPhotos photo){
        if (photo!=null) {
            synchronized (photosarrayHot) {
                List<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item : photosarrayHot) {
                    if (item.tp_id.equals(photo.tp_id))
                        discardedItems.add(item);
                }
                photosarrayHot.removeAll(discardedItems);
                postContentChangeNotificationHot();
            }
        }
    }
    public void addPhotoHot(TblPhotos photo){
        if (photo!=null){
            synchronized (photosarrayHot){
                List<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item : photosarrayHot){
                    if (item.tp_id.equals(photo.tp_id)){
                        discardedItems.add(item);
                    }
                }
                photosarrayHot.removeAll(discardedItems);

                photosarrayHot.add(photo);
                postContentChangeNotificationHot();
            }
        }

    }
    public void addPhotosHot(ArrayList<TblPhotos> photoArray, SearchTerm searchTerm){
        if (photoArray!=null && photoArray.size()>0){
            synchronized (photosarrayHot){
                HashMap<String,String> hashMap = new HashMap<>();
                for (TblPhotos item:photoArray){
                    hashMap.put(item.tp_id,item.tp_id);
                }

                ArrayList<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item:photosarrayHot){
                    if (hashMap.containsKey(item.tp_id)){
                        discardedItems.add(item);
                    }
                }

                photosarrayHot.removeAll(discardedItems);

                if (searchTerm!=null && searchTerm.tp_fetcharrow!=null && searchTerm.tp_fetcharrow.equals("1")){
                    ArrayList<TblPhotos> temp = new ArrayList<>();
                    temp.addAll(photoArray);
                    temp.addAll(photosarrayHot);
                    photosarrayHot = temp;
                }else{
                    photosarrayHot.addAll(photoArray);
                }

                postContentChangeNotificationHot();
            }
        }
    }

    // Fresh

    public void deleteAllPhotosFresh(){
        synchronized (photosarrayFresh){
            photosarrayFresh.clear();
            postContentChangeNotificationFresh();
        }
    }
    public void deletePhotosFresh(TblPhotos photo){
        if (photo!=null) {
            synchronized (photosarrayFresh) {
                List<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item : photosarrayFresh) {
                    if (item.tp_id.equals(photo.tp_id))
                        discardedItems.add(item);
                }
                photosarrayFresh.removeAll(discardedItems);
                postContentChangeNotificationFresh();
            }
        }
    }
    public void addPhotoFresh(TblPhotos photo){
        if (photo!=null){
            synchronized (photosarrayFresh){
                List<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item : photosarrayFresh){
                    if (item.tp_id.equals(photo.tp_id)){
                        discardedItems.add(item);
                    }
                }
                photosarrayFresh.removeAll(discardedItems);

                photosarrayFresh.add(photo);
                postContentChangeNotificationFresh();
            }
        }

    }
    public void addPhotosFresh(ArrayList<TblPhotos> photoArray, SearchTerm searchTerm){
        if (photoArray!=null && photoArray.size()>0){
            synchronized (photosarrayFresh){
                HashMap<String,String> hashMap = new HashMap<>();
                for (TblPhotos item:photoArray){
                    hashMap.put(item.tp_id,item.tp_id);
                }

                ArrayList<TblPhotos> discardedItems = new ArrayList<>();
                for (TblPhotos item:photosarrayFresh){
                    if (hashMap.containsKey(item.tp_id)){
                        discardedItems.add(item);
                    }
                }

                photosarrayFresh.removeAll(discardedItems);

                if (searchTerm!=null &&  searchTerm.tp_fetcharrow!=null && searchTerm.tp_fetcharrow.equals("1")){
                    ArrayList<TblPhotos> temp = new ArrayList<>();
                    temp.addAll(photoArray);
                    temp.addAll(photosarrayFresh);
                    photosarrayFresh = temp;
                }else{
                    photosarrayFresh.addAll(photoArray);
                }

                postContentChangeNotificationFresh();
            }
        }
    }

    public void postContentChangeNotificationHot(){
        if (context!=null){
            Intent intent = new Intent();
            intent.setAction(Constants.BROADCAST_CONTENTCHANGED_HOT);
            context.sendBroadcast(intent);
        }

    }


    public void postContentChangeNotificationFresh(){
        if (context!=null){
            Intent intent = new Intent();
            intent.setAction(Constants.BROADCAST_CONTENTCHANGED_FRESH);
            context.sendBroadcast(intent);
        }
    }

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.BROADCAST_PHOTOCHANGED)){
                int actiontype = intent.getIntExtra("actiontype",-1);
                String controller = intent.getStringExtra("controller");
                switch (actiontype){
                    case 0:{
                        try{
                            TblPhotos item = (TblPhotos)intent.getSerializableExtra("data");
                            if (item != null) {
                                synchronized (photosarrayHot){
                                    for (TblPhotos tblPhotos:photosarrayHot){
                                        if (tblPhotos.tp_id.equals(item.tp_id)){
                                            tblPhotos.ilikethis = item.ilikethis;
                                            tblPhotos.likescount = item.likescount;
                                        }
                                    }
                                    if (!controller.equals("hot")){
                                        postContentChangeNotificationHot();
                                    }
                                }

                                synchronized (photosarrayFresh){
                                    for (TblPhotos tblPhotos:photosarrayFresh){
                                        if (tblPhotos.tp_id.equals(item.tp_id)){
                                            tblPhotos.ilikethis = item.ilikethis;
                                            tblPhotos.likescount = item.likescount;
                                        }
                                    }
                                    if (!controller.equals("fresh")){
                                        postContentChangeNotificationFresh();
                                    }
                                }
                            }

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;
                    }
                    case 1:{
                        try{
                            TblReportAbuse item = (TblReportAbuse)intent.getSerializableExtra("data");
                            if (item != null) {
                                synchronized (photosarrayHot) {
                                    ArrayList<TblPhotos> discardedItems = new ArrayList<>();
                                    for (TblPhotos tblPhotos : photosarrayHot) {
                                        if (tblPhotos.tp_id.equals(item.tp_id)) {
                                            discardedItems.add(tblPhotos);
                                        }
                                    }
                                    if (discardedItems.size()>0) {
                                        photosarrayHot.removeAll(discardedItems);
                                        if (!controller.equals("hot")){
                                            postContentChangeNotificationHot();
                                        }
                                    }
                                }
                                synchronized (photosarrayFresh) {
                                    ArrayList<TblPhotos> discardedItems = new ArrayList<>();
                                    for (TblPhotos tblPhotos : photosarrayFresh) {
                                        if (tblPhotos.tp_id.equals(item.tp_id)) {
                                            discardedItems.add(tblPhotos);
                                        }
                                    }
                                    if (discardedItems.size()>0) {
                                        photosarrayFresh.removeAll(discardedItems);
                                        if (!controller.equals("fresh")){
                                            postContentChangeNotificationFresh();
                                        }
                                    }
                                }
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;
                    }
                    case 4:{
                        try{
                            TblComment item = (TblComment)intent.getSerializableExtra("data");
                            String comment_count = intent.getStringExtra("comment_count");
                            if (item != null) {
                                synchronized (photosarrayHot) {

                                    for (TblPhotos tblPhotos : photosarrayHot) {
                                        if (tblPhotos.tp_id.equals(item.tc_tpid)) {
                                            tblPhotos.commentcount = comment_count;
                                        }
                                    }
                                    if (!controller.equals("hot")) {
                                        postContentChangeNotificationHot();
                                    }
                                }

                                synchronized (photosarrayFresh) {

                                    for (TblPhotos tblPhotos : photosarrayFresh) {
                                        if (tblPhotos.tp_id.equals(item.tc_tpid)) {
                                            tblPhotos.commentcount = comment_count;
                                        }
                                    }
                                    if (!controller.equals("fresh")) {
                                        postContentChangeNotificationFresh();
                                    }
                                }
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    };
}
