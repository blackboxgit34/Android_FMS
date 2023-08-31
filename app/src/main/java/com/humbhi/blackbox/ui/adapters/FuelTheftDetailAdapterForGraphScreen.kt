package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FuelFillingDetailLayoutBinding
import com.humbhi.blackbox.ui.data.models.FuelData
import com.humbhi.blackbox.ui.data.models.FueltheftMainModel


class FuelTheftDetailAdapterForGraphScreen(val list:List<FueltheftMainModel>, val context: Context) : RecyclerView.Adapter<FuelTheftDetailAdapterForGraphScreen.ViewHolder>() {

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(list: List<FueltheftMainModel>, context: Context) {
            FuelFillingDetailLayoutBinding.bind(itemView).apply {
                fuelLevelText.text = "Fuel Drain"
                tvStartTime.text = "Before Drain Time"
                tvEndTime.text = "After Drain Time"
                tvFillingFuelLevel.text = list[absoluteAdapterPosition].DifferenceFuelLevel+ " L"
                tvstartFuel.text = "Before Drain Level"
                fillingStartTime.text = list[adapterPosition].StartDateTime
                Beforefueling.text = list[adapterPosition].StartFuelLevel+ " L"
                fillingEndTime.text = list[adapterPosition].EndateTime
                afterFueling.text = list[adapterPosition].EndFuelLevel+ " L"
                tvEndFuel.text = "After Drain Level"
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fuel_filling_detail_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list,context)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

}