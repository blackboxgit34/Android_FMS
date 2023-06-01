package com.humbhi.blackbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.AllIconsAdapterBinding
import com.humbhi.blackbox.ui.data.models.AllIconsModel

class AllIconsAdapter(
    private val vehicleSelection: VehicleIconSelection,
    val list: List<AllIconsModel>
) : RecyclerView.Adapter<AllIconsAdapter.ViewHolder>() {

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(vehicleSelection: VehicleIconSelection, list: List<AllIconsModel>) {
            AllIconsAdapterBinding.bind(itemView).apply {
                llCarIcon.setOnClickListener {
                    vehicleSelection.onVehicleSelection(adapterPosition)
                }

                if (list[adapterPosition].text.equals("Car")) {
                    ivVehicleIcon.setImageResource(R.drawable.ic_car_selection)
                    tvIconName.text = "Car"
                }

                if (list[adapterPosition].text.equals("Bus")) {
                    ivVehicleIcon.setImageResource(R.drawable.ic_bus_side_view)
                    tvIconName.text = "Bus"
                }

                if (list[adapterPosition].text.equals("Ambulance")) {
                    ivVehicleIcon.setImageResource(R.drawable.ic_car_selection)
                    tvIconName.text = "Ambulance"
                }

                if (list[adapterPosition].text.equals("OilTanker")) {
                    ivVehicleIcon.setImageResource(R.drawable.ic_car_selection)
                    tvIconName.text = "Oil Tanker"
                }
                if (list[adapterPosition].text.equals("Truck")) {
                    ivVehicleIcon.setImageResource(R.drawable.truck_icon)
                    tvIconName.text = "Truck"
                }
                if (list[adapterPosition].text.equals("Other")) {
                    ivVehicleIcon.setImageResource(R.drawable.ic_car_selection)
                    tvIconName.text = "Others"
                }
                if (list[adapterPosition].text.equals("Bike")) {
                    ivVehicleIcon.setImageResource(R.drawable.ic_car_selection)
                    tvIconName.text = "Bike"
                }
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_icons_adapter, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicleSelection: VehicleIconSelection = vehicleSelection
        holder.bind(vehicleSelection, list)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }


    interface VehicleIconSelection {
        fun onVehicleSelection(position: Int)
    }

}