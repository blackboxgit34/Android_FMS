package com.humbhi.blackbox.ui.adapters
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.DashReminderLayoutBinding
import com.humbhi.blackbox.databinding.DocumentsRemindersBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.data.models.AaDataXXXX
import com.humbhi.blackbox.ui.data.models.DashReminderModel
import com.humbhi.blackbox.ui.data.models.ReminderData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class DashRemindersAdapter (val context: Context, val list:List<DashReminderModel>) :
    RecyclerView.Adapter<DashRemindersAdapter.MyHolder>(){
    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list: List<DashReminderModel>) {
            DashReminderLayoutBinding.bind(itemView).apply {
                reminderType.text = list[adapterPosition].reminderType
                when (list[adapterPosition].reminderType) {
                    "National Permit" -> {
                        reminderIcon.setImageResource(R.drawable.permit)
                    }

                    else -> {
                        reminderIcon.setImageResource(R.drawable.document_reminders)
                    }
                }
                if (list[adapterPosition].remDays != null) {
                    if (list[adapterPosition].remDays!!.toInt() < 2) {
                        remainingDays.text = list[adapterPosition].remDays.toString() + " day"
                    } else {
                        remainingDays.text = list[adapterPosition].remDays.toString() + " days"
                    }
                }
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dash_reminder_layout, parent, false)
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