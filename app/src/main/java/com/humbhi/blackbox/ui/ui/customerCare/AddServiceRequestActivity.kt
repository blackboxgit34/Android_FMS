package com.humbhi.blackbox.ui.ui.customerCare


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.ui.adapters.CustSpinnerAdapter
import com.humbhi.blackbox.ui.adapters.SearchableAdapter
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AllVehicleModel
import com.humbhi.blackbox.ui.data.models.ComplaintTypeModel
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

open class AddServiceRequestActivity : AppCompatActivity(), View.OnClickListener, RetrofitResponse {
    var spinnerservicerequest: Spinner? = null
    private var spComplaint: Spinner? = null
    var edtcomment: EditText? = null
    var tvTitle: AppCompatTextView? = null
    val vehiclelist = ArrayList<String>()
    val complaintlist = ArrayList<String>()
    var bbid: String? = null
    private var complaintType: String? = null
    private var vehiclename: String? = null
    val list: ArrayList<AllVehicleModel> = ArrayList<AllVehicleModel>()
    val complaintTypeModel: ArrayList<ComplaintTypeModel> = ArrayList<ComplaintTypeModel>()
    var btnsubmit: TextView? = null
    var ivMenu: AppCompatImageView? = null
    var ivBack: AppCompatImageView? = null
    var ivBell: AppCompatImageView? = null
    var ivSort: AppCompatImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_request)
        initViews()
    }

    fun initViews() {
        spinnerservicerequest = findViewById(R.id.spinnerservicerequest)
        spComplaint = findViewById(R.id.spComplaint)
        btnsubmit = findViewById(R.id.btnsubmit)
        edtcomment = findViewById(R.id.edtcomment)
        tvTitle = findViewById(R.id.tvTitle)
        ivMenu = findViewById(R.id.ivMenu)
        ivBack = findViewById(R.id.ivBack)
        ivBell = findViewById(R.id.ivBell)
        ivSort = findViewById(R.id.ivSort)
        getVehicles()
        btnsubmit!!.setOnClickListener(this)
        tvTitle!!.text = "Add Service "
        ivMenu?.visibility = View.GONE
        ivBack?.visibility = View.VISIBLE
        ivBell?.visibility = View.GONE
        ivSort?.visibility = View.GONE
        ivBack?.setOnClickListener {
            finish()
        }
    }


    open fun getVehicles() {
        Retrofit2(
            this, this, Constants.REQ_GET_ALL_VEHICLES,
            Constants.GET_ALL_VEHICLES.toString() + "id=" + CommonData.getCustIdFromDB()
        )
            .callServicehitec(false)
    }

    open fun getComplaintType() {
        Retrofit2(
            this, this, Constants.REQ_GET_COMPLAINT_TYPE,
            Constants.GET_COMPLAINT_TYPE
        )
            .callServicehitec(false)
    }

    open fun getAddServiceRequest() {

        Retrofit2(this, this, Constants.REQ_ADD_SERVICE_REQUEST,
            Constants.GET_ADD_SERVICE_REQUEST.toString() +"custid="+CommonData.getCustIdFromDB()+"&BBID="+bbid+"&VehicleName="+vehiclename+"&Description="+complaintType+"&Comment="+edtcomment!!.text.toString()
                .trim { it <= ' ' })
            .callServicehitec(false)
    }

    /*
     *  Validations
     * */
    open fun validation(): Boolean {
        var check = true
        if (vehiclelist.size == 0) {
            Constants.alertDialog(this, "Please select vehicle.")
            check = false
        } else if ((spinnerservicerequest!!.selectedItem == "Select Vehicle")) {
            Constants.alertDialog(this, "Please select vehicle.")
            check = false
        } else if (complaintlist.size == 0) {
            Constants.alertDialog(this, "Please select complaint type.")
            check = false
        } else if ((spComplaint?.selectedItem.toString() == "Select Complaint Type")) {
            Constants.alertDialog(this, "Please select complaint type.")
            check = false
        }
        return check
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnsubmit ->
                if (validation()) {
                    getAddServiceRequest()
                }
        }
    }

    /*
     * Spinner
     * */
    private fun spinVehicles() {

        spinnerservicerequest!!.adapter = SearchableAdapter(this, vehiclelist)
        spinnerservicerequest!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    val tv: TextView = view as TextView
                    if (!(spinnerservicerequest!!.selectedItem == "Select Vehicle")) {
                        bbid = list.get(spinnerservicerequest!!.selectedItemPosition - 1).bbid
                        vehiclename =
                            list.get(spinnerservicerequest!!.selectedItemPosition - 1).vehname
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }

    /*
     * Spinner
     * */
    fun spinComplaint() {
        spComplaint?.adapter = SearchableAdapter(this, complaintlist)
        spComplaint?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val tv = view as TextView
                if (spComplaint?.selectedItem != "Select Complaint Type") {
                    complaintType =
                        complaintTypeModel[spComplaint?.selectedItemPosition
                            ?.minus(1)!!].description
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }


    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>) {
        when (requestCode) {
            Constants.REQ_GET_ALL_VEHICLES -> if (response.isSuccessful) {
                try {
                    list.clear()
                    vehiclelist.clear()
                    vehiclelist.add(0, "Select Vehicle")
                    val data = JSONArray(response.body()!!.string())
                    var i = 0
                    while (i < data.length()) {
                        val obj = data.getJSONObject(i)
                        val model = AllVehicleModel()
                        model.bbid = obj.getString("bbid")
                        model.vehname = obj.getString("vehname")
                        vehiclelist.add(obj.getString("vehname"))
                        list.add(model)
                        spinVehicles()
                        i++
                    }
                    getComplaintType()
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            Constants.REQ_GET_COMPLAINT_TYPE -> if (response.isSuccessful) {
                complaintTypeModel.clear()
                complaintlist.clear()
                complaintlist.add(0, "Select Complaint Type")
                try {
                    val data = JSONArray(response.body()!!.string())
                    var i = 0
                    while (i < data.length()) {
                        val obj = data.getJSONObject(i)
                        val model = ComplaintTypeModel()
                        model.id = obj.getString("ID")
                        model.description = obj.getString("Description")
                        complaintlist.add(obj.getString("Description"))
                        complaintTypeModel.add(model)
                        spinComplaint()
                        i++
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            Constants.REQ_ADD_SERVICE_REQUEST -> if (response.isSuccessful) {

                try {
                    val obj = JSONObject(response.body()!!.string())
                    if (obj.has("Message")) {
                        Constants.alertDialog(this, obj.getString("Message"))
                    } else {
                        Constants.alertDialog(this, obj.getString("data").toString())
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }
}