package com.humbhi.blackbox.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FuelFillingDetailAdapterBinding
import com.humbhi.blackbox.databinding.FueltheftdetailadapterBinding
import com.humbhi.blackbox.ui.data.models.FuelData
import com.humbhi.blackbox.ui.data.models.FuelFillingData
import com.humbhi.blackbox.ui.data.models.FuelFillingResponseData
import com.humbhi.blackbox.ui.data.models.FueltheftMainModel

class FuelFillingDetailAdapter(val list: List<FuelData>) :
    RecyclerView.Adapter<FuelFillingDetailAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(list: List<FuelData>) {
            FuelFillingDetailAdapterBinding.bind(itemView).apply {
                tvVehicleName.text = "Total Filling"
                tvTotalDrain.text = list[adapterPosition].DifferenceFuelLevel+" Ltr."
                tvFirstFilling.text = list[adapterPosition].FirstFilling.toString() + " Ltr."
                tvLastFilling.text = list[adapterPosition].LastFiliing.toString() + " Ltr."
                tvDrainLocation.text = list[adapterPosition].StartLocation
                tvBeforeDrainDate.text = list[adapterPosition].StartDateTime
                tvAfterFilling.text = list[adapterPosition].EndateTime
                gGauge.speedTo(100F)
                gGauge.maxSpeed = (list[adapterPosition].StartFuelLevel + list[adapterPosition].EndFuelLevel).toFloat()
                gGauge.speedTextSize = 0F
                gGauge.unitTextSize = 0F
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fuel_filling_detail_adapter, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(list)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }
}