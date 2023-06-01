package com.humbhi.blackbox.ui.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.models.AlertVehicleModel;

import java.util.ArrayList;


public class OverStopAlertAdapter extends RecyclerView.Adapter<OverStopAlertAdapter.MyHolder> {

    Context context;
    ArrayList<AlertVehicleModel> list;
    MyClickListener myClicklistener;

    public OverStopAlertAdapter(Context context, ArrayList<AlertVehicleModel> list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public OverStopAlertAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.overstopalert_adapter
                , viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OverStopAlertAdapter.MyHolder holder, final int position)
    {

        holder.tvVehName.setText(list.get(position).getVehicleName());


        if (list.get(position).getOverStoppageAlert().equals("true"))
        {
            holder.switch1.setChecked(true);
        }
        else
        {
            holder.switch1.setChecked(false);
        }

        holder.edSetSpeed.setText(list.get(position).getOverStopLimit());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tvVehName;
        private SwitchCompat switch1;
        EditText edSetSpeed;
        Button btSetSpeed;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);

            switch1 = itemView.findViewById(R.id.switch1);
            tvVehName = itemView.findViewById(R.id.tvVehName);
            edSetSpeed = itemView.findViewById(R.id.edSetSpeed);

            btSetSpeed = itemView.findViewById(R.id.btSetSpeed);

            btSetSpeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myClicklistener.onSetSpeed(getAdapterPosition(),view,edSetSpeed);
                }
            });

            switch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(((SwitchCompat)view).isChecked())
                    {
                        myClicklistener.onSwitchClick(getAdapterPosition(),view,edSetSpeed);
                    }
                    else
                    {
                        myClicklistener.onSwitchClick(getAdapterPosition(),view,edSetSpeed);
                    }
                }
            });
        }
    }

    public interface MyClickListener

    {
        void onSwitchClick(int poss, View view1,EditText et);
        void onSetSpeed(int poss, View view1,EditText et);
    }


    public void onItemSelectedListener(MyClickListener clickListener)
    {
        myClicklistener = clickListener;
    }

}
