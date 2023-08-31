package com.humbhi.blackbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.StoppageReportAdapterBinding
import com.humbhi.blackbox.ui.data.models.AaDataXXXXX
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlin.properties.Delegates

class StoppageReportAdapter(
    private val vehicleSelection:VehicleDetailsUpdate,
    val listData: List<AaDataXXXXX>
) : RecyclerView.Adapter<StoppageReportAdapter.MyHolder>() {
    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vehicleSelection:VehicleDetailsUpdate, list: List<AaDataXXXXX>) {
            StoppageReportAdapterBinding.bind(itemView).apply {
                val item = list[position]
                tvVehicleNumber.text = item.VehicleName
                tvStopCount.text = item.StoppageCount.toString()
                tvStopTime.text = item.TotalStoppageTime.replace("day(s)", "Days")
                        .replace("hour(s)", "Hr").replace("minute(s)", " Min")
                        .replace("Seconds(s)", " Sec").replace("second(s)", " Sec")
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stoppage_report_adapter, parent, false)
        return MyHolder(view)
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(vehicleSelection,listData)
        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                vehicleSelection.onVehicleSelection(position)
            }
        }
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listData.size
    }
    interface VehicleDetailsUpdate{
        fun onVehicleSelection(position:Int)
    }

}