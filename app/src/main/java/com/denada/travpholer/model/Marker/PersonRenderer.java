package com.denada.travpholer.model.Marker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.denada.travpholer.Fragment.MapTogoFragment;
import com.denada.travpholer.R;
import com.denada.travpholer.mapmarker.clustering.Cluster;
import com.denada.travpholer.mapmarker.clustering.ClusterManager;
import com.denada.travpholer.mapmarker.clustering.view.DefaultClusterRenderer;
import com.denada.travpholer.mapmarker.ui.IconGenerator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hgc on 7/21/2016.
 */
public class PersonRenderer extends DefaultClusterRenderer<Person> {
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
    private final ImageView mImageView;
    private final ImageView mClusterImageView;
    private final ImageView mMarkerImageView;
    private final int mDimension;
    Activity mActivity;

    public PersonRenderer(Activity activity,GoogleMap map,ClusterManager clusterManager) {
        super(activity.getApplicationContext(), map, clusterManager);
        mIconGenerator = new IconGenerator(activity.getApplicationContext());
        mClusterIconGenerator = new IconGenerator(activity.getApplicationContext());
        mActivity = activity;

        View multiProfile = activity.getLayoutInflater().inflate(R.layout.multi_profile, null);
        mClusterIconGenerator.setContentView(multiProfile);
        mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
        mMarkerImageView = (ImageView) multiProfile.findViewById(R.id.imgMarker);

        mImageView = new ImageView(activity.getApplicationContext());
        mDimension = (int) activity.getResources().getDimension(R.dimen.custom_profile_image);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        int padding = (int) activity.getResources().getDimension(R.dimen.custom_profile_padding);
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);


    }

    @Override
    protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.

        Bitmap bm = MapTogoFragment.getMarkerBitmap1(person.map_bitmap, mActivity, "1");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {

        Person person = null;
        for (Person p:cluster.getItems()){
            person = p;
            break;
        }
        Bitmap bm = MapTogoFragment.getMarkerBitmap1(person.map_bitmap,mActivity,String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}