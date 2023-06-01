package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ExcellentDriverAdapterBinding
import com.humbhi.blackbox.ui.data.models.ExcellentDriverdata

class ExcellentDriverAdapter(val context: Context, val list: List<ExcellentDriverdata>) :
    RecyclerView.Adapter<ExcellentDriverAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, list: List<ExcellentDriverdata>) {
            ExcellentDriverAdapterBinding.bind(itemView).apply {
                tvDriverName.text = list[adapterPosition].Driver.toString()
                tvVehicleName.text = list[adapterPosition].VehName

                pvHarshAcc.setProgress(((list[adapterPosition].HA / 100.0f) * 10).toInt())
                pvHarshBreak.setProgress(((list[adapterPosition].HB / 100.0f) * 10).toInt())
                pvHarshRashTurn.setProgress(((list[adapterPosition].RT / 100.0f) * 10).toInt())
                pvHarshOS.setProgress(((list[adapterPosition].OS / 100.0f) * 10).toInt())
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.excellent_driver_adapter, parent, false)

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