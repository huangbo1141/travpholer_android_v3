package com.denada.travpholer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.R;
import com.denada.travpholer.model.ActionType;
import com.denada.travpholer.model.TblPhotos;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hgc on 6/14/2016.
 */

public class RecyclerAdapterHot
        extends RecyclerView.Adapter<RecyclerAdapterHot.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    public View.OnClickListener onClickListener = null;
    List<TblPhotos> m_lstItem;
    Context con;
    Display display;
    private int mBackground;

    public RecyclerAdapterHot(Context context, List<TblPhotos> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        m_lstItem = items;
        con = context;
    }

    public void setOnClickListner(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TblPhotos getItem(int position) {
        return m_lstItem.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder localViewHolder, int paramInt) {

        localViewHolder.img_report.setOnClickListener(onClickListener);
        localViewHolder.txt_ownername.setOnClickListener(onClickListener);
        localViewHolder.img_share.setOnClickListener(onClickListener);
        localViewHolder.img_like.setOnClickListener(onClickListener);
        localViewHolder.img_link.setOnClickListener(onClickListener);
        localViewHolder.img_comment.setOnClickListener(onClickListener);
        localViewHolder.txt_title.setOnClickListener(onClickListener);
        localViewHolder.layComment.setOnClickListener(onClickListener);
        localViewHolder.layContent.setOnClickListener(onClickListener);

        localViewHolder.setData(m_lstItem.get(paramInt));

        ActionType actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 0;
        actionType.imageView = localViewHolder.img_content;
        actionType.txtLikesCount = localViewHolder.txt_likescount;

        localViewHolder.img_like.setTag(actionType);

        actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 1;
        actionType.imageView = localViewHolder.img_content;
        localViewHolder.img_report.setTag(actionType);

        actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 2;
        actionType.imageView = localViewHolder.img_content;
        localViewHolder.txt_ownername.setTag(actionType);


        actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 3;
        actionType.imageView = localViewHolder.img_content;
        localViewHolder.img_share.setTag(actionType);

        actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 6;
        actionType.imageView = localViewHolder.img_content;
        localViewHolder.img_comment.setTag(actionType);

        actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 6;
        actionType.imageView = localViewHolder.img_content;
        localViewHolder.layComment.setTag(actionType);

        actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 5;
        actionType.imageView = localViewHolder.img_content;
        localViewHolder.img_link.setTag(actionType);
        localViewHolder.txt_title.setTag(actionType);

        actionType = new ActionType();
        actionType.index = paramInt;
        actionType.actiontype = 5;
        actionType.imageView = localViewHolder.img_content;
        localViewHolder.img_link.setTag(actionType);
        localViewHolder.layContent.setTag(actionType);
    }

    @Override
    public int getItemCount() {
        return m_lstItem.size();
    }

    public void update(List<TblPhotos> data) {
        this.m_lstItem = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public final View mView;
        public View title_underline;
        public TextView txt_title;
        public TextView txt_location, txt_ownerrank;
        public ImageView img_content;
        public CircleImageView circleImageView;
        public TextView txt_ownername, txt_likescount, txt_commentcount;
        public ImageView img_report, img_like;

        public ProgressBar progressBar;
        public View img_share, img_comment, img_link;
        public View view_link, layComment, layContent;


        public ViewHolder(View localView) {
            super(localView);
            mView = localView;
            txt_title = (TextView) localView.findViewById(R.id.txt_title);
            title_underline = localView.findViewById(R.id.title_underline);
            txt_location = (TextView) localView.findViewById(R.id.txt_location);
            txt_ownername = (TextView) localView.findViewById(R.id.txt_ownername);
            txt_likescount = (TextView) localView.findViewById(R.id.txt_likecount);
            img_content = (ImageView) localView.findViewById(R.id.img_content);
            circleImageView = (CircleImageView) localView.findViewById(R.id.profile_image);
            img_report = (ImageView) localView.findViewById(R.id.img_report);
            img_like = (ImageView) localView.findViewById(R.id.img_like);
            progressBar = (ProgressBar) localView.findViewById(R.id.progressBar);
            img_share = localView.findViewById(R.id.img_share);
            img_comment = localView.findViewById(R.id.img_comment);
            img_link = localView.findViewById(R.id.img_link);
            view_link = localView.findViewById(R.id.layHyperlink);
            txt_commentcount = (TextView) localView.findViewById(R.id.txt_commentcount);
            txt_ownerrank = (TextView) localView.findViewById(R.id.txt_rankname);
            layComment = localView.findViewById(R.id.layComment);
            layContent = localView.findViewById(R.id.layContent);
        }

        public void setData(final TblPhotos data) {

            String pathThumb = CGlobal.getPhotoPath(data.tp_picpath);

            progressBar.setVisibility(View.VISIBLE);
            if (pathThumb != null && !pathThumb.isEmpty()) {
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
                                progressBar.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .into(img_content);

            }
            try {
                pathThumb = data.owner.tu_pic;
                if (pathThumb != null && !pathThumb.isEmpty()) {
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
                            .into(circleImageView);
                }
                if (data.owner.tblBaseRank != null) {
                    txt_ownerrank.setText("  [" + data.owner.tblBaseRank.tbr_title + "]");
                } else {
                    txt_ownerrank.setText("");
                }
                txt_ownername.setText(data.owner.getUsername());
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            txt_title.setText(data.tp_title);
            if (data.tp_hyperlink != null && data.tp_hyperlink.length() >= 4) {

                img_link.setVisibility(View.VISIBLE);
                title_underline.setVisibility(View.VISIBLE);

//                txt_title.setPaintFlags(Paint.ANTI_ALIAS_FLAG);

            } else {
                img_link.setVisibility(View.GONE);
                title_underline.setVisibility(View.GONE);

//                txt_title.setPaintFlags(txt_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            txt_location.setText(data.tp_location);

            txt_likescount.setText(data.likescount);
            txt_commentcount.setText(data.commentcount);

            if (data.ilikethis == null || data.ilikethis.isEmpty() || data.ilikethis.equals("-1")) {
                img_like.setImageResource(R.drawable.like);
            } else {
                img_like.setImageResource(R.drawable.unlike);
            }


            try {
                Float tpheight = Float.valueOf(data.tp_height);
                Float tpwidth = Float.valueOf(data.tp_width);
                float screenwidth = CGlobal.display.getWidth();
                float height = tpheight / tpwidth * screenwidth;
                ViewGroup.LayoutParams layoutParams = img_content.getLayoutParams();
                layoutParams.width = (int) screenwidth;
                layoutParams.height = (int) height;
                img_content.setLayoutParams(layoutParams);
            } catch (Exception ex) {

            }
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}