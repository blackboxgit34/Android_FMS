package com.humbhi.blackbox.ui.ui.geofencing.addFence

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.humbhi.blackbox.databinding.ActivityAddGeofenceBinding
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AllVehicleModel
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.GpsTracker
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Response
import java.io.IOException
import java.util.*
import com.humbhi.blackbox.R
import com.humbhi.blackbox.ui.adapters.CustSpinnerAdapter
import com.humbhi.blackbox.ui.adapters.PlacesAutoCompleteAdapter

class AddGeofenceActivity : AppCompatActivity(), OnMapReadyCallback, RetrofitResponse,OnMarkerDragListener {
    private lateinit var binding: ActivityAddGeofenceBinding
    private lateinit var mMap: GoogleMap
    var context: Context? = null
    var latitude = 0.0
    var longitude = 0.0
    private var Is_MAP_Moveable = false
    private var polygon: Polygon? = null
    private lateinit var tracker: GpsTracker
    var available = false
    var vehicleModel = ArrayList<AllVehicleModel>()
    var vehicleList = ArrayList<String>()
    var vehicleId = ""
    var vehicleName = ""
    private var addresses = ArrayList<Address>()
    private var pinaddresses: List<Address>? = null
    private lateinit var marker:Marker
    private var polygonOptions = PolygonOptions()
    private var locationAddress = ""
    var beginDate = ""
    var endDate = ""
    private val mapTypes = arrayOf("Standard", "Satellite", "Terrain", "Hybrid")

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGeofenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
        BottomSheetBehavior.from(binding.fBottomSheet).apply {
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.mapView.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Select Map Type")
            dialogBuilder.setItems(mapTypes) { dialog: DialogInterface?, which: Int ->
                val selectedMapType = mapTypes[which]
                setMapType(selectedMapType)
            }

            val dialog = dialogBuilder.create()
            dialog.show()
        }
        binding.toolbar.tvTitle.text = "Area Calculator"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivAdd.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        beginDate = CommonUtil.getCurrentDate()
        endDate = CommonUtil.getCurrentDate()
        binding.btDraw.setOnClickListener {
            if (binding.btDraw.text == "Draw") {
                binding.btDraw.text = "Done"
                Is_MAP_Moveable = !Is_MAP_Moveable
            } else {
                binding.btDraw.text = "Draw"
                Is_MAP_Moveable = false
            }
        }
        binding.tvSaveFence.setOnClickListener {

        }
        binding.tvReset.setOnClickListener {
            polygon?.remove()
            polygonOptions.points.clear()
            Is_MAP_Moveable = false
        }
        binding.actvLocation.setOnClickListener {
            if (binding.actvLocation.text.isNotEmpty()) {
                binding.actvLocation.setText("")
            }
        }
        getOtherCustomLocation()
        getAllVehicles()
    }

    private fun getAllVehicles() {
        Retrofit2(
            this,
            this,
            Constants.REQ_LOCATION_ON_MAP,
            Constants.LOCATION_ON_MAP + "id=" + CommonData.getCustIdFromDB()
        ).callServicehitec(true)
    }

    private fun setMapType(mapType: String) {
        // Perform any necessary actions to update the map with the chosen type
        when (mapType) {
            "Standard" -> mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            "Hybrid" -> mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            "Terrain" -> mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            "Satellite" -> mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (mMap != null) {
            tracker = GpsTracker(applicationContext)
            latitude = tracker.latitude
            longitude = tracker.longitude
            if (latitude != 0.0 && longitude != 0.0) {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                mMap.setOnMarkerDragListener(this)
                polygonOptions = PolygonOptions()
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE)
                mMap.setOnMapClickListener {
                    if (binding.btDraw.text == "Done") {
                        polygonOptions.add(it)
                        // Update the polygon on the map
                        polygon?.remove()
                        polygon = mMap.addPolygon(polygonOptions)
                        // Calculate the area of the polygon
                        // Display the area in a TextView or Toast message
                        for(item in polygonOptions.points){
                            Log.e("popints","Lat: "+item.latitude+", Long: "+item.longitude)
                        }
                    }
                    Is_MAP_Moveable
                }
                marker = mMap.addMarker(
                    MarkerOptions().position(LatLng(latitude, longitude)).icon(bitmapDescriptorFromVector(this, R.drawable.pin)).draggable(true)
                )!!
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 14.0f))
                getPostalCode()
            }
        }
    }

    private fun getOtherCustomLocation() {
        binding.actvLocation.setAdapter(PlacesAutoCompleteAdapter(this, R.layout.map_layout))
        binding.actvLocation.setOnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id: Long ->
            val descriptionpick = parent.getItemAtPosition(position) as String
            binding.actvLocation.isFocusable = false
            try {
                val geocoder: Geocoder = Geocoder(this@AddGeofenceActivity, Locale.getDefault())
                addresses = geocoder.getFromLocationName(
                    descriptionpick,
                    1
                ) as ArrayList<Address>
                if (addresses != null && addresses.size > 0) {
                    //todo for hide keyboard
                    latitude = addresses[0].latitude
                    longitude = addresses[0].longitude
                    getPostalCode()
                    if (ActivityCompat.checkSelfPermission(this@AddGeofenceActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@AddGeofenceActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                }
            } catch (ignored: Exception) {
            }
        }
    }

    // get pincode from latitude and longitude
    private fun getPostalCode() {
        val gcd = Geocoder(this@AddGeofenceActivity)
        try {
            pinaddresses = gcd.getFromLocation(latitude, longitude, 5)
            for (address in pinaddresses!!) {
                if (address.locality != null && address.postalCode != null) {
                    Log.e("Location", address.getAddressLine(0))
                    Log.e("pincode", address.postalCode)
                    binding.actvLocation.setText(address.getAddressLine(0))
                    locationAddress = address.getAddressLine(0)
                    marker.position = LatLng(address.latitude, address.longitude)
                    break
                }
            }
            if (latitude != 0.0 && longitude != 0.0) {
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(latitude, longitude),
                        14.0f
                    )
                )
            }
        } catch (e: IOException) {
            Toast.makeText(this@AddGeofenceActivity, e.toString() + "", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun  bitmapDescriptorFromVector(context: Context, vectorResId:Int):BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas =  Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when (requestCode) {
            Constants.REQ_LOCATION_ON_MAP ->{
                if (response!!.isSuccessful) {
                    try {
                        vehicleList.clear()
                        vehicleList.add("Search Vehicle")
                        // vehicleList.add(0,"Select Vehicle");
                        val data = JSONArray(response.body()!!.string())
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
            }
        }
    }

    private fun spinVehicles() {
        binding.edVehicles.adapter = CustSpinnerAdapter.getAdapter(this@AddGeofenceActivity, vehicleList)
        binding.edVehicles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (binding.edVehicles.selectedItem != "Search Vehicle") {
                    vehicleId = vehicleModel[binding.edVehicles.selectedItemPosition - 1].bbid
                    vehicleName = vehicleModel[binding.edVehicles.selectedItemPosition-1].vehname
                    Constants.hideKeyboard(this@AddGeofenceActivity, binding.edVehicles)
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    override fun onMarkerDrag(p0: Marker) {
        marker = p0
        val midLatLng: LatLng = marker.position
        latitude = midLatLng.latitude
        longitude = midLatLng.longitude
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(LatLng(latitude, longitude),14f,0f,0f)))
    }

    override fun onMarkerDragEnd(p0: Marker) {
        marker = p0
        val midLatLng: LatLng = marker.position
        latitude = midLatLng.latitude
        longitude = midLatLng.longitude
        val gcd = Geocoder(this@AddGeofenceActivity)
        try {
            pinaddresses = gcd.getFromLocation(latitude, longitude, 10)
            for (address in pinaddresses!!) {
                if (address.locality != null && address.postalCode != null) {
                    Log.e(
                        "Location",
                        address.getAddressLine(0)
                    )
                    Log.e("pincode", address.postalCode)
                    marker.position = LatLng(latitude, longitude)
                    binding.actvLocation.setText(address.getAddressLine(0))
                    locationAddress = address.getAddressLine(0)
                    break
                }
            }
        } catch (e: IOException) {
            Toast.makeText(
                this@AddGeofenceActivity,
                e.toString() + "",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    override fun onMarkerDragStart(p0: Marker) {
    }
}