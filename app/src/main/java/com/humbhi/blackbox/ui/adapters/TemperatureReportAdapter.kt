package com.humbhi.blackbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.TempReportAdapterBinding
import com.humbhi.blackbox.ui.data.models.FuelFillingData
import com.humbhi.blackbox.ui.data.models.TemperatureReportResponse
import com.humbhi.blackbox.ui.data.models.TempratureData
import com.humbhi.blackbox.ui.ui.addonReports.temperature.TemperatureSensorReport

class TemperatureReportAdapter (private val vehicleSelection:TemperatureDetails,val list:List<TempratureData>) : RecyclerView.Adapter<TemperatureReportAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(vehicleSelection:TemperatureDetails, list:List<TempratureData>){
            TempReportAdapterBinding.bind(itemView).apply {
                ivDetails.setOnClickListener {
                    vehicleSelection.onVehicleSelection(adapterPosition)

                }
                tvVehicleName.text = list[adapterPosition].VehicleName
                tvCurrentTemp.text = list[adapterPosition].CurrentTemperature+"°C"
                tvMaxTemp.text = list[adapterPosition].HighestTemperature+"°C"
                tvMinTemp.text = list[adapterPosition].LowestTemperature+"°C"
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.temp_report_adapter, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicleSelection:TemperatureDetails = vehicleSelection
        holder.bind(vehicleSelection,list)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }



    interface TemperatureDetails{
        fun onVehicleSelection(position:Int)
    }

}