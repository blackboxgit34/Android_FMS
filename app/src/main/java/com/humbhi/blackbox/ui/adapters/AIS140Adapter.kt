package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.AisVehicleAdapterBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.areDatesValidAndFirstIsLess
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.Ais140Models
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.ui.billingPayments.BillAccountActivity
import com.humbhi.blackbox.ui.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates

class AIS140Adapter(val context: Context, val pay: payNow , val list: ArrayList<Ais140Models>) : RecyclerView.Adapter<AIS140Adapter.MyHolder>() {
    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun addOneMonthToDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

            // Convert the input date string to a Date object
            val date = inputFormat.parse(dateString)

            // Add one month to the date
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MONTH, 1)

            // Format the resulting date back to the desired format
            val resultDate = calendar.time
            return outputFormat.format(resultDate)
        }
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(context: Context, pay: payNow, list: ArrayList<Ais140Models>) {
            AisVehicleAdapterBinding.bind(itemView).apply {
                var subscription = ""
                var totalAmount = 0.0
                tvVehicleName.text = list[adapterPosition].veh
                lateFee.text = list[adapterPosition].lateFee.toString()
                if(list[adapterPosition].status==1){
                    radioButton2.visibility = View.VISIBLE
                    radioButton2.isChecked = true
                    billAmount.text = "₹"+list[adapterPosition].billAmountTwoYear.toString()
                    totalFee.text = "₹"+list[adapterPosition].totalAmountTwoYear.toString()
                    totalAmount = list[adapterPosition].totalAmountTwoYear
                    subscription = "2"
                    radioButton1.visibility = View.GONE
                }
                else if(list[adapterPosition].status==2) {
                    radioButton2.visibility = View.VISIBLE
                    subscription = "2"
                    billAmount.text = "₹"+list[adapterPosition].billAmountTwoYear.toString()
                    totalFee.text = "₹"+list[adapterPosition].totalAmountTwoYear.toString()
                    totalAmount = list[adapterPosition].totalAmountTwoYear
                    radioButton2.isChecked = true
                    radioButton1.visibility = View.GONE
                }
                else{
                    subscription = "1"
                    billAmount.text = "₹"+list[adapterPosition].billAmountOneYear.toString()
                    totalFee.text = "₹"+list[adapterPosition].totalAmountOneYear.toString()
                    totalAmount = list[adapterPosition].totalAmountOneYear
                    radioButton1.isChecked = true
                    radioButton2.visibility = View.VISIBLE
                    radioButton1.visibility = View.VISIBLE
                }
                radioButton1.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        subscription = "1"
                        billAmount.text = "₹"+list[adapterPosition].billAmountOneYear.toString()
                        totalFee.text = "₹"+(list[adapterPosition].totalAmountOneYear).toString()
                        totalAmount = list[adapterPosition].totalAmountOneYear
                    }
                }

                // Find the RadioButton and set a check change listener
                radioButton2.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        subscription = "2"
                        if (list[adapterPosition].status == 2) {
                            billAmount.text =
                                "₹"+list[adapterPosition].billAmountTwoYear.toString()
                            totalFee.text =
                                "₹"+list[adapterPosition].totalAmountTwoYear.toString()
                            totalAmount = list[adapterPosition].totalAmountTwoYear
                        }
                        else if(list[adapterPosition].status == 1){
                            billAmount.text = "₹"+list[adapterPosition].billAmountTwoYear.toString()
                            totalFee.text = "₹"+list[adapterPosition].totalAmountTwoYear.toString()
                            totalAmount = list[adapterPosition].totalAmountTwoYear
                        }
                        else{
                            billAmount.text =
                                "₹"+list[adapterPosition].billAmountTwoYear.toString()
                            totalFee.text =
                                "₹"+list[adapterPosition].totalAmountTwoYear.toString()
                            totalAmount = list[adapterPosition].totalAmountTwoYear
                        }
                    }
                }
                if(list[adapterPosition].expirationDate.equals("null")){
                    tvExpirationDate.text = "Valid upto "
                }
                else{
                    tvExpirationDate.text = "Valid upto "+list[adapterPosition].expirationDate
                }
                if(list[adapterPosition].DeviceStatus.equals("Expired")){
                    if(list[adapterPosition].PaymentStatus.equals("Paid")){
                        validityStatus.text = list[adapterPosition].DeviceStatus
                        validityStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_circle_red,
                            0,
                            0,
                            0
                        )
                        tvPaid.visibility = View.VISIBLE
                        tvPay.visibility = View.GONE
                        llLateFee.visibility = View.GONE
                        Status.visibility = View.VISIBLE
                        llTotalPayment.visibility = View.GONE
                        deviceStatus.visibility = View.GONE
                        llBillAmount.visibility = View.GONE
                        rgYearSubscription.visibility = View.GONE
                    }
                    else {
                        validityStatus.text = list[adapterPosition].DeviceStatus
                        validityStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_circle_red,
                            0,
                            0,
                            0
                        )
                        deviceStatus.text = "*Dear customer, your Black Box AIS-140 tracking service has expired. Please renew immediately to continue the services, otherwise, your services will be permanently disconnected. You are not able to track this vehicle"
                        deviceStatus.isSelected = true
                        rgYearSubscription.visibility = View.VISIBLE
                        tvPaid.visibility = View.GONE
                        Status.visibility = View.VISIBLE
                        llLateFee.visibility = View.VISIBLE
                        llBillAmount.visibility = View.VISIBLE
                        llTotalPayment.visibility = View.VISIBLE
                        deviceStatus.visibility = View.VISIBLE
                        tvPay.visibility = View.VISIBLE
                        rgYearSubscription.visibility = View.VISIBLE
                    }
                }
                else if(list[adapterPosition].DeviceStatus.equals("Inactive Device(PD)")){
                    if(list[adapterPosition].PaymentStatus.equals("Paid")){
                        validityStatus.text = list[adapterPosition].DeviceStatus
                        validityStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_circle_red,
                            0,
                            0,
                            0
                        )
                        tvPaid.visibility = View.VISIBLE
                        Status.visibility = View.VISIBLE
                        tvPay.visibility = View.GONE
                        llLateFee.visibility = View.GONE
                        llTotalPayment.visibility = View.GONE
                        deviceStatus.visibility = View.GONE
                        llBillAmount.visibility = View.GONE
                        rgYearSubscription.visibility = View.GONE
                    }
                    else {
                        validityStatus.text = list[adapterPosition].DeviceStatus
                        validityStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_circle_red,
                            0,
                            0,
                            0
                        )
                        deviceStatus.text = "*Dear customer, your Black Box AIS-140 tracking service has been permanently deactivated, to activate the services, please pay the charges immediately."
                        deviceStatus.isSelected = true
                        tvPaid.visibility = View.GONE
                        rgYearSubscription.visibility = View.VISIBLE
                        Status.visibility = View.VISIBLE
                        llLateFee.visibility = View.VISIBLE
                        llBillAmount.visibility = View.VISIBLE
                        llTotalPayment.visibility = View.VISIBLE
                        deviceStatus.visibility = View.VISIBLE
                        tvPay.visibility = View.VISIBLE
                        rgYearSubscription.visibility = View.VISIBLE
                    }
                }
                else if(list[adapterPosition].DeviceStatus.equals("Expiring soon")){
                    if(list[adapterPosition].PaymentStatus.equals("Paid")){
                        validityStatus.text = list[adapterPosition].DeviceStatus
                        validityStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_green_circle,
                            0,
                            0,
                            0
                        )
                        tvPaid.visibility = View.VISIBLE
                        tvPay.visibility = View.GONE
                        llLateFee.visibility = View.GONE
                        llTotalPayment.visibility = View.GONE
                        deviceStatus.visibility = View.GONE
                        llBillAmount.visibility = View.GONE
                        Status.visibility = View.GONE
                        rgYearSubscription.visibility = View.GONE
                    }
                    else {
                        validityStatus.text = list[adapterPosition].DeviceStatus
                        validityStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_green_circle,
                            0,
                            0,
                            0
                        )
                        tvPaid.visibility = View.GONE
                        llLateFee.visibility = View.GONE
                        llBillAmount.visibility = View.VISIBLE
                        llTotalPayment.visibility = View.VISIBLE
                        deviceStatus.visibility = View.VISIBLE
                        Status.visibility = View.GONE
                        rgYearSubscription.visibility = View.VISIBLE
                        deviceStatus.text = "*Dear customer, your Black Box AIS-140 tracking service renewal is due. Please pay the renewal charges before " + list[adapterPosition].expirationDate.toString() + " to avoid the diconnection of services and late fees."
                        deviceStatus.isSelected = true
                        tvPay.visibility = View.VISIBLE
                    }
                }
                else if(list[adapterPosition].DeviceStatus.equals("Device Ok")){
                    validityStatus.text = "Active"
                    validityStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_green_circle,0,0,0)
                    tvPaid.visibility = View.GONE
                    tvPay.visibility = View.GONE
                    llLateFee.visibility = View.GONE
                    llTotalPayment.visibility = View.GONE
                    Status.visibility = View.GONE
                    deviceStatus.visibility = View.GONE
                    llBillAmount.visibility = View.GONE
                    rgYearSubscription.visibility = View.GONE
                }
                else{
                    validityStatus.text = "Active"
                    validityStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_green_circle,0,0,0)
                    tvPay.visibility = View.VISIBLE
                    tvPaid.visibility = View.GONE
                    llLateFee.visibility = View.GONE
                    deviceStatus.visibility = View.GONE
                    Status.visibility = View.GONE
                    llTotalPayment.visibility = View.GONE
                    rgYearSubscription.visibility = View.GONE
                    llBillAmount.visibility = View.GONE
                }
                tvPay.setOnClickListener {
                    pay.pay(adapterPosition,subscription,totalAmount,list[adapterPosition].DeviceStatus.toString())
                }
            }
        }
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ais_vehicle_adapter, parent, false)
        return MyHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context,pay,list)
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    interface payNow{
        fun pay(position: Int,plan:String,totalAmount:Double,status: String)
    }
}