package com.denada.travpholer.util.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.denada.travpholer.model.Country;

import java.util.ArrayList;

public class ImageDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "networkusage.db";
    public static final int DATABASE_VERSION = 2;

    public ImageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        ImageTable.onCreate(database);

    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        ImageTable.onUpgrade(database, oldVersion, newVersion);
    }

    public Country getCountryFromName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Country country = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + ImageTable.TABLE_IMAGE + " where countryName = '" + name + "';";
        Cursor c = db.rawQuery(sql, null);
        int count = 0;
        if (c.moveToFirst()) {
            int cid = c.getColumnIndex(ImageTable.IMAGE_ID);
            int cname = c.getColumnIndex(ImageTable.IMAGE_CNAME);
            int clat = c.getColumnIndex(ImageTable.IMAGE_LAT);
            int clon = c.getColumnIndex(ImageTable.IMAGE_LON);

            String countryId = c.getString(cid);
            String countrynane = c.getString(cname);
            String lat = c.getString(clat);
            String lon = c.getString(clon);

            country = new Country();
            country.countryID = countryId;
            country.countryName = countrynane;
            country.latitude = lat;
            country.longitude = lon;
        }
        if (country==null){
            country=  getCountryFromLocalName(name);
        }
        return country;
    }

    public Country getCountryFromLocalName(String name){
        if (name == null || name.isEmpty()) {
            return null;
        }
        Country country = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + ImageTable.TABLE_IMAGE + " where localName like '%" + name + "%';";
        Cursor c = db.rawQuery(sql, null);
        int count = 0;
        if (c.moveToFirst()) {
            int cid = c.getColumnIndex(ImageTable.IMAGE_ID);
            int cname = c.getColumnIndex(ImageTable.IMAGE_CNAME);
            int clat = c.getColumnIndex(ImageTable.IMAGE_LAT);
            int clon = c.getColumnIndex(ImageTable.IMAGE_LON);

            String countryId = c.getString(cid);
            String countrynane = c.getString(cname);
            String lat = c.getString(clat);
            String lon = c.getString(clon);

            country = new Country();
            country.countryID = countryId;
            country.countryName = countrynane;
            country.latitude = lat;
            country.longitude = lon;
        }

        return country;
    }
    public Country getCountryFromWebCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        Country country = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + ImageTable.TABLE_IMAGE + " where webCode = '" + code + "';";
        Cursor c = db.rawQuery(sql, null);
        int count = 0;
        if (c.moveToFirst()) {
            int cid = c.getColumnIndex(ImageTable.IMAGE_ID);
            int cname = c.getColumnIndex(ImageTable.IMAGE_CNAME);
            int clat = c.getColumnIndex(ImageTable.IMAGE_LAT);
            int clon = c.getColumnIndex(ImageTable.IMAGE_LON);

            String countryId = c.getString(cid);
            String countrynane = c.getString(cname);
            String lat = c.getString(clat);
            String lon = c.getString(clon);

            country = new Country();
            country.countryID = countryId;
            country.countryName = countrynane;
            country.latitude = lat;
            country.longitude = lon;
        }
        return country;
    }

    public ArrayList<Object> getCountries() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + ImageTable.TABLE_IMAGE + " where 1;";

        ArrayList<Object> countries = new ArrayList<>();
        Cursor c = db.rawQuery(sql, null);
        int count = 0;
        if (c.moveToFirst()) {
            int cid = c.getColumnIndex(ImageTable.IMAGE_ID);
            int cname = c.getColumnIndex(ImageTable.IMAGE_CNAME);
            int clat = c.getColumnIndex(ImageTable.IMAGE_LAT);
            int clon = c.getColumnIndex(ImageTable.IMAGE_LON);

            while (true) {
                String countryId = c.getString(cid);
                String countrynane = c.getString(cname);
                String lat = c.getString(clat);
                String lon = c.getString(clon);


                Country country = new Country();
                country.countryID = countryId;
                country.countryName = countrynane;
                country.latitude = lat;
                country.longitude = lon;
                countries.add(country);


                if (c.isLast())
                    break;
                c.moveToNext();
            }

        }
        c.close();
        return countries;
    }
}
 
 