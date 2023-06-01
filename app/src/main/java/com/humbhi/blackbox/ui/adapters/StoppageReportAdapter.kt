package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.StoppageReportAdapterBinding
import com.humbhi.blackbox.ui.data.models.StoppageData

class StoppageReportAdapter(val context: Context, val listData: List<StoppageData>) :
    RecyclerView.Adapter<StoppageReportAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, list: List<StoppageData>) {
            StoppageReportAdapterBinding.bind(itemView).apply {
                tvVehicleNumber.text = list.get(adapterPosition).VehicleName
                tvStopCount.text = list.get(adapterPosition).StoppageCount.toString()
                tvStopTime.text =
                    list.get(adapterPosition).TotalStoppageTime.replace("day(s)", "Days")
                        .replace("hour(s)", "Hr").replace("minute(s)", " Min")
                        .replace("Seconds(s)", " Sec").replace("second(s)", " Sec")
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stoppage_report_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context, listData)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listData.size
    }
}