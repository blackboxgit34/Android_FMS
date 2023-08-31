package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ManageGeofenceAdapterBinding
import com.humbhi.blackbox.ui.data.models.FenceData

class ManageGeofenceAdapter(val context: Context, val list : ArrayList<FenceData>) : RecyclerView.Adapter<ManageGeofenceAdapter.MyHolder>() {
    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list: ArrayList<FenceData>) {
            ManageGeofenceAdapterBinding.bind(itemView).apply {
                fenceName.text = list[adapterPosition].FenceName
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.manage_geofence_adapter, parent, false)
        return MyHolder(view)
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context,list)
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }
}