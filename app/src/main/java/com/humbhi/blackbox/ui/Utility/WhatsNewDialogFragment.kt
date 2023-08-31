package com.humbhi.blackbox.ui.Utility;
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.humbhi.blackbox.R
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.GBillsAdapter.MyHolder
import com.humbhi.blackbox.ui.ui.ais140.AIS140VehicleActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class WhatsNewDialogFragment(context: Context, private  val expiry: String, val expiryDate: String, val vehName: String) : DialogFragment() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_dash, null)
        val text: TextView = view.findViewById(R.id.contentTextView)
        val text2:TextView = view.findViewById(R.id.titleTextView)
        val date = dateAndTime(expiryDate)
        if(expiry.equals("justAlert")) {
            context?.let { text.setTextColor(it.getColor(R.color.white)) }
            context?.let { text2.setTextColor(it.getColor(R.color.white)) }
            text.text = "Dear customer, your AIS-140 renewal is due. For more details"
            text2.visibility = View.GONE
        }
        else if(expiry.equals("attentionAlert")){
            val blinkAnimation = AnimationUtils.loadAnimation(context, R.anim.blink_animation)
            context?.let { text.setTextColor(it.getColor(R.color.white)) }
            context?.let { text2.setTextColor(it.getColor(R.color.red_alpha_one)) }
            text.text = "Dear customer, your AIS-140 renewal is due. Pay before "+date+"\nFor more details"
            text2.startAnimation(blinkAnimation)
            text2.text = "Immediate attention!!!"
        }
        else if(expiry.equals("expiringSoon")){
            val blinkAnimation = AnimationUtils.loadAnimation(context, R.anim.blink_animation)
            context?.let { text.setTextColor(it.getColor(R.color.white)) }
            context?.let { text2.setTextColor(it.getColor(R.color.red_alpha_one)) }
            text.text = "Dear customer, AIS-140 renewal of following devices are due. Please pay imidiately to avoid late fees.\nVehicle tracking will be disabled after "+date+"\n" +
                    "For more details"
            text2.startAnimation(blinkAnimation)
            text2.text = "Immediate attention!!!"
        }
        else if(expiry.equals("expired")){
            val blinkAnimation = AnimationUtils.loadAnimation(context, R.anim.blink_animation)
            context?.let { text.setTextColor(it.getColor(R.color.white)) }
            context?.let { text2.setTextColor(it.getColor(R.color.red_alpha_one)) }
            text.text = "Dear customer, you are not able to view tracking of following vehicles due to non-payment. Recharge immediately to continue the services\nFor more details"
            text2.startAnimation(blinkAnimation)
            text2.text = "Immediate attention!!!"
        }
        val pay: TextView = view.findViewById(R.id.payButton)
        pay.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(context,AIS140VehicleActivity::class.java))
        }
        // Find and set click listener for the close ImageView
        val closeButton = view.findViewById<ImageView>(R.id.close)
        closeButton.setOnClickListener {
            if(MyApplication.cantSkip.equals("no")) {
                MyApplication.skip = "yes"
            }
            dismiss() // Close the dialog
        }
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        // Make the AlertDialog background transparent
        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Adjust dialog's window attributes for bottom-end positioning
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = (screenWidth / 2.2).toInt() // Set the width to a fraction of the screen width
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM or Gravity.END
        layoutParams.x = resources.getDimensionPixelOffset(R.dimen.margin_v)
        layoutParams.y = resources.getDimensionPixelOffset(R.dimen.margin_25) // Set the margin from the bottom
        window?.attributes = layoutParams
        return dialog
    }


    fun showDialog() {
        // Create and show the dialog using the provided context
        if (context is AppCompatActivity) {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val dialogFragment = WhatsNewDialogFragment(MyApplication.getAppContext(), expiry, expiryDate, vehName)
            dialogFragment.show(fragmentManager, "WhatsNewDialog")
        }
    }

    fun dateAndTime(inputDateString: String?): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val date = inputFormat.parse(inputDateString)
            if (date != null) {
                return outputFormat.format(date)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}
