package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.OverstoppageAdapterBinding
import com.humbhi.blackbox.ui.data.models.OverStoppageData

class OverstoppageAdapter(val context: Context,val list:ArrayList<OverStoppageData>) :
    RecyclerView.Adapter<OverstoppageAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list:ArrayList<OverStoppageData>) {
            OverstoppageAdapterBinding.bind(itemView).apply {
                tvVehicleName.text = list.get(adapterPosition).vehname
                tvStoppageCount.text = list.get(adapterPosition).overstoppageCount.toString()
                tvStoppageLimit.text = list.get(adapterPosition).overstoppageLimit.toString()
                tvtime.text =
                    list.get(adapterPosition).overstoppageDuration.replace("day(s)", "Days")
                        .replace("Hour(s)", "Hr").replace("Minute(s)", " Min")
                        .replace("Second(s)", " Sec").replace("second(s)", " Sec")
            }
        }


    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.overstoppage_adapter, parent, false)

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