package com.humbhi.blackbox.ui.ui.livetracking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.humbhi.blackbox.databinding.ActivityLocateMyVehicleBinding
import com.humbhi.blackbox.ui.adapters.CustSpinnerAdapter
import com.humbhi.blackbox.ui.data.db.CommonData.getCustIdFromDB
import com.humbhi.blackbox.ui.data.models.AllVehicleModel
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.GpsTracker
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException


class LocateMyVehicle : AppCompatActivity(), RetrofitResponse {
    lateinit var binding: ActivityLocateMyVehicleBinding
    var vehicleModel = ArrayList<AllVehicleModel>()
    var vehicleList = ArrayList<String>()
    var vehicleId = ""
    var vehicleName = ""
    var lat:Double = 0.0
    var long:Double = 0.0
    var myLat:Double = 0.0
    var myLong:Double = 0.0
    lateinit var tracker: GpsTracker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocateMyVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        getAllVehicles()
        tracker = GpsTracker(applicationContext)
        myLat = tracker.latitude
        myLong = tracker.longitude
        binding.btnGetRoute.setOnClickListener {
            Log.e("lat and long: ", "$myLat, $myLong")
            if(lat != 0.0 && long != 0.0)
            {
             val uri = "http://maps.google.com/maps?saddr=$myLat,$myLong&daddr=$lat,$long"
             val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
             startActivity(intent)
            }
        }
    }

    private fun setToolbar() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Locate My Vehicle"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getAllVehicles() {
        Retrofit2(
            this,
            this,
            Constants.REQ_LOCATION_ON_MAP,
            Constants.LOCATION_ON_MAP + "id=" + getCustIdFromDB()
        )
            .callServicehitec(true)
    }

    fun spinVehicles() {
        //Getting the instance of AutoCompleteTextView
        binding.spVehicles.setThreshold(0) //will start working from first character
        binding.spVehicles.setAdapter(
            CustSpinnerAdapter.getAdapter(
                this,
                vehicleList
            )
        ) //setting the adapter data into the AutoCompleteTextView
        binding.spVehicles.setOnItemClickListener { parent, view, position, id ->
            val selection = parent.getItemAtPosition(position) as String
            var pos = -1

            for (i in vehicleList.indices) {
                if (vehicleList[i] == selection) {
                    pos = i
                    break
                }
            }
            vehicleId = vehicleModel.get(pos).getBbid()
            vehicleName = vehicleModel.get(pos).getVehname()
            Retrofit2(this, this, Constants.REQ_VEHICLES_ON_MAP, Constants.VEHICLES_ON_MAP + "custid=" + getCustIdFromDB() + "&StatusCode=&sEcho=0&iDisplayStart=0&iDisplayLength=999" + "&sSearch=" + vehicleName + "&iSortCol_0=0&sSortDir_0=").callService(true)
        }
        binding.spVehicles.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.spVehicles.showDropDown() }
        binding.spVehicles.setOnTouchListener { v, event ->
            binding.spVehicles.showDropDown()
            false
        }
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when (requestCode) {
            Constants.REQ_LOCATION_ON_MAP ->
                if (response!!.isSuccessful) {
                try {
                    vehicleList.clear()
                    // vehicleList.add(0,"Select Vehicle");
                    val data = JSONArray(response!!.body()!!.string())
                    var i = 0
                    while (i < data.length()) {
                        val obj = data.getJSONObject(i)
                        val model = AllVehicleModel()
                        model.bbid = obj.getString("BoxId")
                        model.vehname = obj.getString("VehName")
                        vehicleList.add(obj.getString("VehName"))
                        vehicleModel.add(model)
                        spinVehicles()
                        i++
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        Constants.REQ_VEHICLES_ON_MAP ->{
                if (response!!.isSuccessful) {
                    try {
                        val jsonObject = JSONObject(response.body()!!.string())
                        val data = jsonObject.getJSONArray("aaData")
                        for ( i in 0 until data.length()) {
                            val obj = data.getJSONObject(i)
                             lat = obj.getDouble("Latitude")
                             long = obj.getDouble("Longitude")
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
}