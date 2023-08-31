package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.VehicleStatusAdapterBinding
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusDataItem

class VehicleStatusAdapter(val context: Context,private val vehicleSelection:VehicleDetails,
private val listData:List<VehicleLiveStatusDataItem>) : RecyclerView.Adapter<VehicleStatusAdapter.ViewHolder>()
{
    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(context:Context,vehicleSelection:VehicleDetails,list:List<VehicleLiveStatusDataItem>){

            VehicleStatusAdapterBinding.bind(itemView).apply {
                tvVehicleName.text = list.get(adapterPosition).VehicleName
                if(list[adapterPosition].LocationWithoutLink == null){
                    tvLocation.text = "No location available"
                }
                else {
                    tvLocation.text = list[adapterPosition].LocationWithoutLink
                }
                tvDistanceSpeed.text =list.get(adapterPosition).Speed.toString()+" km/h"
                tvDistance.text = list.get(adapterPosition).Distance.toString()+"KM"
                val progressStatus = list.get(adapterPosition).ProgressStatus
                if(progressStatus.equals("BD")){
                    tvStatus.text = "Battery Disconnection"
                }
                else{
                    tvStatus.text = progressStatus
                }
                if (progressStatus == "Stopped"){
                    tvStatus.setBackgroundResource(R.drawable.round_bg_yellow)
                    statusShadow.setBackgroundResource(R.drawable.yellow_shadow)
                    ivIgnition.setImageResource(R.drawable.ic_ignition_off)
                }
                if (progressStatus == "IgnitionOn"){
                    tvStatus.background = ContextCompat.getDrawable(context, R.drawable.round_background)
                    ivIgnition.setImageResource(R.drawable.ic_ignition_on)
                    statusShadow.setBackgroundResource(R.drawable.shadow_orange)
                }
                if (progressStatus == "Moving"){
                    tvStatus.background = ContextCompat.getDrawable(context, R.drawable.round_bg_green)
                    ivIgnition.setImageResource(R.drawable.ic_ignition_on)
                    statusShadow.setBackgroundResource(R.drawable.shadow_green)
                }
                if (progressStatus == "Unreachable"){
                    tvStatus.background = ContextCompat.getDrawable(context, R.drawable.round_bg_red)
                    statusShadow.setBackgroundResource(R.drawable.shadow_red)
                    ivIgnition.setImageResource(R.drawable.ic_ignition_off)
                }
                if (progressStatus == "BD"){
                    tvStatus.background = ContextCompat.getDrawable(context, R.drawable.round_bg_red)
                    statusShadow.setBackgroundResource(R.drawable.shadow_red)
                    ivIgnition.setImageResource(R.drawable.ic_ignition_off)
                }
                if (progressStatus == "Hispeed"){
                    tvStatus.background = ContextCompat.getDrawable(context, R.drawable.round_bg_blue)
                    ivIgnition.setImageResource(R.drawable.ic_ignition_on)
                    statusShadow.setBackgroundResource(R.drawable.shadow_blue)
                }
                if (progressStatus == "Towed"){
                    tvStatus.background = ContextCompat.getDrawable(context, R.drawable.round_bg_creame)
                    ivIgnition.setImageResource(R.drawable.ic_ignition_off)
                    statusShadow.setBackgroundResource(R.drawable.shadow_white)
                }
                tvDateTime.text = list.get(adapterPosition).DateTime

                val vehIcon = list.get(adapterPosition).VIconName
                Log.e("vehIcon",vehIcon)
                if (vehIcon=="Tempo"){
                    when(progressStatus)
                    {
                        "Unreachable"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_red_side_view)
                        }
                        "BD"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_red_side_view)
                        }
                        "Moving"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_green_side_view)
                        }
                        "IgnitionOn"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_orange_side_view)
                        }
                        "Stopped"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_yellow_side_view)
                        }
                        "Hispeed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_blue_side_view)
                        }
                        "Towed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_white_side_view)
                        }
                    }
                }
                else if (vehIcon=="Tata Ace"){
                    when(progressStatus)
                    {
                        "Unreachable"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_red_side_view)
                        }
                        "BD"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_red_side_view)
                        }
                        "Moving"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_green_side_view)
                        }
                        "IgnitionOn"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_orange_side_view)
                        }
                        "Stopped"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_yellow_side_view)
                        }
                        "Hispeed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_blue_side_view)
                        }
                        "Towed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_tempo_white_side_view)
                        }
                    }
                }
                else if (vehIcon=="Other"){
                    when(progressStatus)
                    {
                        "Unreachable"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_red_side_view)
                        }
                        "BD"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_red_side_view)
                        }
                        "Moving"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_green_side_view)
                        }
                        "IgnitionOn"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_orange_side_view)
                        }
                        "Stopped"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_yellow_side_view)
                        }
                        "Hispeed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_blue_side_view)
                        }
                        "Towed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_white_side_view)
                        }
                    }
                }
                else if (vehIcon=="Trucker"){
                    ivVehicleIcon.setImageResource(R.drawable.truck_icon)
                }

                else if (vehIcon=="Car"){
                    when(progressStatus){
                        "Unreachable"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_red_car_side_view)
                        }
                        "BD"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_red_car_side_view)
                        }
                        "Moving"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_green_car_side_view)
                        }
                        "IgnitionOn"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_orange_car_side_view)
                        }
                        "Stopped"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_yellow_car_side_view)
                        }
                        "Hispeed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_blue_car_side_view)
                        }
                        "Towed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_white_car_side_view)
                        }
                    }

                }
                else if (vehIcon=="Truck"){
                    when(progressStatus)
                    {
                        "Unreachable"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_red_side_view)
                        }
                        "BD"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_red_side_view)
                        }
                        "Moving"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_green_side_view)
                        }
                        "IgnitionOn"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_orange_side_view)
                        }
                        "Stopped"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_yellow_side_view)
                        }
                        "Hispeed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_blue_side_view)
                        }
                        "Towed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_white_side_view)
                        }
                    }
                }

                else if (vehIcon=="Bus"){
                    ivVehicleIcon.setImageResource(R.drawable.bus)
                }

                else if (vehIcon=="OilTanker"){
                    ivVehicleIcon.setImageResource(R.drawable.oil_tanker_side)
                }
                else if (vehIcon=="RoadRoller"){
                    when(progressStatus)
                    {
                        "Unreachable"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_red_car_side_view)
                        }
                        "BD"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_red_car_side_view)
                        }
                        "Moving"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_green_car_side_view)
                        }
                        "IgnitionOn"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_orange_car_side_view)
                        }
                        "Stopped"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_yellow_car_side_view)
                        }
                        "Hispeed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_blue_car_side_view)
                        }
                        "Towed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_white_car_side_view)
                        }
                    }
                }
                else{
                    when(progressStatus) {
                        "Unreachable"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_red_side_view)
                        }
                        "BD"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_red_side_view)
                        }
                        "Moving"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_green_side_view)
                        }
                        "IgnitionOn"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_orange_side_view)
                        }
                        "Stopped"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_yellow_side_view)
                        }
                        "Hispeed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_blue_side_view)
                        }
                        "Towed"->{
                            ivVehicleIcon.setImageResource(R.drawable.ic_truck_white_side_view)
                        }
                    }
                }

                val acStatus = list.get(adapterPosition).ACTooltip
                val batteryStatus = list.get(adapterPosition).batteryTooltip
                val fuelStatus = list.get(adapterPosition).FuelRodStatus
                if (acStatus == "AC Off")
                {
                    ivAcStatus.setImageResource(R.drawable.ac_off)
                }
                else
                {
                    ivAcStatus.setImageResource(R.drawable.ic_ac)
                }
                if (batteryStatus == "VehBattery Normal"){
                    ivBatteryStatus.setImageResource(R.drawable.ic_battery_green)
                }
                else
                {
                    ivBatteryStatus.setImageResource(R.drawable.ic_battery_low)
                }
                if(fuelStatus==null){
                    ivFuelStatus.visibility = View.GONE
                }
                cvMainLayout.setOnClickListener {
                    vehicleSelection.onVehicleSelection(adapterPosition)
                }
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_status_adapter, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicleSelection:VehicleDetails = vehicleSelection
        holder.bind(context,vehicleSelection,listData)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listData.size
    }

    interface VehicleDetails{
        fun onVehicleSelection(position:Int)
    }
}