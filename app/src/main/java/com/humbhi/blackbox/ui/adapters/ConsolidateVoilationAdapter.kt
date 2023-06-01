package com.humbhi.blackbox.ui.adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ConsolidateVoilationAdapterBinding
import com.humbhi.blackbox.ui.data.models.ConsolidateVoilationData
import com.humbhi.blackbox.ui.ui.drivingBehaviour.consolidateReport.consolidateVoilationDetails.ConsolidateVoilationDetails
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.overSpeedDetail.OverspeedDetailActivity
import com.humbhi.blackbox.ui.utils.ExplicitIntentUtil

class ConsolidateVoilationAdapter(
    val context: Context,
    val listData: List<ConsolidateVoilationData>
) :
    RecyclerView.Adapter<ConsolidateVoilationAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, listData: List<ConsolidateVoilationData>) {
            ConsolidateVoilationAdapterBinding.bind(itemView).apply {
                tvVehicleName.text =  Html.fromHtml(Html.fromHtml(listData[adapterPosition].VehicleName).toString())
                tvHA.text = listData[adapterPosition].HACount.toString()
                tvHB.text = listData[adapterPosition].HBCount.toString()
                tvRT.text = listData[adapterPosition].RTCount.toString()
                tvOS.text = listData[adapterPosition].OSCount.toString()
                tvTotalVoilations.text = listData[adapterPosition].totViolation.toString()
                if (listData[adapterPosition].ConsolidatedViolationSubList.isEmpty()){
                    tvDetails.visibility = View.GONE
                }
                else{
                    tvDetails.visibility = View.VISIBLE

                    tvDetails.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putParcelableArrayList(
                            "VoilationDetail",
                            ArrayList(listData[adapterPosition].ConsolidatedViolationSubList)
                        )
                        bundle.putString("VehicleName",
                            Html.fromHtml(Html.fromHtml(listData[adapterPosition].VehicleName).toString())
                                .toString()
                        )
                        ExplicitIntentUtil.startActivity(
                            context as Activity,
                            ConsolidateVoilationDetails::class.java, bundle
                        )
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
            .inflate(R.layout.consolidate_voilation_adapter, parent, false)

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