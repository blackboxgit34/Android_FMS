package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.NoDrivingAdapterBinding
import com.humbhi.blackbox.ui.data.models.AaDataXX


class NoDrivingAdapter(val context: Context, val listData: List<AaDataXX>) :
    RecyclerView.Adapter<NoDrivingAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, list: List<AaDataXX>) {
            NoDrivingAdapterBinding.bind(itemView).apply {
                tvVehNum.text = list[adapterPosition].VehicleName
                tvDrivingLimit.text = list[adapterPosition].noDrvHr
                tvDrivingCount.text = list[adapterPosition].count.toString()
                tvDrivingDuration.text = list[adapterPosition].totDrvHr
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.no_driving_adapter, parent, false)

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