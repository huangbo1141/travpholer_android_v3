/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.denada.travpholer;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.denada.travpholer.Activity.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.denada.travpholer.Activity.SplashActivity;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.model.TblBaseAch;
import com.denada.travpholer.model.TblBaseRank;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */

public class GcmIntentService extends IntentService {
	public static int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	TaskStackBuilder stackBuilder;
	String mTitle;
	SharedPreferences pref;
	Editor edit;
	boolean is_noty = false;
	Handler mHandler;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mHandler = new Handler();
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification(extras);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification(extras);
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				// Post notification of received message.
				sendNotification(extras);

				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle msg) {

		// handle notification here
		/*
		 * types of notification 1. result update 2. circular update 3. student
		 * corner update 4. App custom update 5. Custom Message 6. Notice from
		 * College custom
		 */
		showSampleNotifcation();
		try{
			int type = Integer.parseInt(msg.getString("type"));
//		String title = msg.getString("title");
//		String message = msg.getString("message");
			String title = "";
			String message = "";
			String ticker="";
			int notid = 0x1001 + type;

			PendingIntent contentIntent = null;
			Context con = this;

			SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
			switch (type) {

				case 1: {
					String rdata = msg.getString("rdata");
					TblBaseRank tblBaseRank = TblBaseRank.parseJson(rdata);
					title = "Congratulations";
					message = "You have reached "+tblBaseRank.tbr_title+" – Level "+ tblBaseRank.tbr_rank +"!";

					Intent intent;
					if (CGlobal.curUser == null){
						intent = new Intent(this,SplashActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					}else{
						intent = new Intent(this,MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					}
					contentIntent = PendingIntent.getActivity(con, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
					break;
				}
				case 2: {
					String rdata = msg.getString("rdata");
					TblBaseAch ach = TblBaseAch.parseJson(rdata);
					message = "New Travel Achievement Unlocked – "+ ach.tba_name +"!";
					title = "Congratulations";

					Intent intent;

					if (CGlobal.curUser == null){
						intent = new Intent(this,SplashActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					}else{
						intent = new Intent(this,MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					}
					contentIntent = PendingIntent.getActivity(con, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
					break;
				}
				case 3: {
					title = "Offer";
					message = "You offer rejected";
					break;
				}
				case 4: {
					title = "Bid";
					message = "You have beed out bid";
					break;
				}
				case 5: {
					title = "Alert";
					message = "The bids you placed has 10 min remains";
					break;
				}
				case 6: {
					title = "Congratulations";
					message = "You won the bid";
					break;
				}
				default:
					return;
			}
			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this);

			mBuilder.setSmallIcon(R.mipmap.ic_launcher)
					.setContentTitle(title)
					.setTicker(ticker)
					.setAutoCancel(true)
					.setContentText(message).setContentIntent(null);

			mNotificationManager.notify(notid, mBuilder.build());
		}catch (Exception ex) {
			Log.e("GcmIntent",ex.toString());
		}



	}

	public void showSampleNotifcation(){
		String title = "";
		String message = "";
		String ticker="";
		int notid = 0x1001;
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);

		mBuilder.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setTicker(ticker)
				.setAutoCancel(true)
				.setContentText(message).setContentIntent(null);

		mNotificationManager.notify(notid, mBuilder.build());
	}
	public  void makeNotification(ArrayList<TblBaseAch> list){
		for(int i=0; i<list.size(); i++){
			int notid = 0x1001 + i;

			TblBaseAch ach = list.get(i);
			String title = "Congratulations";
			String message = "New Travel Achievement Unlocked – "+ ach.tba_name +"!";
			String ticker = "New Achievement";

			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this);

			mBuilder.setSmallIcon(R.mipmap.ic_launcher)
					.setContentTitle(title)
					.setTicker(ticker)
					.setAutoCancel(true)
					.setContentText(message).setContentIntent(null);
			mNotificationManager.notify(notid, mBuilder.build());
		}
	}
	private String getUTCDatetime(){

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date();
		String localTime = fmt.format(date);

		return localTime;
	}
	private class DisplayToast implements Runnable {
		String mText;

		public DisplayToast(String text) {
			mText = text;
		}

		public void run() {
			Toast.makeText(GcmIntentService.this, mText, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		this.stopForeground(true);
	}
}
