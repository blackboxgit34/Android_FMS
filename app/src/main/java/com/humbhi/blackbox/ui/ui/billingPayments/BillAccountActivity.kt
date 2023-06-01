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
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.humbhi.blackbox.databinding.ActivityBillAccountBinding
import com.humbhi.blackbox.ui.Utility.AvenuesParams
import com.humbhi.blackbox.ui.Utility.ServiceUtility
import com.humbhi.blackbox.ui.Utility.WebViewActivity
import com.humbhi.blackbox.ui.adapters.GBillsAdapter
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.NewBillsModel
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class BillAccountActivity : AppCompatActivity(),RetrofitResponse {

    private lateinit var binding:ActivityBillAccountBinding
    private lateinit var adapter:GBillsAdapter
    private var type = "new"
    var list: ArrayList<NewBillsModel> = java.util.ArrayList<NewBillsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "Bills & Payments"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        setupTabLayout()
        type = "new"
        getNewBills()
    }

    private fun setupTabLayout() {
        binding.tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                OnTabTapped(tab.getPosition())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {
                OnTabTapped(tab.getPosition())
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
            .callService(false)
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
            Constants.REQ_GET_BILLING_DETAILS -> if (response!!.isSuccessful) {
                try {
                    val jsonObject1 = JSONObject(response!!.body()!!.string())
                    val cust_Billname = jsonObject1.getString("Billname")
                    val cust_address = jsonObject1.getString("address")
                    val cust_city = jsonObject1.getString("city")
                    val cust_state = jsonObject1.getString("state")
                    val cust_postalcode = jsonObject1.getString("PostalCode")
                    val cust_mobile = jsonObject1.getString("mobile")
                    val cust_Country_new = jsonObject1.getString("Country")
                    val cust_email = jsonObject1.getString("email")
                    val cust_phone = jsonObject1.getString("phone")
                    val cust_success = jsonObject1.getString("success")
                    val cust_Amount = jsonObject1.getString("Amount")
                    val cust_BillNo = jsonObject1.getString("BillNo")
                    val cust_error_code = jsonObject1.getString("Error_code")
                    //String cust_error_code = "Incorrect email&Incorrect phone";
                    if (cust_error_code.equals("Incorrect email", ignoreCase = true)) {
                        //   alert = new AlertClass(MyCurrentBillActivity.this, "We do not have your updated email id.Please contact customer care().");
                        val builder1 = AlertDialog.Builder(this)
                        builder1.setMessage("We do not have your updated email id.Please contact customer care(0172-5016957).")
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
                    } else if (cust_error_code.equals("Incorrect phone", ignoreCase = true)) {
                        //    alert = new AlertClass(MyCurrentBillActivity.this, cust_error_code);
                        val builder1 = AlertDialog.Builder(this)
                        builder1.setMessage("We do not have your updated phome number.Please contact customer care(0172-5016957).")
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