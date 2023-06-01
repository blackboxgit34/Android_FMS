package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.HarshBreakChartAdapterBinding
import com.humbhi.blackbox.ui.data.models.HarshBreakData


class HarshBreakChartAdapter(val context: Context, val listData: List<HarshBreakData>) :
    RecyclerView.Adapter<HarshBreakChartAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, listData: List<HarshBreakData>) {
            HarshBreakChartAdapterBinding.bind(itemView).apply {
                tvName.text = listData[adapterPosition].VehicleName
                //cvBar.getLayoutParams().height = listData[adapterPosition].count
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.harsh_break_chart_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.bind(context, listData)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listData.size
    }
}