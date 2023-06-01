package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FuelTheftAdapterBinding
import com.humbhi.blackbox.ui.data.models.FuelFillingData
import com.humbhi.blackbox.ui.data.models.Theftdata
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelGraphJavaActivity
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelTheftReportActivity

class FuelTheftAdapter (private val vehicleSelection:VehicleDetails,val list:List<Theftdata>, val context: Context) : RecyclerView.Adapter<FuelTheftAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(vehicleSelection:VehicleDetails,list: List<Theftdata>,context: Context){
            FuelTheftAdapterBinding.bind(itemView).apply {
                tvDetails.setOnClickListener {
                    vehicleSelection.onVehicleSelection(adapterPosition)
                }
                tvVehicleName.text = list[adapterPosition].getVehicleNam()
                tvFuelDranangeCount.text = list[adapterPosition].objCount.toString()
                tvTotalDrianage.text = list[adapterPosition].TotalTheft.toString()+" Ltr."

                if (list[adapterPosition].objCount>0){
                    tvDetails.visibility = View.VISIBLE
                    ivGraph.visibility = View.VISIBLE
                }
                else{
                    tvDetails.visibility = View.GONE
                    ivGraph.visibility = View.GONE
                }
                ivGraph.setOnClickListener{
                   val intent = Intent(context,FuelGraphJavaActivity::class.java)
                   intent.putExtra("vehicleId",list[adapterPosition].bbid)
                    intent.putExtra("startDate",list[adapterPosition].FueltheftMainModel[0].StartDateTime)
                    intent.putExtra("endDate",list[adapterPosition].FueltheftMainModel[0].EndateTime)
                   context.startActivity(intent)
                    (context as FuelTheftReportActivity).finish()
                }
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fuel_theft_adapter, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicleSelection:VehicleDetails = vehicleSelection
        holder.bind(vehicleSelection,list,context)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }



    interface VehicleDetails{
        fun onVehicleSelection(position:Int)
    }

}