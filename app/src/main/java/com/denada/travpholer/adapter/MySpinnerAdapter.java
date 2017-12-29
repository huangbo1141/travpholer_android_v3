package com.denada.travpholer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.denada.travpholer.R;
import com.denada.travpholer.model.City;
import com.denada.travpholer.model.Country;
import com.denada.travpholer.model.States;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by hgc on 6/26/2015.
 */
public class MySpinnerAdapter extends ArrayAdapter<Object> {

    private List<Object> items;
    private Context mContext;
    private int curindex;
    private String hint = null;

    public MySpinnerAdapter(Context context, int index, Object[] items, String hint) {
        super(context,index,items);

        mContext    =   context;
        curindex    =   index;
        this.items = new LinkedList<Object>(Arrays.asList(items));
        this.hint = hint;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public MySpinnerAdapter(Context context, int index, ArrayList<Object> items,String hint) {
        super(context,index,items);
        mContext    =   context;
        curindex    =   index;
        this.items = items;
        this.hint = hint;
    }


    @Override
    public int getCount() {
        if (hint==null)
            return super.getCount();
        return items.size()+1;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent,2);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position,convertView,parent,1);
    }
    public View getCustomView(int position, View convertView, ViewGroup parent,int mode) {
        // TODO Auto-generated method stub
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(curindex, parent, false);
        TextView label=(TextView)row.findViewById(R.id.text1);
        Object data;
        if(hint==null){
            data = items.get(position);
        }else{
            if(position == 0){
                data = hint;
            }else{
                data = items.get(position-1);
            }
        }


        if (data instanceof String){
            label.setText((String)data);
        }else if(data instanceof Country){
            label.setText(((Country) data).countryName);
        }else if(data instanceof City){
            label.setText(((City) data).cityName);
        }else if(data instanceof States){
            label.setText(((States) data).stateName);
        }

        row.setBackgroundColor(Color.parseColor("#ffffff"));
        label.setTextColor(Color.parseColor("#000000"));

        return row;
    }

    public void updateData(ArrayList<Object> newData,String hint){
        items = newData;
        this.hint = hint;
        notifyDataSetChanged();
    }

    class ViewHolder{
        public TextView textView;
    }

    public String getSelectionText(int pos){
        Object data =  items.get(pos);
        if (data instanceof String){
            return (String)data;
        }else if(data instanceof Country ){
            return ((Country) data).countryName;
        }else if(data instanceof City){
            return ((City) data).cityName;
        }else if(data instanceof States ){
            return ((States) data).stateName;
        }else{
            return data.toString();
        }
    }
}
