package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.VehicleAlertAdapterBinding
import com.humbhi.blackbox.ui.data.models.AlertForApp

class VehicleAlertAdapter(val context: Context, val list: List<AlertForApp>) :
    RecyclerView.Adapter<VehicleAlertAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, list: List<AlertForApp>) {
            VehicleAlertAdapterBinding.bind(itemView).apply {




                    tvAlertText.text = list.get(adapterPosition).AlertDetails
                    if (tvAlertText.text.equals("Main Battery Disconnection")){
                        ivIcon.setImageResource(R.drawable.ic_alert_battert_disconnection)
                    }
                    if (tvAlertText.text.equals("OverStoppage")){
                        ivIcon.setImageResource(R.drawable.ic_alert_overstoppage)
                    }
                    if (tvAlertText.text.equals("Over-speed")){
                        ivIcon.setImageResource(R.drawable.ic_alert_overspeed)
                    }
                    if (tvAlertText.text.equals("IgnitionOn")){
                        ivIcon.setImageResource(R.drawable.ic_alert_ignitionon)
                    }
                    if (tvAlertText.text.equals("POI Sms")){
                        ivIcon.setImageResource(R.drawable.ic_alert_geofence_voilate)
                    }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_alert_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context, list)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }
}