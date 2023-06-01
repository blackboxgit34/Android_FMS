package com.humbhi.blackbox.ui.adapters


import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.OverspeedDetailAdapterBinding
import com.humbhi.blackbox.ui.data.models.OverSpeedData

class OverSpeedDetailAdapter(val context: Context, val overspeed: List<OverSpeedData>) :
    RecyclerView.Adapter<OverSpeedDetailAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, overspeed: List<OverSpeedData>) {
            OverspeedDetailAdapterBinding.bind(itemView).apply {
              tvSpeed.text = overspeed[adapterPosition].Speed+" km/h"
              tvLocation.text = Html.fromHtml(overspeed[adapterPosition].Location).toString()
              tvDateTime.text = overspeed[adapterPosition].DateTime
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.overspeed_detail_adapter, parent, false)

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