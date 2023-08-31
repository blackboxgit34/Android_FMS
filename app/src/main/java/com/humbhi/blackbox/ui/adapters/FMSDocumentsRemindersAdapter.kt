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
import com.humbhi.blackbox.databinding.DocumentsRemindersBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.data.models.AaDataXXXX
import com.humbhi.blackbox.ui.data.models.ReminderData

class FMSDocumentsRemindersAdapter (val context: Context, val list:List<AaDataXXXX>) :
    RecyclerView.Adapter<FMSDocumentsRemindersAdapter.MyHolder>(){

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list: List<AaDataXXXX>) {
            DocumentsRemindersBinding.bind(itemView).apply {
                tvVehicleName.text = list[adapterPosition].vehicleName
                tvDetails.setOnClickListener {
                    if(rvDetails.visibility==View.VISIBLE){
                        tvDetails.text = "View Detail"
                        rvDetails.visibility = View.GONE
                    }
                    else{
                        tvDetails.text = "Hide Detail"
                        rvDetails.visibility = View.VISIBLE
                    }
                    rvDetails.layoutManager = LinearLayoutManager(context)
                    val adapter = list[adapterPosition].reminderData?.let { it1 ->
                        FMSDocumentReminderDetailAdapter(context, object : FMSDocumentReminderDetailAdapter.VehicleDetailsUpdate {
                            override fun onVehicleSelection(position: Int) {
                                val dialog = Dialog(context)
                                dialog.setContentView(R.layout.dialog_options_layout)
                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                dialog.setCancelable(true)

                                val editButton = dialog.findViewById<Button>(R.id.editButton)
                                val deleteButton = dialog.findViewById<Button>(R.id.deleteButton)

                                editButton.setOnClickListener {
                                    // Handle Edit option
                                    dialog.dismiss()
                                }

                                deleteButton.setOnClickListener {
                                    // Handle Delete option
                                    dialog.dismiss()
                                }

                                dialog.show()
                            }
                        },
                            it1
                        )
                    }
                    rvDetails.adapter = adapter
                    }
                }
            }
        }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.documents_reminders, parent, false)
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