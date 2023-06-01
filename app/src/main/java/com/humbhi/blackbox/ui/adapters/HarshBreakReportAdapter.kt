package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.HarshBreakAdapterBinding
import com.humbhi.blackbox.ui.data.models.HarshBreakData
import com.humbhi.blackbox.ui.data.models.StoppageData

class HarshBreakReportAdapter(val context: Context,val listData: List<HarshBreakData>) :
    RecyclerView.Adapter<HarshBreakReportAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,listData: List<HarshBreakData>) {
            HarshBreakAdapterBinding.bind(itemView).apply {
                tvCount.text = listData[adapterPosition].count.toString()
                tvVehicleName.text = listData[adapterPosition].VehicleName
                if (listData[adapterPosition].count==0){
                    tvViewOnMap.visibility = View.GONE
                }
                else{
                    tvViewOnMap.visibility = View.GONE
                    tvViewOnMap.setOnClickListener {

                    }
                }
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.harsh_break_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.bind(context,listData)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
    return listData.size
    }
}