package com.humbhi.blackbox.ui.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.DrivingLimitAdapterBinding
import com.humbhi.blackbox.databinding.ScoreCardLayoutBinding
import com.humbhi.blackbox.ui.data.models.AaData
import com.humbhi.blackbox.ui.data.models.AaDataX

class DrivingLimitAdapter(val context: Context, val listData: List<AaDataX>) :
    RecyclerView.Adapter<DrivingLimitAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, list: List<AaDataX>) {
            DrivingLimitAdapterBinding.bind(itemView).apply {
                tvVehNum.text = list[adapterPosition].VehicleName
                tvDrivingLimit.text = list[adapterPosition].DrLimit
                tvDrivingCount.text = list[adapterPosition].count.toString()
                tvHaltLimit.text = list[adapterPosition].haltTime
                tvDrivingDuration.text = list[adapterPosition].TotDur
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.driving_limit_adapter, parent, false)

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