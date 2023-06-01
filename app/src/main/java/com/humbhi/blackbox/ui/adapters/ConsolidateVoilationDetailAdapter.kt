package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ConsolidateVoilationDetailAdapterBinding
import com.humbhi.blackbox.ui.data.models.ConsolidatedViolationSub

class ConsolidateVoilationDetailAdapter(
    val context: Context,
    val listData: List<ConsolidatedViolationSub>
) :
    RecyclerView.Adapter<ConsolidateVoilationDetailAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, listData: List<ConsolidatedViolationSub>) {
            ConsolidateVoilationDetailAdapterBinding.bind(itemView).apply {
                tvDriverName.text = listData[adapterPosition].DrName
                tvVoilationType.text = listData[adapterPosition].ViolationType
                tvDate.text = listData[adapterPosition].sDate
                tvLocation.text = Html.fromHtml(
                    Html.fromHtml(listData[adapterPosition].Location).toString()
                )
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.consolidate_voilation_detail_adapter, parent, false)

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