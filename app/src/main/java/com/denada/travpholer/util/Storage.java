package com.denada.travpholer.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;


import com.denada.travpholer.util.exif.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class Storage {

    private static final String TAG = "Storage";

    public static final String MIME_TYPE_JPEG = "image/jpeg";

    //public static final String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    //public static final String DCIM = UsearchApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();
    //public static final String DCIM_DIRECTORY = DCIM + "/Camera";
    //public static final String PICTURES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
    //public static final String PICTURES = UsearchApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
    //public static final String DIRECTORY = PICTURES + "/uSearch";

    public static final String FILMSTRIP_DIRECTORY = "Filmstrip";
    public static final String CAMERA_DIRECTORY = "Camera";
    public static final String LOGS_DIRECTORY = "Logs";
    public static final String CACHE_DIRECTORY = "Cache";
    public static final String STATE_DIRECTORY = "State";

    public static final String JPEG_POSTFIX = ".jpg";

    // Match the code in MediaProvider.computeBucketValues().
    //public static final String BUCKET_ID = String.valueOf(DIRECTORY.toLowerCase().hashCode());

    public static final long UNAVAILABLE = -1L;
    public static final long PREPARING = -2L;
    public static final long UNKNOWN_SIZE = -3L;
    public static final long LOW_STORAGE_THRESHOLD_BYTES = 50000000;

    private static boolean mExternalStorageAvailable = false;
    private static boolean mExternalStorageWriteable = false;

    @SuppressLint("NewApi") public static File getFilmstripDirectory(Context context) {
        Log.e(Storage.TAG, "Storage.getFilmstripDirectory().1: context => " + (context == null ? "(null)" : "(context)"));
        File file = null;
        File path = context.getExternalFilesDir(null);
        if (path != null) {
            Log.e(Storage.TAG, "Storage.getFilmstripDirectory().2: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            if (!Environment.isExternalStorageRemovable()) {
                updateExternalStorageState();
                Log.e(Storage.TAG, "Storage.getFilmstripDirectory().3: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable);
                if (mExternalStorageAvailable && mExternalStorageWriteable) {
                    file = new File(path, FILMSTRIP_DIRECTORY);
                    Log.e(Storage.TAG, "Storage.getFilmstripDirectory().4: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable + ", file.getAbsolutePath() => " + file.getAbsolutePath());
                }
            }
        }
        if (file == null) {
            path = context.getFilesDir();
            Log.e(Storage.TAG, "Storage.getFilmstripDirectory().5: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            file = new File(path, FILMSTRIP_DIRECTORY);
            //file = context.getDir(FILMSTRIP_DIRECTORY, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
            //Log.e(Storage.TAG, "Storage.getFilmstripDirectory().6: context => " + (context == null ? "(null)" : "(context)") + ", file.getAbsolutePath() => " + file.getAbsolutePath());
            Log.e(Storage.TAG, "Storage.getFilmstripDirectory().6: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", file.getAbsolutePath() => " + file.getAbsolutePath());
        }
        Log.e(Storage.TAG, "Storage.getFilmstripDirectory().7: context => " + (context == null ? "(null)" : "(context)") + ", file.getAbsolutePath() => " + (file == null ? "(null)" : file.getAbsolutePath()));
        if (file != null) file.mkdirs();
        return file;
    }

    @SuppressLint("NewApi") public static File getCameraDirectory(Context context) {
        Log.e(Storage.TAG, "Storage.getCameraDirectory().1: context => " + (context == null ? "(null)" : "(context)"));
        File file = null;
        File path = context.getExternalFilesDir(null);
        if (path != null) {
            Log.e(Storage.TAG, "Storage.getCameraDirectory().2: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            if (!Environment.isExternalStorageRemovable()) {
                updateExternalStorageState();
                Log.e(Storage.TAG, "Storage.getCameraDirectory().3: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable);
                if (mExternalStorageAvailable && mExternalStorageWriteable) {
                    file = new File(path, CAMERA_DIRECTORY);
                    Log.e(Storage.TAG, "Storage.getCameraDirectory().4: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable + ", file.getAbsolutePath() => " + file.getAbsolutePath());
                }
            }
        }
        if (file == null) {
            path = context.getFilesDir();
            Log.e(Storage.TAG, "Storage.getCameraDirectory().5: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            file = new File(path, CAMERA_DIRECTORY);
            Log.e(Storage.TAG, "Storage.getCameraDirectory().6: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", file.getAbsolutePath() => " + file.getAbsolutePath());
        }
        Log.e(Storage.TAG, "Storage.getCameraDirectory().7: context => " + (context == null ? "(null)" : "(context)") + ", file.getAbsolutePath() => " + (file == null ? "(null)" : file.getAbsolutePath()));
        if (file != null) file.mkdirs();
        return file;
    }

    @SuppressLint("NewApi") public static File getCacheDirectory(Context context) {
        Log.e(Storage.TAG, "Storage.getCacheDirectory().1: context => " + (context == null ? "(null)" : "(context)"));
        File file = null;
        File path = context.getExternalCacheDir();
        if (path != null) {
            Log.e(Storage.TAG, "Storage.getCacheDirectory().2: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            if (!Environment.isExternalStorageRemovable()) {
                updateExternalStorageState();
                Log.e(Storage.TAG, "Storage.getCacheDirectory().3: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable);
                if (mExternalStorageAvailable && mExternalStorageWriteable) {
                    file = new File(path, CACHE_DIRECTORY);
                    Log.e(Storage.TAG, "Storage.getCacheDirectory().4: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable + ", file.getAbsolutePath() => " + file.getAbsolutePath());
                }
            }
        }
        if (file == null) {
            path = context.getCacheDir();
            Log.e(Storage.TAG, "Storage.getCacheDirectory().5: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            file = new File(path, CACHE_DIRECTORY);
            Log.e(Storage.TAG, "Storage.getCacheDirectory().6: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", file.getAbsolutePath() => " + file.getAbsolutePath());
        }
        Log.e(Storage.TAG, "Storage.getCacheDirectory().7: context => " + (context == null ? "(null)" : "(context)") + ", file.getAbsolutePath() => " + (file == null ? "(null)" : file.getAbsolutePath()));
        if (file != null) file.mkdirs();
        return file;
    }

    @SuppressLint("NewApi") public static File getLogsDirectory(Context context) {
        Log.e(Storage.TAG, "Storage.getLogsDirectory().1: context => " + (context == null ? "(null)" : "(context)"));
        File file = null;
        File path = context.getExternalFilesDir(null);
        if (path != null) {
            Log.e(Storage.TAG, "Storage.getLogsDirectory().2: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            if (!Environment.isExternalStorageRemovable()) {
                updateExternalStorageState();
                Log.e(Storage.TAG, "Storage.getLogsDirectory().3: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable);
                if (mExternalStorageAvailable && mExternalStorageWriteable) {
                    file = new File(path, LOGS_DIRECTORY);
                    Log.e(Storage.TAG, "Storage.getLogsDirectory().4: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable + ", file.getAbsolutePath() => " + file.getAbsolutePath());
                }
            }
        }
        if (file == null) {
            path = context.getFilesDir();
            Log.e(Storage.TAG, "Storage.getLogsDirectory().5: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            file = new File(path, LOGS_DIRECTORY);
            Log.e(Storage.TAG, "Storage.getLogsDirectory().6: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", file.getAbsolutePath() => " + file.getAbsolutePath());
        }
        Log.e(Storage.TAG, "Storage.getLogsDirectory().7: context => " + (context == null ? "(null)" : "(context)") + ", file.getAbsolutePath() => " + (file == null ? "(null)" : file.getAbsolutePath()));
        if (file != null) file.mkdirs();
        return file;
    }

    @SuppressLint("NewApi") public static File getStateDirectory(Context context) {
        Log.e(Storage.TAG, "Storage.getStateDirectory().1: context => " + (context == null ? "(null)" : "(context)"));
        File file = null;
        File path = context.getExternalFilesDir(null);
        if (path != null) {
            Log.e(Storage.TAG, "Storage.getStateDirectory().2: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            if (!Environment.isExternalStorageRemovable()) {
                updateExternalStorageState();
                Log.e(Storage.TAG, "Storage.getStateDirectory().3: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable);
                if (mExternalStorageAvailable && mExternalStorageWriteable) {
                    file = new File(path, STATE_DIRECTORY);
                    Log.e(Storage.TAG, "Storage.getStateDirectory().4: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", Environment.isExternalStorageRemovable() => " + Environment.isExternalStorageRemovable() + ", mExternalStorageAvailable => " + mExternalStorageAvailable + ", mExternalStorageWriteable => " + mExternalStorageWriteable + ", file.getAbsolutePath() => " + file.getAbsolutePath());
                }
            }
        }
        if (file == null) {
            path = context.getFilesDir();
            Log.e(Storage.TAG, "Storage.getStateDirectory().5: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath());
            file = new File(path, STATE_DIRECTORY);
            Log.e(Storage.TAG, "Storage.getStateDirectory().6: context => " + (context == null ? "(null)" : "(context)") + ", path.getAbsolutePath() => " + path.getAbsolutePath() + ", file.getAbsolutePath() => " + file.getAbsolutePath());
        }
        Log.e(Storage.TAG, "Storage.getStateDirectory().7: context => " + (context == null ? "(null)" : "(context)") + ", file.getAbsolutePath() => " + (file == null ? "(null)" : file.getAbsolutePath()));
        if (file != null) file.mkdirs();
        return file;
    }

    public static void writeFile(String path, byte[] jpeg, ExifInterface exif) {
        Log.e(Storage.TAG, "Storage.writeFile().1: path => " + (path == null ? "(path)" : path) + ", jpeg => " + (jpeg == null ? "(null)" : "(jpeg)") + ", exif => " + (exif == null ? "(null)" : "(exif)"));
        if (exif != null) {
            try {
                exif.writeExif(jpeg, path);
            } catch (Exception e) {
                Log.e(Storage.TAG, "Storage.writeFile().2: Failed to write data", e);
            }
        } else {
            writeFile(path, jpeg);
        }
    }

    public static void writeFile(File path, byte[] jpeg, ExifInterface exif) {
        Log.e(Storage.TAG, "Storage.writeFile().1: jpeg => " + (jpeg == null ? "(null)" : "(jpeg)") + ", path.getAbsolutePath() => " + (path == null ? "(null)" : path.getAbsolutePath()));
        if (exif != null) {
            try {
                exif.writeExif(jpeg, path.getAbsolutePath());
            } catch (Exception e) {
                Log.e(Storage.TAG, "Storage.writeFile().2: Failed to write data", e);
            }
        } else {
            writeFile(path, jpeg);
        }
    }

    public static void writeFile(String path, byte[] data) {
        Log.e(Storage.TAG, "Storage.writeFile().1: path => " + (path == null ? "(null)" : path) + ", data => " + (data == null ? "(null)" : "(data)"));
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(data);
        } catch (Exception e) {
            Log.e(Storage.TAG, "Storage.writeFile().2: Failed to write data", e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                Log.e(Storage.TAG, "Storage.writeFile().3: Failed to close file after write", e);
            }
        }
    }

    public static void writeFile(File path, byte[] data) {
        Log.e(Storage.TAG, "Storage.writeFile().1: data => " + (data == null ? "(null)" : "(data)") + ", path.getAbsolutePath() => " + (path == null ? "(null)" : path.getAbsolutePath()));
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(data);
        } catch (Exception e) {
            Log.e(Storage.TAG, "Storage.writeFile().2: Failed to write data", e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                Log.e(Storage.TAG, "Storage.writeFile().3: Failed to close file after write", e);
            }
        }
    }

    // Save the image in a given directory.
    public static String addImage(File directory, byte[] jpeg, ExifInterface exif, String title) {
        Log.e(Storage.TAG, "Storage.addImage().1: directory => " + (directory == null ? "(null)" : directory.getAbsolutePath()) + ", jpeg => " + (jpeg == null ? "(null)" : "(jpeg)") + ", exif => " + (exif == null ? "(null)" : "(exif)") + ", title => " + title);
        File path = generatePath(directory, title);
        Log.e(Storage.TAG, "Storage.addImage().2: directory => " + (directory == null ? "(null)" : directory.getAbsolutePath()) + ", jpeg => " + (jpeg == null ? "(null)" : "(jpeg)") + ", exif => " + (exif == null ? "(null)" : "(exif)") + ", title => " + title + ", path => " + (path == null ? "(null)" : path.getAbsolutePath()));
        writeFile(path, jpeg, exif);
        return path.getAbsolutePath();
    }

    // Overwrites the file, or inserts the image if one does not already exist.
    public static void updateImage(String path, byte[] jpeg, ExifInterface exif) {
        Log.e(Storage.TAG, "Storage.updateImage(): path => " + (path == null ? "(null)" : path) + ", jpeg => " + (jpeg == null ? "(null)" : "(jpeg)") + ", exif => " + (exif == null ? "(null)" : "(exif)"));
        writeFile(path, jpeg, exif);
    }

    public static void deleteImage(String path) {
        Log.e(Storage.TAG, "Storage.deleteImage(): path: " + path);
        File file = new File(path);
        if (file != null) {
            file.delete();
        }
    }

    public static File generatePath(File directory, String title) {
        Log.e(Storage.TAG, "Storage.generatePath(): directory => " + (directory == null ? "(null)" : directory.getAbsolutePath()) + ", title => " + title + ".jpg");
        return new File(directory, title + JPEG_POSTFIX);
    }

    public static long getAvailableSpace(Context context) {
        String state = Environment.getExternalStorageState();
        Log.e(Storage.TAG, "Storage.getAvailableSpace().1: External storage state=" + state);
        if (Environment.MEDIA_CHECKING.equals(state)) {
            return PREPARING;
        }
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return UNAVAILABLE;
        }
        File dir = getCameraDirectory(context);
        dir.mkdirs();
        if (!dir.isDirectory() || !dir.canWrite()) {
            return UNAVAILABLE;
        }
        try {
            StatFs stat = new StatFs(dir.getAbsolutePath());
            return stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            Log.i(Storage.TAG, "Storage.getAvailableSpace().2: Fail to access external storage", e);
        }
        return UNKNOWN_SIZE;
    }

    /**
     * OSX requires plugged-in USB storage to have path /DCIM/NNNAAAAA to be
     * imported. This is a temporary fix for bug#1655552.
     */
    public static void ensureOSXCompatible(Context context) {
        File nnnAAAAA = new File(getCameraDirectory(context), "100ANDRO");
        if (!(nnnAAAAA.exists() || nnnAAAAA.mkdirs())) {
            Log.e(Storage.TAG, "Storage.ensureOSXCompatible(): Failed to create " + nnnAAAAA.getPath());
        }
    }

    private static void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }
}