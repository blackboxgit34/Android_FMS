package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ScoreCardLayoutBinding
import com.humbhi.blackbox.ui.data.models.AaData

class ScoreCardAdapter(val context: Context, val listData: List<AaData>) :
    RecyclerView.Adapter<ScoreCardAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, list: List<AaData>) {
            ScoreCardLayoutBinding.bind(itemView).apply {
                tvSNo.text = list[position].SNo.toString()
                tvDriverName.text = "Driver Name: "+list[position].DriverName
                tvAS.text = "Score: "+list[position].AvgScr.toString()
                if(list[position].Distance.toInt()==0){
                    tvRamarks.text = "Remarks: N/A"
                }
                else{
                    if (list[position].AvgScr in 0.0..40.0) {
                        //    val remark = "<font color='#008000'>Paid \"+\"(₹ \" + total+\")</font>\""
                        tvRamarks.text = "Remarks: Risky"
                    }
                    else if (list[position].AvgScr > 40 && list[position].AvgScr <= 60) {
                        tvRamarks.text = "Remarks: Moderate"
                    }
                    else if (list[position].AvgScr > 60 && list[position].AvgScr <= 100) {
                        tvRamarks.text = "Remarks: Safe"
                    }
                }
                fun ScoreChart(scr:Float): Float? {
                    var score : Float? = null
                    if (scr >= 0 && scr < 10) {
                        score = 0f
                    }
                    if (scr == 10f) {
                        score = 10.00f
                    }
                    else if (scr > 10f && scr <= 20f) {
                        score = 20.0f
                    }
                    else if (scr > 20f && scr <= 30f) {
                        score = 30.0f
                    }
                    else if (scr > 30f && scr <= 40f) {
                        score = 40.0f
                    }
                    else if (scr > 40 && scr <= 50) {
                        score = 50.0f
                    }
                    else if (scr > 50 && scr <= 60) {
                        score = 60.0f
                    }
                    else if (scr > 60 && scr <= 70) {
                        score = 70.0f
                    }
                    else if (scr > 70 && scr <= 80) {
                        score = 80.0f
                    }
                    else if (scr > 80 && scr <= 90) {
                        score = 90.0f
                    }
                    else if (scr > 90 ) {
                        score = 100.0f
                    }
                    return score
                }
                val star = ScoreChart(list[position].AvgScr.toFloat())!!.toFloat()
                val rating = star/20
                ratingBar.rating = rating
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.score_card_layout, parent, false)

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