package com.denada.travpholer.util;

import android.util.Log;


import com.denada.travpholer.util.exif.ExifInterface;

import java.io.IOException;


public class Exif {
    private static final String TAG = "CameraExif";

    public static ExifInterface getExif(byte[] jpegData) {
        ExifInterface exif = new ExifInterface();
        try {
            exif.readExif(jpegData);
        } catch (IOException e) {
            Log.w(TAG, "Failed to read EXIF data", e);
        }
        return exif;
    }

    // Returns the degrees in clockwise. Values are 0, 90, 180, or 270.
    public static int getOrientation(ExifInterface exif) {
        Integer val = exif.getTagIntValue(ExifInterface.TAG_ORIENTATION);
        if (val == null) {
            return 0;
        } else {
            return ExifInterface.getRotationForOrientationValue(val.shortValue());
        }
    }

    public static int getOrientation(byte[] jpegData) {
        if (jpegData == null) return 0;

        ExifInterface exif = getExif(jpegData);
        return getOrientation(exif);
    }

    public static int getImageWidth(ExifInterface exif) {
        Integer val = exif.getTagIntValue(ExifInterface.TAG_IMAGE_WIDTH);
        Log.e(TAG, "Exif.getImageWidth(): val => " + (val == null ? "(null)" : val.intValue()));
        if (val == null) {
            return 0;
        } else {
            return val.intValue();
        }
    }

    public static int getImageWidth(byte[] jpegData) {
        if (jpegData == null) return 0;
        ExifInterface exif = getExif(jpegData);
        return getImageWidth(exif);
    }

    public static int getImageHeight(ExifInterface exif) {
        Integer val = exif.getTagIntValue(ExifInterface.TAG_IMAGE_LENGTH);
        Log.e(TAG, "Exif.getImageHeight(): val => " + (val == null ? "(null)" : val.intValue()));
        if (val == null) {
            return 0;
        } else {
            return val.intValue();
        }
    }

    public static int getImageHeight(byte[] jpegData) {
        if (jpegData == null) return 0;
        ExifInterface exif = getExif(jpegData);
        return getImageHeight(exif);
    }

    public static String getDateTime(ExifInterface exif) {
        String val = exif.getTagStringValue(ExifInterface.TAG_DATE_TIME);
        Log.e(TAG, "Exif.getDateTime(): val => " + (val == null ? "(null)" : val));
        return val;
    }

    public static String getDateTime(byte[] jpegData) {
        if (jpegData == null) return null;
        ExifInterface exif = getExif(jpegData);
        return getDateTime(exif);
    }

    public static double[] getLatLng(ExifInterface exif) {
        double[] val = exif.getLatLongAsDoubles();
        Log.e(TAG, "Exif.getLatLng(): val => " + (val == null ? "(null)" : val));
        return val;
    }
}