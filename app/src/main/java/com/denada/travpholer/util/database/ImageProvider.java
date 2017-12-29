package com.denada.travpholer.util.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class ImageProvider extends ContentProvider {

  // database
  private ImageDatabaseHelper database;

  // used for the UriMacher
  private static final int Usage 	= 30;
  private static final int UsageList = 10;			//	get every app's (MOBILE	OR WIFI OR OTHER )  usage in list
  private static final int UsageItem = 20;

  public static final String AUTHORITY = "cwc.cwcmobileapps.util.database";

  public static final String BASE_PATH = "images";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/images";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/image";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
	  sURIMatcher.addURI(AUTHORITY, BASE_PATH , Usage);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/*", UsageList);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", UsageItem);
  }

  @Override
  public boolean onCreate() {
    database = new ImageDatabaseHelper(getContext());
    return false; 
  }

  
  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // check if the caller has requested a column which does not exists
    checkColumns(projection);

    // Set the table
    queryBuilder.setTables(ImageTable.TABLE_IMAGE);

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
    case Usage:

        break;
    case UsageList:
    	String path	=	uri.getLastPathSegment();
    	String params[]	=	path.split(";");

    	return null;
      
    case UsageItem:
      // adding the ID to the original query
      queryBuilder.appendWhere(ImageTable.IMAGE_ID + "="
          + uri.getLastPathSegment());
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case Usage:
      id = sqlDB.insert(ImageTable.TABLE_IMAGE, null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case Usage:
      rowsDeleted = sqlDB.delete(ImageTable.TABLE_IMAGE, selection,
          selectionArgs);
      break;
    case UsageItem:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(ImageTable.TABLE_IMAGE,
            ImageTable.IMAGE_ID + "=" + id,
            null);
      } else {
        rowsDeleted = sqlDB.delete(ImageTable.TABLE_IMAGE,
            ImageTable.IMAGE_ID + "=" + id
            + " and " + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case Usage:
      rowsUpdated = sqlDB.update(ImageTable.TABLE_IMAGE,
          values, 
          selection,
          selectionArgs);
      break;
    case UsageItem:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(ImageTable.TABLE_IMAGE,
            values,
            ImageTable.IMAGE_ID + "=" + id,
            null);
      } else {
        rowsUpdated = sqlDB.update(ImageTable.TABLE_IMAGE,
            values,
            ImageTable.IMAGE_ID + "=" + id
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }
  
  
  private void checkColumns(String[] projection) {
    String[] available = { ImageTable.IMAGE_ID, ImageTable.IMAGE_LNAME, ImageTable.IMAGE_REGION,
        ImageTable.IMAGE_CNAME, ImageTable.IMAGE_WEBCODE};
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }

} 