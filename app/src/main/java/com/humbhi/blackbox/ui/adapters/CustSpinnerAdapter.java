package com.humbhi.blackbox.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.humbhi.blackbox.R;

import java.util.ArrayList;

public class CustSpinnerAdapter {
    public static ArrayAdapter<String> getAdapter(Context mContext, ArrayList<String> list)
    {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text,list)
        {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
               /* if(position==0)
                {
                    tv.setTextColor(Color.GRAY);

                }
                else
                {
                    tv.setTextColor(Color.WHITE);
                }*/
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_text);
        return spinnerArrayAdapter;
    }
}
