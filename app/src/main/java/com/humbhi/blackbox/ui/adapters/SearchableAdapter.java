package com.humbhi.blackbox.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.humbhi.blackbox.R;

import java.util.ArrayList;

public class SearchableAdapter extends ArrayAdapter<String> {

    private ArrayList<String> originalData;
    private ArrayList<String> filteredData;

    public SearchableAdapter(Context context, ArrayList<String> list) {
        super(context, R.layout.spinner_text, list);
        originalData = new ArrayList<>(list);
        filteredData = new ArrayList<>(list);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        // Customize the appearance of dropdown items if needed
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                String searchString = constraint != null ? constraint.toString().trim() : "";
                if (searchString.isEmpty()) {
                    synchronized (this) {
                        filteredData.clear();
                        filteredData.addAll(originalData);
                    }
                } else {
                    synchronized (this) {
                        filteredData.clear();
                        for (String item : originalData) {
                            if (item.toLowerCase().contains(searchString.toLowerCase())) {
                                filteredData.add(item);
                            }
                        }
                    }
                }
                filterResults.values = filteredData;
                filterResults.count = filteredData.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<String>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public int getOriginalPosition(int filteredPosition) {
        String selectedItem = filteredData.get(filteredPosition);
        return originalData.indexOf(selectedItem);
    }

    public ArrayList<String> getOriginalData() {
        return originalData;
    }
}
