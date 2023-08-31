package com.humbhi.blackbox.ui.ui.ais140

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.DnsResolver
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityAis140VehicleBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.AvenuesParams
import com.humbhi.blackbox.ui.Utility.ServiceUtility
import com.humbhi.blackbox.ui.Utility.WebViewActivity
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import com.humbhi.blackbox.ui.adapters.AIS140Adapter
import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.Ais140Models
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
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors

class AIS140VehicleActivity : AppCompatActivity() ,RetrofitResponse{

    private lateinit var binding: ActivityAis140VehicleBinding
    var list : ArrayList<Ais140Models> = ArrayList<Ais140Models>()
    val executor = Executors.newSingleThreadExecutor()
    var subscription = ""
    var cust_email = ""
    var status = ""
    val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAis140VehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "AIS 140 Validity Status"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
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
        binding.rvRecycler.setHasFixedSize(true)
        binding.rvRecycler.layoutManager = LinearLayoutManager(this)
        getAIS140VehicleList()
    }

    private fun getAIS140VehicleList(){
        binding.progress.progressLayout.visibility = View.VISIBLE
        executor.execute {
            val api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
            api.getAis140VehicleStatus(CommonData.getCustIdFromDB())
                .enqueue(object : Callback<AisModel> {
                    override fun onResponse(call: Call<AisModel>, response: Response<AisModel>) {
                        binding.progress.progressLayout.visibility = View.GONE
                       val responseFromBody = response.body()
                        if (responseFromBody != null) {
                            for(i in 0 until responseFromBody.Table.size){
                                responseFromBody.Table[i].PaymentStatus.let {
                                    responseFromBody.Table[i].TotalAmountOneYear.let { it1 ->
                                        Ais140Models(responseFromBody.Table[i].vehname,
                                            responseFromBody.Table[i].instDate,
                                            if( responseFromBody.Table[i].commercialvalidity.equals("null")){
                                                ""
                                            } else{
                                                responseFromBody.Table[i].commercialvalidity?.let { it2 ->
                                                    dateAndTime(
                                                        it2
                                                    )
                                                }
                                            },
                                            responseFromBody.Table[i].OneYearCharge,
                                            responseFromBody.Table[i].bbid,
                                            responseFromBody.Table[i].ExpireIndays.toString(),
                                            responseFromBody.Table[i].latefee,
                                            responseFromBody.Table[i].DeviceStatus,
                                            it,
                                            it1,
                                            responseFromBody.Table[i].Status,
                                            responseFromBody.Table[i].TwoYearCharge,
                                            responseFromBody.Table[i].TotalAmountTwoYear
                                        )
                                    }
                                }.let {
                                    list.add(
                                        it
                                    )
                                }
                            }
                            val adapter = AIS140Adapter(this@AIS140VehicleActivity,object : AIS140Adapter.payNow{
                                override fun pay(position: Int,plan: String, totalAmount: Double,Status: String) {
                                    list[position].bbid?.let {
                                        getBillingDetails(totalAmount.toString(),
                                            it
                                        )
                                    }
                                    subscription = plan
                                    status = Status
                                }
                            },list)
                            binding.rvRecycler.adapter = adapter
                        }
                    }

                    override fun onFailure(call: Call<AisModel>, t: Throwable) {
                        binding.progress.progressLayout.visibility = View.GONE
                        if (t is SocketTimeoutException) {
                            Constants.Toastmsg(
                                this@AIS140VehicleActivity,
                                this@AIS140VehicleActivity.getString(R.string.time_out)
                            )
                        } else if (t is UnknownHostException) {
                            Constants.Toastmsg(
                                this@AIS140VehicleActivity,
                                this@AIS140VehicleActivity.getString(R.string.no_network)
                            )
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (t is DnsResolver.DnsException) {
                                Constants.Toastmsg(
                                    this@AIS140VehicleActivity,
                                    this@AIS140VehicleActivity.getString(R.string.dns_error)
                                )
                            }
                        } else {
                            Constants.Toastmsg(
                                this@AIS140VehicleActivity,
                                this@AIS140VehicleActivity.getString(R.string.something_went_wrong)
                            )
                        }
                    }
                })
        }
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

    fun dateAndTime(inputDateString: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDateString)
        return date?.let { outputFormat.format(it) }
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode) {
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
            Constants.REQ_GET_BILLING_DETAILS -> {
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
                                intent.putExtra("status",status)
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
                                intent.putExtra(
                                    AvenuesParams.merchant_param1,
                                    java.lang.String.valueOf(ServiceUtility.chkNull("ais140"))
                                )
                                if (subscription.equals("1")) {
                                    intent.putExtra(
                                        AvenuesParams.merchant_param2,
                                        java.lang.String.valueOf(ServiceUtility.chkNull("12"))
                                    )
                                } else {
                                    intent.putExtra(
                                        AvenuesParams.merchant_param2,
                                        java.lang.String.valueOf(ServiceUtility.chkNull("24"))
                                    )
                                }
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
                                    Constants.alertDialog(this@AIS140VehicleActivity,"Email Id update successfully, now you can pay now")
                                }
                            }
                        }

                        override fun onFailure(call: Call<UpdateEmailModel>, t: Throwable) {
                            binding.progress.progressLayout.visibility = View.GONE
                            if(t is SocketTimeoutException){
                                Constants.alertDialog(this@AIS140VehicleActivity,this@AIS140VehicleActivity.getString(R.string.time_out))
                            }
                            else{
                                Constants.alertDialog(this@AIS140VehicleActivity,"Something went wrong")
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
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Do nothing to prevent going back
    }
}