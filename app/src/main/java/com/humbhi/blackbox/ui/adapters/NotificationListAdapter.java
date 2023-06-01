package com.humbhi.blackbox.ui.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.models.NotificationModel;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyHolder> {

    Context context;
    ArrayList<NotificationModel>list;
    MyClickListener myClicklistener;

    public NotificationListAdapter(Context context, ArrayList<NotificationModel> list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public NotificationListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_adapter
                , viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.MyHolder holder, final int position) {

        if(list.get(position).getNotificationRead().equals("Read")){
            holder.llNotification.setBackgroundColor(context.getResources().getColor(R.color.primary_little_fade));
        }
        else{
            holder.llNotification.setBackgroundColor(context.getResources().getColor(R.color.light_black2));
        }
        holder.tvDate.setText("Date: "+list.get(position).getNotificationDate());
        holder.tvMessage.setText(list.get(position).getNotificationMsg());

        switch (Integer.parseInt(list.get(position).getTypeId()))
        {
            case 1:
                holder.tvType.setText("Geofence");
                holder.ivCircleNotation.setImageResource(R.drawable.ic_yellow_circle);
                holder.ivNotificationIcon.setImageResource(R.drawable.satelite);
                break;
            case 2:
                holder.tvType.setText("Overspeed");
                holder.ivCircleNotation.setImageResource(R.drawable.ic_circle_red);
                holder.ivNotificationIcon.setImageResource(R.drawable.fire_speed_icon);

                break;
            case 9:
                holder.tvType.setText("Main Battery Disconnection");
                holder.ivCircleNotation.setImageResource(R.drawable.ic_blue_circle);
                holder.ivNotificationIcon.setImageResource(R.drawable.ic_battery_notification);
                break;
            case 68:
                holder.tvType.setText("OverStoppage");
                holder.ivCircleNotation.setImageResource(R.drawable.ic_yellow_circle);
                holder.ivNotificationIcon.setImageResource(R.drawable.ic_alert_overstoppage);
                break;
            case 70:
                holder.tvType.setText("IgnitionOn");
                holder.ivCircleNotation.setImageResource(R.drawable.ic_circle_orange);
                holder.ivNotificationIcon.setImageResource(R.drawable.ic_alert_ignitionon);
                break;

            case 78:
                holder.tvType.setText("Service Request(Complaint)");
                holder.ivNotificationIcon.setImageResource(R.drawable.ic_customer);
                break;

            case 99:
                holder.tvType.setText("Vehicle Service Reminder");

                break;

            case 100:
                holder.tvType.setText("Vehicle Document Reminder");
                break;

            case 101:
                holder.tvType.setText("Employee Contract Over Reminder");
                break;

            case 102:
                holder.tvType.setText("Driver Doc Renewal Reminder");
                break;

            case 126:
                holder.tvType.setText("Fuel Rod Disconnection");
                holder.ivCircleNotation.setImageResource(R.drawable.ic_green_circle);
                holder.ivNotificationIcon.setImageResource(R.drawable.ic_fuel_notification);
                break;
        }

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        LinearLayoutCompat llNotification;
        private TextView tvType,tvDate,tvMessage;
        ImageView ivDelete,ivNotificationIcon,ivCircleNotation,ivShare,ivNotification;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivNotificationIcon = itemView.findViewById(R.id.ivNotificationIcon);
            ivCircleNotation = itemView.findViewById(R.id.ivCircleNotation);
            ivShare = itemView.findViewById(R.id.ivShare);
            llNotification = itemView.findViewById(R.id.llSelectNotification);
            ivNotification = itemView.findViewById(R.id.mark);
            llNotification.setOnClickListener(v->{
                if(list.get(getAdapterPosition()).getNotificationRead().equals("UnRead")) {
                    if(ivNotification.getVisibility()==View.VISIBLE){

                        ivNotification.setVisibility(View.GONE);
                    }
                    else{
                        ivNotification.setVisibility(View.VISIBLE);
                    }
                    myClicklistener.onSelect(getAdapterPosition(), v);
                }
            });
            ivDelete.setOnClickListener(view -> myClicklistener.onSwitchClick(getAdapterPosition(),view));
            ivShare.setOnClickListener(v -> {
                myClicklistener.onShare(getAdapterPosition(),v);
            });
        }
    }

    public interface MyClickListener
    {
        void onSwitchClick(int poss, View view1);
        void onShare(int poss,View view2);
        void onSelect(int poss, View view3);
    }
    public void onItemSelectedListener(MyClickListener clickListener)
    {
        myClicklistener = clickListener;
    }

}
