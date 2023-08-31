package com.humbhi.blackbox.ui.ui.fms

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.humbhi.blackbox.databinding.ActivityAddDocumentBinding
import com.humbhi.blackbox.ui.adapters.SearchableAdapter
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.FMSVehicleListModel
import com.humbhi.blackbox.ui.data.models.RenewalTypesModel
import com.humbhi.blackbox.ui.data.models.RenewalTypesModelItem
import com.humbhi.blackbox.ui.data.models.VehicleModelList
import com.humbhi.blackbox.ui.data.network.AsyncApicall
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.Calendar

class AddDocumentActivity : AppCompatActivity() {
    val binding: ActivityAddDocumentBinding by lazy { ActivityAddDocumentBinding.inflate(layoutInflater) }
    val Api : NetworkService by lazy{ NewRetrofitClient.getInstance().create(NetworkService::class.java) }
    var list: ArrayList<VehicleModelList> = ArrayList()
    var list2: ArrayList<RenewalTypesModelItem> = ArrayList()
    var vehicleList : ArrayList<String> = ArrayList()
    var renewalList : ArrayList<String> = ArrayList()
    var vehicleId = ""
    var vehicleName = ""
    var renewalId = ""
    var renewalName = ""
    var dueDateParam = ""
    var picker: DatePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Add Documents Reminder"
        binding.dueDate.keyListener = null
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.dueDate.setOnClickListener {
            datepicker()
        }
        binding.tvSubmit.setOnClickListener {
            if(vehicleId.equals("") && renewalId.equals("") && dueDateParam.equals("") &&  binding.ReminderDays.text.toString().equals("") && binding.Remarks.text.toString().equals("")) {
                Constants.alertDialog(this,"Please fill all details")
            }
            else{
                addReminder()
            }
        }
        hitApi()
    }

    private fun datepicker() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, 0) // to get back 13 year add -13
        val previous_year = cal.time
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                var monthOfYear = monthOfYear
                val x: String
                val y: String
                if (monthOfYear < 9) {
                    monthOfYear = monthOfYear + 1
                    x = "0$monthOfYear"
                } else {
                    x = (monthOfYear + 1).toString()
                }
                y = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                dueDateParam = "$year-$x-$y"
                binding.dueDate.setText("$y-$x-$year")
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        try {
            picker = datePickerDialog.datePicker
            picker!!.minDate = System.currentTimeMillis();
        } catch (e: Exception) {
            // e.printStackTrace();
            picker = datePickerDialog.datePicker
        }
        datePickerDialog.show()
    }

    private fun hitApi() {
        binding.progress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            Api.fmsVehiclesList(CommonData.getCustIdFromDB())
                .enqueue(object : Callback<FMSVehicleListModel> {
                    override fun onResponse(
                        call: Call<FMSVehicleListModel>,
                        response: Response<FMSVehicleListModel>
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            vehicleList.clear()
                            list.clear()
                            val responseFromApi = response.body()!!
                            for(i in 0 until responseFromApi.size){
                                list.add(VehicleModelList(responseFromApi[i].VehicleName,responseFromApi[i].BBID))
                                vehicleList.add(responseFromApi[i].VehicleName)
                            }
                            spinVehicles()
                            CoroutineScope(Dispatchers.IO).launch {
                            Api.getAllRenewalTypes().enqueue(object: Callback<RenewalTypesModel>{
                                override fun onResponse(
                                    call: Call<RenewalTypesModel>,
                                    response: Response<RenewalTypesModel>
                                ) {
                                    CoroutineScope(Dispatchers.Main).launch{
                                        binding.progress.visibility = View.GONE
                                        renewalList.clear()
                                        list2.clear()
                                        val responseFromApi2 = response.body()!!
                                        for(i in 0 until responseFromApi2.size){
                                            list2.add(RenewalTypesModelItem(responseFromApi2[i].Selected,
                                                responseFromApi2[i].Value,
                                                responseFromApi2[i].Text))
                                            renewalList.add( responseFromApi2[i].Text)
                                        }
                                        spinRenewalType()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<RenewalTypesModel>,
                                    t: Throwable
                                ) {
                                    CoroutineScope(Dispatchers.Main).launch{
                                        binding.progress.visibility = View.GONE
                                        if (t is SocketTimeoutException) {
                                            Constants.alertDialog(
                                                this@AddDocumentActivity,
                                                "Please check your network connection"
                                            )
                                        } else {
                                            Constants.alertDialog(
                                                this@AddDocumentActivity,
                                                "Something went wrong"
                                            )
                                        }
                                    }
                                }
                            })
                            }
                        }
                    }

                    override fun onFailure(call: Call<FMSVehicleListModel>, t: Throwable) {
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.progress.visibility = View.GONE
                            if (t is SocketTimeoutException) {
                                Constants.alertDialog(
                                    this@AddDocumentActivity,
                                    "Please check your network connection"
                                )
                            } else {
                                Constants.alertDialog(
                                    this@AddDocumentActivity,
                                    "Something went wrong"
                                )
                            }
                        }
                    }
                })
        }
    }

    private fun addReminder(){
        binding.progress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = AsyncApicall.callApi {
                    Api.addUpdateReminder(
                        "0",
                        renewalId,
                        dueDateParam,
                        binding.ReminderDays.text.toString(),
                        vehicleId,
                        binding.Remarks.text.toString()
                    )
                }
                CoroutineScope(Dispatchers.Main).launch {
                    val response = data.string()
                    if (response.equals("1") || response.equals("2")) {
                        binding.progress.visibility = View.GONE
                        Constants.alertWithIntent(this@AddDocumentActivity,"Successfully added",DocumentReminder::class.java)
                        finish()
                    }
                    else{
                        binding.progress.visibility = View.GONE
                        Constants.alertDialog(this@AddDocumentActivity,"Something went wrong")
                    }
                }
            }
            catch (e: Throwable) {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.progress.visibility = View.GONE
                }
            }
        }
    }

    fun spinVehicles() {
        //Getting the instance of AutoCompleteTextView
        val adapter = SearchableAdapter(this, vehicleList)
        binding.spVehicles.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        binding.spVehicles.setOnItemClickListener { parent, view, position, id ->
            val selection = parent.getItemAtPosition(position) as String
            var pos = -1
            for (i in vehicleList.indices) {
                if (vehicleList[i] == selection) {
                    pos = i
                    break
                }
            }
            vehicleId = list[pos].bbid
            vehicleName = list[pos].vehname
        }
        binding.spVehicles.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.spVehicles.showDropDown() }
        binding.spVehicles.setOnTouchListener { v, event ->
            binding.spVehicles.showDropDown()
            false
        }
    }

    fun spinRenewalType() {
        //Getting the instance of AutoCompleteTextView
        val adapter = SearchableAdapter(this, renewalList)
        binding.spRenewalType.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        binding.spRenewalType.setOnItemClickListener { parent, view, position, id ->
            val selection = parent.getItemAtPosition(position) as String
            var pos = -1
            for (i in renewalList.indices) {
                if (renewalList.get(i) == selection) {
                    pos = i
                    break
                }
            }
            renewalId = list2.get(pos).Text
            renewalName = list2.get(pos).Value
        }
        binding.spRenewalType.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.spRenewalType.showDropDown() }
        binding.spRenewalType.setOnTouchListener { v, event ->
            binding.spRenewalType.showDropDown()
            false
        }
    }

}