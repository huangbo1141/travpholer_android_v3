package com.denada.travpholer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.R;
import com.denada.travpholer.model.ActionType;
import com.denada.travpholer.model.TblNotificationTrack;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hgc on 6/14/2016.
 */

public class RecyclerAdapterNotification
        extends RecyclerView.Adapter<RecyclerAdapterNotification.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    List<Object> m_lstItem;
    Context con;

    public View.OnClickListener onClickListener = null;
    public void setOnClickListner(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        

        public final View mView;
        public TextView txtName,txtTitle,txtTime;
        public ImageView imgPicture;
        public CircleImageView imgProfile;


        public ViewHolder(View localView) {
            super(localView);
            mView = localView;
            txtName = (TextView) localView.findViewById(R.id.txtName);
            txtTitle = (TextView) localView.findViewById(R.id.txtTitle);
            txtTime = (TextView) localView.findViewById(R.id.txtTime);

            imgPicture = (ImageView) localView.findViewById(R.id.imgPicture);
            imgProfile = (CircleImageView) localView.findViewById(R.id.imgProfile);
        }

        public void setData(final Object data){

            TblNotificationTrack track = (TblNotificationTrack) data;
            txtName.setText(track.username);
            txtTitle.setText(track.tp_title);
            txtTime.setText(CGlobal.getAgoTime(track.create_datetime));
            if (track.tp_picpath!=null && !track.tp_picpath.isEmpty()){
                String pathThumb = CGlobal.getThumbPhotoPath(track.tp_picpath);
                Glide.with(con)
                        .load(pathThumb)
                        .asBitmap()
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {


                                return false;
                            }
                        })
                        .into(imgPicture);
            }

            if (track.tu_pic!=null && !track.tu_pic.isEmpty()){
                String pathThumb = track.tu_pic;
                Glide.with(con)
                        .load(pathThumb)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {

                                return false;
                            }
                        })
                        .into(imgProfile);
            }

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


    public Object getItem(int position) {
        return m_lstItem.get(position);
    }

    public RecyclerAdapterNotification(Context context, List<Object> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        m_lstItem = items;
        con = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notification_like, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notification_comment, parent, false);
        }

        
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Object object = this.m_lstItem.get(position);
        if (object instanceof TblNotificationTrack){
            TblNotificationTrack track = (TblNotificationTrack)object;
            return Integer.valueOf(track.noti_type);
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder localViewHolder, int paramInt) {

        localViewHolder.mView.setOnClickListener(onClickListener);

        localViewHolder.setData(m_lstItem.get(paramInt));

        ActionType actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 0;
        localViewHolder.mView.setTag(actionType);
    }

    @Override
    public int getItemCount() {
        return m_lstItem.size();
    }
    public void update(List<Object> data)
    {
        this.m_lstItem = data;
        notifyDataSetChanged();
    }
}