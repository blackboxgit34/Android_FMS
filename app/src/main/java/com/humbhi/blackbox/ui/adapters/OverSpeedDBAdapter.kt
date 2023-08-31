package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.OverSpeedDbBinding
import com.humbhi.blackbox.ui.data.models.AaDataXXXXXX


class OverSpeedDBAdapter(val context: Context, private val vehicleSelection: OverSpeedDetails, val overspeed: List<AaDataXXXXXX>) :
    RecyclerView.Adapter<OverSpeedDBAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind( context: Context,vehicleSelection: OverSpeedDetails, overspeed: List<AaDataXXXXXX>) {
            OverSpeedDbBinding.bind(itemView).apply {
                tvVehNum.text = overspeed[adapterPosition].VehicleName
                tvOverspeedCount.text = overspeed[adapterPosition].count.toString()
                tvMaxSpeed.text = overspeed[adapterPosition].Speed.toString()
                if (overspeed[adapterPosition].drOverSpeedSubLst.isNotEmpty()) {
                    tvDetails.visibility = View.VISIBLE
                    tvDetails.setOnClickListener {
                        vehicleSelection.onVehicleSelection(adapterPosition)
                    }
                } else {
                    tvDetails.visibility = View.GONE
                }
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.over_speed_db, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context,vehicleSelection ,overspeed)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return overspeed.size
    }

    interface OverSpeedDetails{
        fun onVehicleSelection(position:Int)
    }

}