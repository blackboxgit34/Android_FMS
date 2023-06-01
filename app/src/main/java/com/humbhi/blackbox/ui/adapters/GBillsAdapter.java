package com.humbhi.blackbox.ui.adapters;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.models.NewBillsModel;
import com.humbhi.blackbox.ui.utils.DateFormatter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GBillsAdapter extends RecyclerView.Adapter<GBillsAdapter.MyHolder> {

    Context context;
    ArrayList<NewBillsModel> list;
    String billPeriod;
    MyClickListener myClicklistener;

    public GBillsAdapter(Context context, ArrayList<NewBillsModel>list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GBillsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bills_adapter, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GBillsAdapter.MyHolder holder, final int position) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (!TextUtils.isEmpty(list.get(position).getBillperiod())) {
            String[] str_array = list.get(position).getBillperiod().split("TO");
            String stringa = str_array[0];
            String stringb = str_array[1];

            try {
                Date d = dateFormat.parse(stringa);
                long milli = d.getTime();

                stringa = DateFormatter.getCurrentDateTime(DateFormatter.SHORT_DATE_FORMAT_1, milli, null, DateFormatter.TimeZoneFormat.NONE);

            } catch (ParseException ex) {
            }

            try {
                Date d = dateFormat.parse(stringb);
                long milli = d.getTime();

                stringb = DateFormatter.getCurrentDateTime(DateFormatter.SHORT_DATE_FORMAT_1, milli, null, DateFormatter.TimeZoneFormat.NONE);

            } catch (ParseException ex) {
            }
            holder.tvDate.setText(stringa + " To " + stringb);
        }
        holder.tvBillNo.setText(list.get(position).getBillno());
        holder.tvBoxcount.setText(list.get(position).getBoxCount());

        if (!TextUtils.isEmpty(list.get(position).getBillperiod())) {

            SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date myDate = dateParser.parse(list.get(position).getFromDate());
                Calendar c = Calendar.getInstance();
                c.setTime(myDate);
                c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 14);
                Date newDate = c.getTime();
                String newFormattedDate = dateParser.format(newDate); //01/21/2015

                Date d = dateParser.parse(newFormattedDate);
                long milli = d.getTime();

                String resultformat = DateFormatter.getCurrentDateTime(DateFormatter.SHORT_DATE_FORMAT_1, milli, null, DateFormatter.TimeZoneFormat.NONE);

                holder.tvDueDate.setText("" + resultformat); // parse input

            } catch (ParseException e) {
                e.printStackTrace();
                //handle exception

            }
            //     holder.mTxtDueDate.setText(": "+ dueDate);
        } else {
            holder.tvDueDate.setText("--");
        }

        holder.tvCurrentBal.setText("₹ "+list.get(position).getCurrentBalance());
        holder.tvOtherCharge.setText("₹ "+list.get(position).getOtherCharges());
        holder.tvDiscount.setText("₹ "+list.get(position).getDiscount());
        holder.tvPreviousBalnce.setText("₹ "+list.get(position).getPreviousBalance());
        holder.tvtotal.setText("₹ "+list.get(position).getTotalBalance());
        if (list.get(position).getType().equals("old"))
        {
            holder.llDueDate.setVisibility(View.GONE);
            holder.tvPayNow.setVisibility(View.GONE);
        }
        else
        {
            holder.llDueDate.setVisibility(View.VISIBLE);
            holder.tvPayNow.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView tvDate,tvBillNo,tvBoxcount,tvDueDate,tvCurrentBal,tvOtherCharge,tvDiscount,tvPreviousBalnce,tvtotal,tvPayNow;
        LinearLayout llDueDate;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBillNo = itemView.findViewById(R.id.tvBillNo);
            tvBoxcount = itemView.findViewById(R.id.tvBoxcount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvCurrentBal = itemView.findViewById(R.id.tvCurrentBal);
            tvOtherCharge = itemView.findViewById(R.id.tvOtherCharge);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvPreviousBalnce = itemView.findViewById(R.id.tvPreviousBalnce);
            tvtotal = itemView.findViewById(R.id.tvtotal);
            llDueDate = itemView.findViewById(R.id.llDueDate);
            tvPayNow = itemView.findViewById(R.id.tvPayNow);



            tvPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    myClicklistener.onPayClick(getAdapterPosition(),view);
                }
            });
        }
    }

    public interface MyClickListener

    {
        void onPayClick(int poss, View view1);
    }
    public void onItemSelectedListener(MyClickListener clickListener)
    {
        myClicklistener = clickListener;
    }
}
