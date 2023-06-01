package com.humbhi.blackbox.ui.adapters

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.WorkingHourDetailAdapterBinding
import com.humbhi.blackbox.ui.data.models.ObjIgnitionStatusReport

class WorkingHourDetailAdapter(val list: List<ObjIgnitionStatusReport>) :
    RecyclerView.Adapter<WorkingHourDetailAdapter.ViewHolder>() {


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(list: List<ObjIgnitionStatusReport>) {
            WorkingHourDetailAdapterBinding.bind(itemView).apply {
                tvOnTime.text = list[adapterPosition].IgnitionOnTime
                tvOffTime.text = list[adapterPosition].IgnitionOffTime
                tvDuration.text = list[adapterPosition].Duration
                tvSLocation.text =
                    Html.fromHtml(Html.fromHtml(list[adapterPosition].SLocation).toString())
                tvELocation.text =
                    Html.fromHtml(Html.fromHtml(list[adapterPosition].ELocation).toString())
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.working_hour_detail_adapter, parent, false)
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