package com.humbhi.blackbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.WorkingHourAdapterBinding
import com.humbhi.blackbox.ui.data.models.WorkingHourData

class WorkingHourReportAdapter(
    private val vehicleSelection: WorkingIgnitionDetails,
    val list: List<WorkingHourData>
) : RecyclerView.Adapter<WorkingHourReportAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(vehicleSelection: WorkingIgnitionDetails, list: List<WorkingHourData>) {
            WorkingHourAdapterBinding.bind(itemView).apply {
                tvViewDetails.setOnClickListener {

                    vehicleSelection.onVehicleSelection(adapterPosition)

                }

                tvVehicleName.text = list[adapterPosition].VehicleName
                tvIgnitionTimes.text = list[adapterPosition].IgnitionOnOffCounter.toString()+" Times"
                tvWorkHours.text = list[adapterPosition].TotalWorkingHours.replace("day(s)", "Days")
                    .replace("hour(s)", "Hr").replace("minute(s)", " Min")

                if(list[adapterPosition].objIgnitionStatusReport.size>0){
                    tvViewDetails.visibility = View.VISIBLE
                }
                else{
                    tvViewDetails.visibility = View.GONE
                }

            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.working_hour_adapter, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicleSelection: WorkingIgnitionDetails = vehicleSelection
        holder.bind(vehicleSelection, list)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }


    interface WorkingIgnitionDetails {
        fun onVehicleSelection(position: Int)
    }

}