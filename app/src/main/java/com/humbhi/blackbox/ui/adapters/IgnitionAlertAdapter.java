package com.humbhi.blackbox.ui.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.models.AlertVehicleModel;


import java.util.ArrayList;

public class IgnitionAlertAdapter extends RecyclerView.Adapter<IgnitionAlertAdapter.MyHolder> {

    Context context;
    ArrayList<AlertVehicleModel>list;
    MyClickListener myClicklistener;

    public IgnitionAlertAdapter(Context context, ArrayList<AlertVehicleModel>list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public IgnitionAlertAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alert_adapter
                , viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IgnitionAlertAdapter.MyHolder holder, final int position)
    {

        holder.tvVehName.setText(list.get(position).getVehicleName());

        if (list.get(position).getMessageType().equals("1"))
        {
            if (list.get(position).getIgnitionStatusAlert().equals("true"))
            {
                holder.switch1.setChecked(true);
            }
            else
            {
                holder.switch1.setChecked(false);
            }
        }

        if (list.get(position).getMessageType().equals("4"))
        {
            if (list.get(position).getBatteryDisconnection().equals("true"))
            {
                holder.switch1.setChecked(true);
            }
            else
            {
                holder.switch1.setChecked(false);
            }
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tvVehName;
        private SwitchCompat switch1;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);

            switch1 = itemView.findViewById(R.id.switch1);
            tvVehName = itemView.findViewById(R.id.tvVehName);

            switch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((SwitchCompat)view).isChecked()){
                        myClicklistener.onSwitchClick(getAdapterPosition(),view);
                    }
                    else
                    {
                        myClicklistener.onSwitchClick(getAdapterPosition(),view);
                    }
                }
            });
        }
    }

    public interface MyClickListener

    {
        void onSwitchClick(int poss, View view1);
    }
    public void onItemSelectedListener(MyClickListener clickListener)
    {
        myClicklistener = clickListener;
    }

}
