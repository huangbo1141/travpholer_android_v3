package com.denada.travpholer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.denada.travpholer.view.autocomplete.managers.ContentManager;
import com.denada.travpholer.view.autocomplete.rest.RestClientManager;
import com.denada.travpholer.view.autocomplete.rest.model.Prediction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DAVID-WORK on 23/07/2015.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable
{
    private Context mContext;
    private List<String> resultList = new ArrayList<>();
    private List<Prediction> resultList_data = new ArrayList<>();

    public AutoCompleteAdapter(Context context)
    {
        mContext = context;
        RestClientManager.init(context);
        ContentManager.init(context);
    }

    @Override
    public int getCount()
    {
        return resultList.size();
    }

    @Override
    public String getItem(int index)
    {
        return resultList.get(index);
    }


    public Prediction getItem_data(int index)
    {
        return resultList_data.get(index);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        Filter filter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults filterResults = new FilterResults();
                if (constraint != null)
                {
                    List<Prediction> predicationList = findCities(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = predicationList;
                    filterResults.count = predicationList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                if (results != null && results.count > 0)
                {
                    resultList_data = (List<Prediction>) results.values;

                    ArrayList<String> descriptionList = new ArrayList<>();
                    for (Prediction prediction : resultList_data)
                    {
                        descriptionList.add(prediction.getDescription());
                    }
                    resultList = descriptionList;
                    notifyDataSetChanged();
                }
                else
                {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    /**
     * Returns a search result for the given city prefix
     */
    private List<Prediction> findCities(String input)
    {
        // synchronous execution!
        ArrayList<Prediction> syncPredictionList = RestClientManager.getInstance().getSyncPredictionList(input);
        if(syncPredictionList != null)
        {
            return ContentManager.getInstance().getPredictionList();
        }

        return null;
    }
}