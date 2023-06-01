package com.humbhi.blackbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.TempDetailAdapterBinding
import com.humbhi.blackbox.ui.data.models.TempDetailData

class TempDetailAdapter(val list: List<TempDetailData>) :
    RecyclerView.Adapter<TempDetailAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(list: List<TempDetailData>) {
            TempDetailAdapterBinding.bind(itemView).apply {

                tvLocation.text = list[adapterPosition].Location
                tvDateTime.text = list[adapterPosition].DateTime
                tvTemp.text = list[adapterPosition].Temperature.toString() + "°C"
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.temp_detail_adapter, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

}