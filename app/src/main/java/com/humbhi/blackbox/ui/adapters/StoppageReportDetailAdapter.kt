package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.CommonDetailLayoutBinding
import com.humbhi.blackbox.ui.data.models.ObjStoppageReport

class StoppageReportDetailAdapter(
    private val context: Context,
    private val listData: List<ObjStoppageReport>
) : RecyclerView.Adapter<StoppageReportDetailAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CommonDetailLayoutBinding.bind(view)

        fun bind(context: Context, list: List<ObjStoppageReport>) {
            val item = list[adapterPosition]
            binding.tvLocation.text = Html.fromHtml(Html.fromHtml(item.StopLocation).toString())
            binding.tvStartDateTime.text = item.StartDate
            binding.tvStopDateTime.text = item.StopDate
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the common_detail_layout view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.common_detail_layout, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context, listData)
    }

    // return the number of items in the list
    override fun getItemCount(): Int {
        return listData.size
    }
}
