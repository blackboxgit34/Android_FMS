package com.humbhi.blackbox.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.OverspeedAdapterBinding
import com.humbhi.blackbox.ui.data.models.Overspeed
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.overSpeedDetail.OverspeedDetailActivity
import com.humbhi.blackbox.ui.utils.ExplicitIntentUtil

class OverspeedAdapter(val context: Context, private val vehicleSelection: OverSpeedDetails, val overspeed: List<Overspeed>) :
    RecyclerView.Adapter<OverspeedAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind( context: Context,vehicleSelection: OverSpeedDetails, overspeed: List<Overspeed>) {
            OverspeedAdapterBinding.bind(itemView).apply {
                tvVehNum.text = overspeed[adapterPosition].vehname
                tvOverspeedCount.text = overspeed[adapterPosition].overspeedCount.toString()
                tvoverseedLimit.text = overspeed[adapterPosition].overspeedLimit.toString()
                tvMaxSpeed.text = overspeed[adapterPosition].maxSpeed.toString()
                tvDuration.text = overspeed.get(adapterPosition).overSpeedDuration.replace("Hour(s)", "Hr.").replace("Minute(s)", " Min.")
                    .replace("Second(s)", " Sec.").replace("Second(s)", " S                                                                                                                                                    ")

                pvOStimes.setProgress(overspeed[adapterPosition].overspeedCount,true)
                if (overspeed[adapterPosition].overSpeedData.size > 0) {
                    tvDetails.visibility = View.VISIBLE
                    tvDetails.setOnClickListener {
                        vehicleSelection.onVehicleSelection(adapterPosition)
                    }
                } else {
                    tvDetails.visibility = View.GONE
                }
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.overspeed_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context,vehicleSelection ,overspeed)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return overspeed.size
    }

    interface OverSpeedDetails{
        fun onVehicleSelection(position:Int)
    }

}