package com.humbhi.blackbox.ui.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.models.ImmobilizeVehicleModel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ImmobilizeAdapetr extends RecyclerView.Adapter<ImmobilizeAdapetr.MyHolder> {

    Context context;

    MyClickListener myClicklistener;

    ArrayList<ImmobilizeVehicleModel>list;

    public ImmobilizeAdapetr(Context context, ArrayList<ImmobilizeVehicleModel>list)
    {
        this.context = context;
        this.list = list;
    }
    long elapsedMinutes;
    long elapsedHours;
    @NonNull
    @Override
    public ImmobilizeAdapetr.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.immobilize_adapter, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImmobilizeAdapetr.MyHolder holder, final int position)
    {
        holder.tvVehName.setText(list.get(position).getVehicleName());
        holder.tvLocation.setText(list.get(position).getLocation());
        if (list.get(position).getIsImmobilizeActive().equals("1"))
        {
            holder.tvNotInstalled.setVisibility(View.GONE);
            holder.switch1.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tvNotInstalled.setText("Not Installed");
         //   holder.tvNotInstalled.setText("minutes = "+elapsedMinutes+" , "+" hours ="+elapsedHours);
            holder.tvNotInstalled.setVisibility(View.VISIBLE);
            holder.switch1.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);
        }
        if (list.get(position).getImmobilize().equals("3")||list.get(position).getImmobilize().equals("5"))
        {
            holder.switch1.setChecked(true);
        }
        if (list.get(position).getImmobilize().equals("2")||list.get(position).getImmobilize().equals("4"))
        {
            holder.switch1.setChecked(false);
        }
        if (list.get(position).getImmobilize().equals("0")||list.get(position).getImmobilize().equals("1"))
        {
            holder.switch1.setVisibility(View.GONE);
            holder.tvNotInstalled.setVisibility(View.VISIBLE);
            holder.tvNotInstalled.setText("Waiting for device");
        }
        holder.tvStatus.setText(list.get(position).getImmobilisze());
        Calendar c = Calendar.getInstance();
      //  c.add(Calendar.MINUTE, 15);
        SimpleDateFormat df2 = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        String formattedDate2 = df2.format(c.getTime());
        Log.e("DateCurrent",formattedDate2);
        if(validateFromDate(formattedDate2,list.get(position).getLastdate().replace("T"," ")))
        {
            holder.tvVehName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.moving, 0);
            Log.e("Green","check");
        }
        else {
            holder.tvVehName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unreachable, 0);
            Log.e("RED","check");
        }
    }

    private boolean validateFromDate(String start,String End){
        String startDataTime = start;
        String endDateTime = End;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        try {
            Date startDT = simpleDateFormat.parse(startDataTime);
            Log.e("endDateTime",endDateTime);
            Date endDT = simpleDateFormat.parse(endDateTime);

            long diffInMs = startDT.getTime() - endDT.getTime();
            long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMs);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            elapsedHours = diffInMs / hoursInMilli;
            diffInMs = diffInMs % hoursInMilli;

            elapsedMinutes = diffInMs / minutesInMilli;
            diffInMs = diffInMs % minutesInMilli;

            long elapsedSeconds = diffInMs / secondsInMilli;

            Log.e("elapsedMinutes", String.valueOf(elapsedMinutes));
            Log.e("elapsedHours", String.valueOf(elapsedHours));
            if(elapsedMinutes < 15 && elapsedHours<24) {
                Log.e("status","true");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("status","false");
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvVehName,tvLocation,tvNotInstalled,tvStatus;
        private SwitchCompat switch1;
        public MyHolder(@NonNull View itemView)
        {
            super(itemView);
            switch1 = itemView.findViewById(R.id.switch1);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvVehName = itemView.findViewById(R.id.tvVehName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNotInstalled = itemView.findViewById(R.id.tvNotInstalled);
            switch1.setOnClickListener(view -> {
                if(((SwitchCompat)view).isChecked())
                {
                    myClicklistener.onSwitchClick(getAdapterPosition(),view);
                }
                else
                {
                    myClicklistener.onSwitchClick(getAdapterPosition(),view);
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
