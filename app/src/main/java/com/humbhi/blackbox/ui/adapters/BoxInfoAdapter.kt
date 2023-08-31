package com.humbhi.blackbox.ui.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ChnageVehicleTypeLayoutBinding
import com.humbhi.blackbox.ui.Utility.areDatesValidAndFirstIsLess
import com.humbhi.blackbox.ui.data.models.SettingsData
import java.util.Locale

class BoxInfoAdapter (val context: Context,val list:List<SettingsData>) :
    RecyclerView.Adapter<BoxInfoAdapter.MyHolder>() {
    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list: List<SettingsData>) {
            ChnageVehicleTypeLayoutBinding.bind(itemView).apply {
                vehName.text = list[adapterPosition].VehicleName
                val typeId = list[adapterPosition].TypeId.toInt()
                val vehicleType = list[adapterPosition].VehicleTypeList.find { it.Id == typeId }
                if (vehicleType != null) {
                    val vehicleName = vehicleType.Name
                    vehType.text = vehicleName
                }
                InstllationDate.text = list[adapterPosition].InstallationDate
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chnage_vehicle_type_layout, parent, false)

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