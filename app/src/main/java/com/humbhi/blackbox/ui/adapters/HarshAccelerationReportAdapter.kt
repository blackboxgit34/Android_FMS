package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.HarshAccReportAdapterBinding
import com.humbhi.blackbox.ui.data.models.HarshAccelerationData

class HarshAccelerationReportAdapter(
    val context: Context,
    val listData: List<HarshAccelerationData>
) :
    RecyclerView.Adapter<HarshAccelerationReportAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, listData: List<HarshAccelerationData>) {
            HarshAccReportAdapterBinding.bind(itemView).apply {
                tvCount.text = listData[adapterPosition].count.toString()
                tvVehicleName.text = listData[adapterPosition].VehicleName
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.harsh_acc_report_adapter, parent, false)

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