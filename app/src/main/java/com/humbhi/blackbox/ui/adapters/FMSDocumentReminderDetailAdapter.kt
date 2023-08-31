package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FmsVehicleListDetailsBinding
import com.humbhi.blackbox.databinding.FmsVehiclesListBinding
import com.humbhi.blackbox.ui.data.models.AaDataXXX
import com.humbhi.blackbox.ui.data.models.ReminderData

class FMSDocumentReminderDetailAdapter (val context: Context, private val vehicleSelection: VehicleDetailsUpdate, val list:List<ReminderData>) :
    RecyclerView.Adapter<FMSDocumentReminderDetailAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, vehicleSelection: VehicleDetailsUpdate, list: List<ReminderData>) {
            FmsVehicleListDetailsBinding.bind(itemView).apply {
                tvRenewalType.text = list[adapterPosition].RenewalType
                tvStatus.text = list[adapterPosition].status
                tvRemarks.text = list[adapterPosition].Remarks
                tvDueDate.text = list[adapterPosition].DueDate
                ivAction.setOnClickListener {
                    vehicleSelection.onVehicleSelection(adapterPosition)
                }
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fms_vehicle_list_details, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context,vehicleSelection,list)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    interface VehicleDetailsUpdate{
        fun onVehicleSelection(position:Int)
    }
}