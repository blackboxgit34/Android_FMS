package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.OverspeedDetailAdapterBinding
import com.humbhi.blackbox.ui.data.models.DrOverSpeedSubLst
import com.humbhi.blackbox.ui.data.models.OverSpeedData

class OverSpeedDetailDBAdapter(val context: Context, val overspeed: ArrayList<DrOverSpeedSubLst>) :
    RecyclerView.Adapter<OverSpeedDetailDBAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, overspeed: List<DrOverSpeedSubLst>) {
            OverspeedDetailAdapterBinding.bind(itemView).apply {
                tvSpeed.text = overspeed[adapterPosition].Speed.toString()+" km/h"
                tvLocation.text = Html.fromHtml(overspeed[adapterPosition].Location).toString()
                tvDateTime.text = overspeed[adapterPosition].stDate
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.overspeed_detail_adapter, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context, overspeed)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return overspeed.size
    }

}