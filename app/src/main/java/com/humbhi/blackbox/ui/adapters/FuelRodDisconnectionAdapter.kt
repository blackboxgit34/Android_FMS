package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FuelRodDisconnectionAdapterBinding
import com.humbhi.blackbox.ui.data.models.DisconnectionData

class FuelRodDisconnectionAdapter(val context: Context,val list: List<DisconnectionData>) :
    RecyclerView.Adapter<FuelRodDisconnectionAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(context: Context,list: List<DisconnectionData>) {
            FuelRodDisconnectionAdapterBinding.bind(itemView).apply {
                val garbage = Html.fromHtml(Html.fromHtml(list[adapterPosition].GarbageCount).toString()).toString()
                val disconnection = Html.fromHtml(Html.fromHtml(list[adapterPosition].DisconnectionCount).toString()).toString()
                val disconnectionCount = disconnection.toInt()
                val garbageCount = garbage.toInt()
                if(garbageCount+disconnectionCount>0){
                    tvVehicleName.setTextColor(context.resources.getColor(R.color.primary_orange))
                }
                else{
                    tvVehicleName.setTextColor(context.resources.getColor(R.color.white))
                }
                tvVehicleName.text = list[adapterPosition].VehicleName
                tvDriverName.text = list[adapterPosition].DriverName
                tvDisconnectionCount.text = Html.fromHtml(Html.fromHtml(list[adapterPosition].DisconnectionCount).toString())
                tvDirtCount.text = Html.fromHtml(Html.fromHtml(list[adapterPosition].GarbageCount).toString())
                tvDisconnectionDuration.text = list[adapterPosition].DisconnectionDuration
                tvDirtDuration.text = list[adapterPosition].GarbageDuration
            }


        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fuel_rod_disconnection_adapter, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(context,list)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

}