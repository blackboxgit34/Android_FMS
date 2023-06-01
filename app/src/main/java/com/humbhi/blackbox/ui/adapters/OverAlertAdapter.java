package com.humbhi.blackbox.ui.adapters;

import android.content.Context;

import android.text.Editable;
import android.text.TextWatcher;
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

import com.github.anastr.speedviewlib.TubeSpeedometer;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.models.AlertVehicleModel;


import java.util.ArrayList;

public class OverAlertAdapter extends RecyclerView.Adapter<OverAlertAdapter.MyHolder> {

    Context context;
    ArrayList<AlertVehicleModel> list;
    MyClickListener myClicklistener;

    public OverAlertAdapter(Context context, ArrayList<AlertVehicleModel>list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public OverAlertAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.overspeed_alertadapter
                , viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OverAlertAdapter.MyHolder holder, final int position)
    {

        holder.tvVehName.setText(list.get(position).getVehicleName());


            if (list.get(position).getOverSpeedAlert().equals("true"))
            {
                holder.switch1.setChecked(true);
            }
            else
            {
                holder.switch1.setChecked(false);
            }

        holder.edSetSpeed.setText(list.get(position).getOverSpeedLimit());
        holder.tubeSpeedometer.setSpeedAt(Float.parseFloat(String.valueOf(list.get(position).getOverSpeedLimit())));
        holder.edSetSpeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0)
                {
                    holder.tubeSpeedometer.setSpeedAt(Float.parseFloat(String.valueOf(charSequence)));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tvVehName;
        private SwitchCompat switch1;
        EditText edSetSpeed;
        TubeSpeedometer tubeSpeedometer;
        Button btSetSpeed;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);

            switch1 = itemView.findViewById(R.id.switch1);
            tvVehName = itemView.findViewById(R.id.tvVehName);
            edSetSpeed = itemView.findViewById(R.id.edSetSpeed);
            tubeSpeedometer = itemView.findViewById(R.id.tubeSpeedometer);
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
