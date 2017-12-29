package com.denada.travpholer;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.denada.travpholer.util.CLog;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;


@ReportsCrashes(
        mailTo = "bohuang29@hotmail.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig =
                new TwitterAuthConfig("9OIypj02F1jKwacVSyOEbgwNt",
                        "hkzM07C7cZGAaTsuyVjQCefSwGVb9mE75SGRv2rc8kFGPCc3yW");
//      TwitterAuthConfig authConfig =
//              new TwitterAuthConfig("da3CdyTTbPd8ivBN1vs0BIfOK",
//                      "JFrxFGLSJUjeEn0Hgfd10SSCzMwW6OAWwfAGn2HhFc8yUXaPJz");

        Fabric.with(this, new Twitter(authConfig));
        printHashKey();

        ACRA.init(this);
        CLog.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CLog.release();

    }


    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.denada.travpholer",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(" KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        } catch (NoSuchAlgorithmException e) {

        }
    }
}
