package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FuelFillingAdapterBinding
import com.humbhi.blackbox.ui.data.models.FuelFillingData
import com.humbhi.blackbox.ui.data.models.FuelFillingResponseData
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelFillinReportActivity
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelGraphJavaActivity
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelTheftReportActivity


class FuelFillingAdapter(private val vehicleSelection: VehicleDetails, val list:List<FuelFillingData>, val context: Context) : RecyclerView.Adapter<FuelFillingAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(vehicleSelection:VehicleDetails, list:List<FuelFillingData>,context: Context){
            FuelFillingAdapterBinding.bind(itemView).apply {
                tvDetails.setOnClickListener {
                    vehicleSelection.onVehicleSelection(adapterPosition)
                }

                tvVehicleName.text = list[adapterPosition].VehicleName
                tvFillingCount.text = list[adapterPosition].objCount.toString()
                tvFillingQuantity.text = list[adapterPosition].TotalFilling.toString()+" Ltr."
                if (list[adapterPosition].objCount>0){
                    tvDetails.visibility = View.VISIBLE
                    ivGraph.visibility = View.VISIBLE
                }
                else{
                    tvDetails.visibility = View.GONE
                    ivGraph.visibility = View.GONE
                }
                ivGraph.setOnClickListener{
                    val intent = Intent(context, FuelGraphJavaActivity::class.java)
                    intent.putExtra("vehicleId",list[adapterPosition].bbid)
                    intent.putExtra("fromNavigate","filling fuel")
                    intent.putExtra("fuelFillingLevel",list[adapterPosition].FuelFillingMainModel[0].DifferenceFuelLevel)
                    intent.putExtra("startFillingTime",list[adapterPosition].FuelFillingMainModel[0].StartDateTime)
                    intent.putExtra("endFillingTime",list[adapterPosition].FuelFillingMainModel[0].EndateTime)
                    intent.putExtra("startDate",list[adapterPosition].FuelFillingMainModel[0].StartDateTime)
                    intent.putExtra("endDate",list[adapterPosition].FuelFillingMainModel[0].EndateTime)
                    context.startActivity(intent)
                    (context as FuelFillinReportActivity).finish()
                }
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fuel_filling_adapter, parent, false)

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