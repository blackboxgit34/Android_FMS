package com.humbhi.blackbox.ui.ui.billingPayments

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityBillAccountBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.AvenuesParams
import com.humbhi.blackbox.ui.Utility.ServiceUtility
import com.humbhi.blackbox.ui.Utility.WebViewActivity
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import com.humbhi.blackbox.ui.adapters.GBillsAdapter
import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.NewBillsModel
import com.humbhi.blackbox.ui.data.models.UpdateEmailModel
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.banner.BillBanner
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors

class BillAccountActivity : AppCompatActivity(),RetrofitResponse {

    private lateinit var binding:ActivityBillAccountBinding
    private lateinit var adapter:GBillsAdapter
    private var type = "new"
    var list: ArrayList<NewBillsModel> = java.util.ArrayList<NewBillsModel>()
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())
    var cust_email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "Bills & Payments"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        if(MyApplication.cantSkip.equals("yes")){
            getAisData()
        }
        binding.toolbar.ivBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            if (Network.isNetworkAvailable(this)) {
                Retrofit2(
                    this, this,
                    Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
                    Constants.EXPIRE_ACCOUNT_DETAILS
                            + "custid=" + CommonData.getCustIdFromDB()
                ).callService(false)
            }
        }

        setupTabLayout()
        type = "new"
        getNewBills()
    }

    private fun getAisData() {
        val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch{
            try {
                val handleAisDataResponse = Api.getAis140VehicleStatuses(CommonData.getCustIdFromDB())
                handleAisDataResponse(handleAisDataResponse)
            }
            catch (e: Exception) {
                scope.coroutineContext.cancelChildren()
                val errorMessage = when (e) {
                    is ConnectException, is UnknownHostException -> R.string.no_network
                    is SocketTimeoutException -> R.string.time_out
                    is CancellationException, is SocketException -> null // Handle network loss
                    else -> R.string.something_went_wrong
                }?.let {
                    withContext(Dispatchers.Main) {
                        Constants.Toastmsg(this@BillAccountActivity, getString(it))
                    }
                }
                throw e
            }
        }
    }
    private fun handleAisDataResponse(response: Response<AisModel>?) {
        if (response != null) {
            if (response.isSuccessful) {
                mainHandler.post {
                    val responseFromBody = response.body()
                    val vehicleWithLeastValidity = responseFromBody?.Table?.minByOrNull { it.ExpireIndays }
                    var expiry = ""
                    if (vehicleWithLeastValidity != null) {
                        val expireIndays = vehicleWithLeastValidity.ExpireIndays
                        val paymentStatus = vehicleWithLeastValidity.PaymentStatus
                        when {
                            expireIndays in 29 downTo 9 && paymentStatus == "Not Paid" -> {
                                expiry = "attentionAlert"
                            }
                            expireIndays in 9 downTo 4 && paymentStatus == "Not Paid" -> {
                                expiry = "justAlert"
                            }
                            expireIndays in 4 downTo 0 && paymentStatus == "Not Paid" -> {
                                expiry = "expiringToday"
                            }
                            expireIndays < 0 && paymentStatus == "Not Paid" -> {
                                MyApplication.cantSkip = "yes"
                                expiry = "expired"
                            }
                        }
                    }
                         if (!isFinishing) {
                               val dialogFragment = vehicleWithLeastValidity?.commercialvalidity?.let {
                            vehicleWithLeastValidity.vehname?.let { it1 ->
                                WhatsNewDialogFragment(this, expiry,
                                    it, it1
                                )
                            }
                        }
                             if (dialogFragment != null) {
                                 dialogFragment.show(supportFragmentManager, "WhatsNewDialog")
                             }
                         }
                }
            }
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                OnTabTapped(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {
                OnTabTapped(tab.position)
            }
        })
    }

    private fun OnTabTapped(position: Int) {
        when (position) {
            0 -> {
                type = "new"
                getNewBills()
            }
            1 -> {
                type = "old"
                getOldBills()
            }
            else -> {

            }
        }
    }

    private fun getNewBills() {
        Retrofit2(
            this, this, Constants.REQ_GET_NEW_BILL,
            Constants.GET_NEW_BILL.toString() + "CustId=" + CommonData.getCustIdFromDB()
                    + "&sEcho=1&iDisplayStart=1&iDisplayLength=1&iSortCol_0=1&sSortDir_0="
        )
            .callService(true)
    }

    private fun getOldBills() {
        Retrofit2(
            this, this, Constants.REQ_GET_OLD_BILL, (Constants.GET_OLD_BILL.toString() + "CustId="
                    + CommonData.getCustIdFromDB()
                    + "&sEcho=1&iDisplayStart=1&iDisplayLength=1&iSortCol_0=1&sSortDir_0=")
        )
            .callService(true)
    }

    private fun getBillingDetails(amount: String, billno: String) {
        Retrofit2(
            this,
            this,
            Constants.REQ_GET_BILLING_DETAILS,
            (Constants.GET_BILLING_DETAILS.toString() + "custid=" + CommonData.getCustIdFromDB()
                    + "&amount=" + amount + "&billno=" + billno)
        )
            .callService(true)
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when (requestCode) {
            Constants.REQ_GET_NEW_BILL -> if (response!!.isSuccessful) {
                try {
                    list.clear()
                    val jsonObject = JSONObject(response!!.body()!!.string())
                    val iTotalRecords = jsonObject.getInt("iTotalRecords")
                    val aaData = jsonObject.getJSONArray("aaData")
                    var i = 0
                    while (i < aaData.length()) {
                        val obj = aaData.getJSONObject(i)
                        val model = NewBillsModel()
                        model.custid = obj.getString("custid")
                        model.billno = obj.getString("billno")
                        model.boxCount = obj.getString("BoxCount")
                        model.billperiod = obj.getString("billperiod")
                        model.currentBalance = obj.getString("actualamount")
                        model.otherCharges = obj.getString("OtherCharges")
                        model.discount = obj.getString("Discount")
                        model.previousBalance = obj.getString("PreviousBalance")
                        model.totalBalance = obj.getString("Balance")
                        model.fromDate = obj.getString("FromDate")
                        model.type = type
                        list.add(model)
                        i++
                    }
                    binding.rvBills.layoutManager = LinearLayoutManager(this)
                    adapter = GBillsAdapter(this, list)
                    binding.rvBills.setAdapter(adapter)
                    adapter.onItemSelectedListener { poss, view1 ->
                        getBillingDetails(
                            list[poss].totalBalance,
                            list[poss].billno
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            Constants.REQ_GET_OLD_BILL -> if (response!!.isSuccessful) {
                try {
                    list.clear()
                    val jsonObject = JSONObject(response!!.body()!!.string())
                    val iTotalRecords = jsonObject.getInt("iTotalRecords")
                    val aaData = jsonObject.getJSONArray("aaData")
                    var i = 0
                    while (i < aaData.length()) {
                        val obj = aaData.getJSONObject(i)
                        val model = NewBillsModel()
                        model.custid = obj.getString("custid")
                        model.billno = obj.getString("billno")
                        model.boxCount = obj.getString("BoxCount")
                        model.billperiod = obj.getString("billperiod")
                        model.currentBalance = obj.getString("actualamount")
                        model.otherCharges = obj.getString("OtherCharges")
                        model.discount = obj.getString("Discount")
                        model.previousBalance = obj.getString("PreviousBalance")
                        model.totalBalance = obj.getString("Balance")
                        model.fromDate = obj.getString("FromDate")
                        model.type = type
                        list.add(model)
                        i++
                    }
                    binding.rvBills.setLayoutManager(LinearLayoutManager(this))
                    adapter = GBillsAdapter(this, list)
                    binding.rvBills.setAdapter(adapter)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            Constants.REQ_EXPIRE_ACCOUNT_DETAILS -> {
                executor.execute {
                    try {
                        var SoftBanner = ""
                        var hardBanner = ""
                        var aisCount = 0
                        val result = JSONObject(response!!.body()!!.string())
                        val table = result.getJSONArray("Table")
                        for (i in 0 until table.length()) {
                            val jsonObject = table.getJSONObject(0)
                            SoftBanner = jsonObject.getString("SoftBanner")
                            hardBanner = jsonObject.getString("hardBanner")
                            aisCount = jsonObject.getString("Ais140Count").toInt()
                        }
                        CommonData.setAisCount(aisCount.toString())
                        if(!CommonData.getSkip().equals("yes")) {
                            if (SoftBanner != "false") {
                                mainHandler.post() {
                                    val intent = Intent(this, BillBanner::class.java)
                                    intent.putExtra("SoftBanner", SoftBanner)
                                    intent.putExtra("hardBanner", hardBanner)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                        if (hardBanner != "false") {
                            mainHandler.post() {
                                val intent = Intent(this, BillBanner::class.java)
                                intent.putExtra("SoftBanner", SoftBanner)
                                intent.putExtra("hardBanner", hardBanner)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } catch (e: JSONException) {
                    } catch (e: IOException) {
                    }
                }
            }
            Constants.REQ_GET_BILLING_DETAILS ->
                if (response!!.isSuccessful) {
                try {
                    val jsonObject1 = JSONObject(response!!.body()!!.string())
                    val cust_Billname = jsonObject1.getString("Billname")
                    val cust_address = jsonObject1.getString("address")
                    val cust_city = jsonObject1.getString("city")
                    val cust_state = jsonObject1.getString("state")
                    val cust_postalcode = jsonObject1.getString("PostalCode")
                    val cust_mobile = jsonObject1.getString("mobile")
                    val cust_Country_new = jsonObject1.getString("Country")
                    cust_email = jsonObject1.getString("email")
                    val cust_phone = jsonObject1.getString("phone")
                    val cust_success = jsonObject1.getString("success")
                    val cust_Amount = jsonObject1.getString("Amount")
                    val cust_BillNo = jsonObject1.getString("BillNo")
                    val cust_error_code = jsonObject1.getString("Error_code")
                    //String cust_error_code = "Incorrect email&Incorrect phone";
                    if (cust_error_code.equals("Incorrect email", ignoreCase = true)) {
                        showUpdateEmailDialog()
                    } else if (cust_error_code.equals("Incorrect phone", ignoreCase = true)) {
                        //    alert = new AlertClass(MyCurrentBillActivity.this, cust_error_code);
                        val builder1 = AlertDialog.Builder(this)
                        builder1.setMessage("We do not have your updated phone number.Please contact customer care(0172-5016957).")
                        builder1.setCancelable(true)
                        builder1.setPositiveButton(
                            Html.fromHtml("<font color='#ffffff'>Call</font>"),
                            DialogInterface.OnClickListener { dialog, id ->
                                val callUri = Uri.parse(Constants.PHONE_CALL + "0172-5016957")
                                val callIntent = Intent(Intent.ACTION_CALL, callUri)
                                callIntent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
                                if (ActivityCompat.checkSelfPermission(
                                        this,
                                        Manifest.permission.CALL_PHONE
                                    ) !== PackageManager.PERMISSION_GRANTED
                                ) {
                                    return@OnClickListener
                                }
                                startActivity(callIntent)
                            })
                        builder1.setNegativeButton(
                            Html.fromHtml("<font color='#ffffff'>Cancel</font>")
                        ) { dialog, id -> dialog.cancel() }
                        val alert11 = builder1.create()
                        alert11.show()
                    } else if (cust_error_code.equals(
                            "Incorrect email&Incorrect phone",
                            ignoreCase = true
                        )
                    ) {
                        //    alert = new AlertClass(MyCurrentBillActivity.this, "We do not have your updated email id and phone number.Please contact customer care().");
                        val builder1 = AlertDialog.Builder(this)
                        builder1.setMessage("We do not have your updated email id and phone number.Please contact customer care(0172-5016957).")
                        builder1.setCancelable(true)
                        builder1.setPositiveButton(
                            Html.fromHtml("<font color='#ffffff'>Call</font>"),
                            DialogInterface.OnClickListener { dialog, id ->
                                val callUri = Uri.parse(Constants.PHONE_CALL + "0172-5016957")
                                val callIntent = Intent(Intent.ACTION_CALL, callUri)
                                callIntent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
                                if (ActivityCompat.checkSelfPermission(
                                        this,
                                        Manifest.permission.CALL_PHONE
                                    ) !== PackageManager.PERMISSION_GRANTED
                                ) {
                                    return@OnClickListener
                                }
                                startActivity(callIntent)
                            })
                        builder1.setNegativeButton(
                            Html.fromHtml("<font color='#ffffff'>Cancel</font>")
                        ) { dialog, id -> dialog.cancel() }
                        val alert11 = builder1.create()
                        alert11.show()
                    } else {

                        //    String vAccessCode = "4YRUXLSRO20O8NIH";
                        val vAccessCode = "AVWU74EK14AN34UWNA"
                        val vMerchantId = "153998"
                        //     String vMerchantId = "2";
                        val vCurrency = "INR"
                        if (vAccessCode != "" && vMerchantId != "" && vCurrency != "" && cust_Amount != "") {
                            val intent = Intent(this, WebViewActivity::class.java)
                            intent.putExtra(
                                AvenuesParams.ACCESS_CODE,
                                java.lang.String.valueOf(ServiceUtility.chkNull(vAccessCode))
                            )
                            intent.putExtra(
                                AvenuesParams.MERCHANT_ID,
                                java.lang.String.valueOf(ServiceUtility.chkNull(vMerchantId))
                            )
                            intent.putExtra(
                                AvenuesParams.ORDER_ID,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_BillNo))
                            )
                            intent.putExtra(
                                AvenuesParams.CURRENCY,
                                java.lang.String.valueOf(ServiceUtility.chkNull(vCurrency))
                            )
                            intent.putExtra(
                                AvenuesParams.AMOUNT, java.lang.String.valueOf(
                                    ServiceUtility.chkNull(
                                        cust_Amount
                                    )
                                )
                            )
                            intent.putExtra(
                                AvenuesParams.REDIRECT_URL,
                                java.lang.String.valueOf(ServiceUtility.chkNull("http://trackmaster.in/AspxPages/ccavResponseHandler.aspx"))
                            )
                            intent.putExtra(
                                AvenuesParams.CANCEL_URL,
                                java.lang.String.valueOf(ServiceUtility.chkNull("http://trackmaster.in/AspxPages/ccavResponseHandler.aspx"))
                            )
                            intent.putExtra(
                                AvenuesParams.RSA_KEY_URL,
                                java.lang.String.valueOf(ServiceUtility.chkNull("http://trackmaster.in/AspxPages/GetRSA.aspx"))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_Name,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_Billname))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_Address,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_address))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_City,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_city))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_Zip_Code,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_postalcode))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_State,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_state))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_Country,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_Country_new))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_Mobile,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_mobile))
                            )
                            intent.putExtra(
                                AvenuesParams.Billing_Email,
                                java.lang.String.valueOf(ServiceUtility.chkNull(cust_email))
                            )

                            //    intent.putExtra("billing_address", String.valueOf(ServiceUtility.chkNull(cust_address)));
                            //     intent.putExtra("billing_city", String.valueOf(ServiceUtility.chkNull(cust_city)));
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "All parameters are mandatory.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    this,
                    "Oops! Something went wrong, Please try later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Do nothing to prevent going back
    }

    private fun showUpdateEmailDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Email")
        // Inflate the dialog view
        val dialogView = layoutInflater.inflate(R.layout.update_email, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialogView.findViewById<TextView>(R.id.oldEmailTextView).text = "Old Email: "+cust_email

        // Set up the positive and negative buttons
        builder.setPositiveButton("Update") { dialog, _ ->
            // Get the new email from the input field
            val newEmail = dialogView.findViewById<EditText>(R.id.newEmailEditText)
            val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
            if(newEmail.text.isEmpty()){
                Constants.alertDialog(this,"Email Id can't be null")
            }
            else if(!isValidEmail(newEmail.text.toString())){
                Constants.alertDialog(this,"Please enter valid email id")
            }
            else {
                val selectedEmail =  newEmail.text.trim().toString()
                binding.progress.progressLayout.visibility = View.VISIBLE
                Api.updateEmail(CommonData.getCustIdFromDB(), selectedEmail)
                    .enqueue(object : Callback<UpdateEmailModel> {
                        override fun onResponse(
                            call: Call<UpdateEmailModel>,
                            response: Response<UpdateEmailModel>
                        ) {
                            binding.progress.progressLayout.visibility = View.GONE
                            val responseFromBody = response.body()
                            if (responseFromBody != null) {
                                if(responseFromBody.data.equals("1")){
                                    dialog.dismiss()
                                    Constants.alertDialog(this@BillAccountActivity,"Email Id update successfully, now you can pay now")
                                }
                            }
                        }

                        override fun onFailure(call: Call<UpdateEmailModel>, t: Throwable) {
                            binding.progress.progressLayout.visibility = View.GONE
                            if(t is SocketTimeoutException){
                                Constants.alertDialog(this@BillAccountActivity,this@BillAccountActivity.getString(
                                    R.string.time_out))
                            }
                            else{
                                Constants.alertDialog(this@BillAccountActivity,"Something went wrong")
                            }
                        }
                    })
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        // Display the dialog
        builder.show()
    }
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}