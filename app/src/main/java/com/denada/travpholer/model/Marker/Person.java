/*
 * Copyright 2013 Google Inc.
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

package com.denada.travpholer.model.Marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.denada.travpholer.mapmarker.clustering.ClusterItem;
import com.denada.travpholer.model.TblPhotos;
import com.google.android.gms.maps.model.LatLng;

public class Person implements ClusterItem {
    public final String name;
    public Bitmap map_bitmap;
    private final LatLng mPosition;
    public String tp_thumb;
    public String tp_id;
    public String tp_countryid;


    public Person(LatLng position, String name, int pictureResource) {
        this.name = name;
        mPosition = position;
    }

    public Person(LatLng position, String name, int pictureResource,Context context) {
        this.name = name;

        mPosition = position;
        map_bitmap = BitmapFactory.decodeResource(context.getResources(), pictureResource);
    }

    public Person(TblPhotos tblPhotos){
        this.name = tblPhotos.tp_location;
        mPosition = new LatLng(Float.valueOf(tblPhotos.tp_lat),Float.valueOf(tblPhotos.tp_lon));
        tp_thumb = tblPhotos.tp_thumb;
        tp_id = tblPhotos.tp_id;
        tp_countryid = tblPhotos.tp_countryid;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
