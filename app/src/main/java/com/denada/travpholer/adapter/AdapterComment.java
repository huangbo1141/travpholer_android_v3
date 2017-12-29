package com.denada.travpholer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Response.HotResponse;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.util.http.ApiClient;
import com.denada.travpholer.view.UnderlinedTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * Created by Administrator on 9/19/2015.
 */
public class AdapterComment extends BaseAdapter
{
    //List<IconModel> m_lstItem;
    List<TblComment> m_lstItem;
    Context con;
    public View.OnClickListener onClickListener = null;
    public AdapterComment(Context con, List<TblComment> data)
    {
        super();
        m_lstItem = data;
        this.con = con;
    }
    public void setOnClickListner(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public int getCount()
    {
        return m_lstItem.size();
    }

    @Override
    public Object getItem(int position) {
        try{
            return m_lstItem.get(position);
        }catch (Exception ex){
            return null;
        }

    }


    public long getItemId(int paramInt)
    {
        return 0L;
    }


    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {

        View localView = paramView;
        ViewHolder localViewHolder = null;

        if (localView == null)
        {
            localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_comment, null);
        }
        else
        {
            localViewHolder = (ViewHolder) localView.getTag();
        }

        if (localViewHolder == null)
        {
            localViewHolder = new ViewHolder();
            localViewHolder.txtName = (TextView) localView.findViewById(R.id.txtName);
            localViewHolder.txtComment = (TextView) localView.findViewById(R.id.txtComment);
            localView.setTag(localViewHolder);
        }
        localView.setTag(localViewHolder);
        localViewHolder.setData(m_lstItem.get(paramInt));



        return localView;

    }

    public void update(List<TblComment> data)
    {
        this.m_lstItem = data;
        notifyDataSetChanged();
    }

    public void updateItem(TblComment item,int index){
        if (index>=0 && index < this.m_lstItem.size()){
            m_lstItem.set(index,item);
            notifyDataSetChanged();
        }

    }

    public class ViewHolder
    {

        public TextView txtName,txtComment;

        public void setData(final TblComment data){

            txtName.setText(data.tc_name);
            txtComment.setText(data.tc_content);

        }

    }
}
