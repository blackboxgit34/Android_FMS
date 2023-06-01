package com.humbhi.blackbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.AisVehicleAdapterBinding
import com.humbhi.blackbox.databinding.DailyreportAdapterBinding

class AIS140Adapter : RecyclerView.Adapter<AIS140Adapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            AisVehicleAdapterBinding.bind(itemView).apply {

            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ais_vehicle_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return 8
    }
}