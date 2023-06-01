package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.MonthlyReportAdapterBinding
import com.humbhi.blackbox.ui.data.models.MonthData
import com.humbhi.blackbox.ui.data.models.MonthlyDataReponseModel

class MonthlyReportAdapter(val context: Context,val list:List<MonthData>) :
    RecyclerView.Adapter<MonthlyReportAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list:List<MonthData>) {
            MonthlyReportAdapterBinding.bind(itemView).apply {
                tvStoppagesCount.text = list.get(adapterPosition).OverStoppageCount+" times"
                tvIgnition.text = list[adapterPosition].IgnitionOnHours
                tvDriving.text = list[adapterPosition].DrivingTime
                tvDistanceText.text = list.get(adapterPosition).DistanceTravelled+"\nkm"
                tvVehName.text = list[adapterPosition].VehicleNo
                tubeSpeedometer.speedTo(list[adapterPosition].DistanceTravelled.toFloat())
                tubeSpeedometer.withTremble = false
                tubeSpeedometer.unitTextSize=0F
                tubeSpeedometer.speedTextSize = 0F
                tubeSpeedometer.speedTextColor = R.color.red
                val sDays = list[adapterPosition].StoppageTime.substringBefore("-")
                val sHours = list[adapterPosition].StoppageTime.substringAfter("-")
                if (sDays == "00") {
                    val newStrg = sHours
                    val mString = newStrg.split(":").toTypedArray()
                    if (mString[0] == "00") {
                        tvStoppage.text = mString[1] + " Min " + mString[2] + " Sec"
                    } else {
                        tvStoppage.text = mString[0] + " Hr " + mString[1] + " Min"
                    }

                } else {
                    tvStoppage.text = sDays + " Days"
                }
                setTimeFormat(list[adapterPosition].IgnitionOnHours,tvIgnition)
                setTimeFormat(list[adapterPosition].DrivingTime,tvDriving)

            }
        }

        fun setTimeFormat(time:String,textView: TextView){
            var sDays = time.substringBefore("-")
            val sHours = time.substringAfter("-")
            if (sDays == "00") {
                val newStrg = sHours
                val mString = newStrg.split(":").toTypedArray()
                if (mString[0] == "00") {
                    textView.text = mString[1] + " Min " + mString[2] + " Sec"
                } else {
                    textView.text = mString[0] + " Hr " + mString[1] + " Min"
                }

            } else {
                textView.text = sDays + " Days"
            }
        }
    }


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.monthly_report_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context,list)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }
}