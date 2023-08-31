package com.humbhi.blackbox.ui.ui.vehicleStatus.vehicleDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityVehicleDetailBinding
import com.humbhi.blackbox.ui.adapters.VehicleAlertAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AlertForApp
import com.humbhi.blackbox.ui.data.models.VehicleDetailResponse
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.routePlayback.RoutePlayBack
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.IntentConstant
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class VehicleDetailActivity : AppCompatActivity(), VehicleDetailView, OnMapReadyCallback,View.OnClickListener {
    private lateinit var binding: ActivityVehicleDetailBinding
    private lateinit var vehicleDetailPresenter: VehicleDetailPresenter
    private lateinit var mMap: GoogleMap
    private lateinit var runnable: Runnable
    val points: ArrayList<LatLng> = ArrayList()
    val latList: ArrayList<Double> = ArrayList()
    val lngList: ArrayList<Double> = ArrayList()
    private var handler = Handler(Looper.getMainLooper())

    private val repeatPeriod: Long = 5000
    private var isFirstTime: Boolean = true
    private  var alertList: List<AlertForApp>? = null
    var Angle_Bring = 0f
    private lateinit var adapter: VehicleAlertAdapter
    private lateinit var vehicleId:String
    private lateinit var backendStartDate:String
    private lateinit var backendEndDate:String
    private lateinit var vehicleName:String
    private lateinit var marker: Marker
    var userLocationMarker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        vehicleDetailPresenter = VehicleDetailPresenterImpl(this, DataManagerImpl(RestClient.getRetrofitBuilderForTrackMasterSecure()))
        BottomSheetBehavior.from(binding.fBottomSheet).apply {
            peekHeight = 450
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val vehName: String? = intent.getStringExtra(IntentConstant.VEHICLE_NAME)
        if (vehName != null) {
            binding.toolbar.tvTitle.text = vehName
            runnable = Runnable {

                vehicleDetailPresenter.hitVehicleDetailApi(
                    CommonData.getCustIdFromDB(),
                    "",
                    "0",
                    0,
                    1,
                    vehName,
                    "0",
                    ""
                )

                handler.postDelayed(runnable, repeatPeriod)

            }

            handler.postDelayed(runnable, repeatPeriod)


        }

        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvPlayback.setOnClickListener {
            val c = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = df.format(c.time)
            backendStartDate = formattedDate
            backendEndDate = formattedDate
            val intent = Intent(this, RoutePlayBack::class.java)
            intent.putExtra("tableName", vehicleId)
            intent.putExtra("fromDate", backendStartDate)
            intent.putExtra("endDate", backendEndDate)
            intent.putExtra("vehicleName", vehicleName)
            intent.putExtra("flag", "vehicleStatus")
            intent.putExtra("showStoppages", "0")

            startActivity(intent)
        }
        binding.tvAlerts.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun getVehicleDetails(vehicleDetailResponse: VehicleDetailResponse) {

        for (item in vehicleDetailResponse.aaData[0].AlertForApp.indices) {

            if (vehicleDetailResponse.aaData[0].AlertForApp.toString().contains("Over-speed")
                || vehicleDetailResponse.aaData[0].AlertForApp.toString().contains("Main Battery Disconnection")
                || vehicleDetailResponse.aaData[0].AlertForApp.toString().contains("OverStoppage")
                || vehicleDetailResponse.aaData[0].AlertForApp.toString().contains(
                    "IgnitionOn"
                ) || vehicleDetailResponse.aaData[0].AlertForApp.toString().contains("POI Sms")
            ){
                Log.e("TagValues",vehicleDetailResponse.aaData[0].AlertForApp[item].AlertDetails)
            }

        }

        val vehiclePositionIcon = LatLng(
            vehicleDetailResponse.aaData.get(0).Lat.toDouble(),
            vehicleDetailResponse.aaData.get(0).Lng.toDouble()
        )
        Log.e("ChangeLat", vehicleDetailResponse.aaData.get(0).Lat)

        vehicleId = vehicleDetailResponse.aaData.get(0).Bbid.toString()
        vehicleName = vehicleDetailResponse.aaData.get(0).VehicleName.toString()
        if (mMap != null) {
            mMap.clear()
        }


        /*Set Details Data*/
        binding.tvLocation.text = vehicleDetailResponse.aaData[0].Location
        if (vehicleDetailResponse.aaData[0].Speed == "0") {
            marker = mMap.addMarker(
                MarkerOptions().position(vehiclePositionIcon)
                    //.title(vehicleDetailResponse.aaData[0].VehicleName)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_truck_green_top))!!
            )!!
            Log.e("TAG_CASE_CHECK", "Stopped")
            val cameraPosition = CameraPosition.Builder()
                .target(vehiclePositionIcon) // Sets the center of the map to Mountain View
                .zoom(14f)            // Sets the zoom
                .tilt(30f)            // Sets the tilt of the camera to 30 degrees
                .build()              // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            binding.tvProgressStatus.text = "Stopped"
            val days = vehicleDetailResponse.aaData[0].TotalParkingTime.substringBefore("-")
            val hours = vehicleDetailResponse.aaData[0].TotalParkingTime.substringAfter("-")
            if (days == "00") {
                var newStrg = hours
                val mString = newStrg.split(":").toTypedArray()
                if (mString[0] == "00") {
                    binding.tvTotalStatusTime.text = mString[1] + "M " + mString[2] + "S"
                } else {
                    binding.tvTotalStatusTime.text = mString[0] + "H " + mString[1] + "M"
                }

            } else {
                binding.tvTotalStatusTime.text = days + " Days"
            }
            if (vehicleDetailResponse.aaData[0].FuelStatus == "0") {
                binding.tvFuelStatus.text = "Inactive"
            } else {
                binding.tvFuelStatus.text = "Active"
            }
            binding.tvSpeed.text = vehicleDetailResponse.aaData[0].Speed + " k/h"
            binding.tvDistance.text = vehicleDetailResponse.aaData[0].Distance.toString() + " KM"

            //  time extraction calculation
            calculateStoppageTime(vehicleDetailResponse.aaData[0].StoppageTime)
            calculateLastStoppageTime(vehicleDetailResponse.aaData[0].ParkingTimeALasttStop)
            calculateTotalParkingTime(vehicleDetailResponse.aaData[0].TotalParkingTime)
            calculateMovingLastStopTime(vehicleDetailResponse.aaData[0].MovingFromLastStop)
            calculateTotalMovingTime(vehicleDetailResponse.aaData[0].TotalMoving)

            binding.tvIgnitionStatus.text = vehicleDetailResponse.aaData[0].IgnitionStatus
            binding.tvDataDate.text = vehicleDetailResponse.aaData[0].DataDateTime
            if (vehicleDetailResponse.aaData[0].BatteryStatus.contains("signal2.png")) {
                binding.tvBatteryStatus.text = "Low"
            }
            if (vehicleDetailResponse.aaData[0].BatteryStatus.contains("signal3.png")) {
                binding.tvBatteryStatus.text = "Charged"
            }
            // AC Status
            if (vehicleDetailResponse.aaData[0].AcStatus.contains("acoff.png")) {
                binding.tvAcStatus.text = "Off"
            } else {
                binding.tvAcStatus.text = "On"
            }
            if (vehicleDetailResponse.aaData[0].AcStatus.equals("NA")) {
                binding.tvAcStatus.text = "NA"
            }


        } else {

            binding.tvProgressStatus.text = "Moving"

            marker = mMap.addMarker(
                MarkerOptions().position(vehiclePositionIcon)
                    //.title(vehicleDetailResponse.aaData[0].VehicleName)
                    .anchor(0.5.toFloat(), 0.5.toFloat())
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_truck_green_top))!!
            )!!
            val latLng = LatLng( vehicleDetailResponse.aaData.get(0).Lat.toDouble(), vehicleDetailResponse.aaData.get(0).Lng.toDouble())
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            val days = vehicleDetailResponse.aaData[0].TotalMoving.substringBefore("-")
            val hours = vehicleDetailResponse.aaData[0].TotalMoving.substringAfter("-")
            if (days == "00") {
                var newStrg = hours
                val mString = newStrg.split(":").toTypedArray()
                if (mString[0] == "00") {
                    binding.tvTotalStatusTime.text = mString[1] + "M " + mString[2] + "S"
                } else {
                    binding.tvTotalStatusTime.text = mString[0] + "H " + mString[1] + "M"
                }

            } else {
                binding.tvTotalStatusTime.text = days + " Days"
            }

            // car location

            points.add(
                LatLng(
                    vehicleDetailResponse.aaData[0].Lat.toDouble(),
                    vehicleDetailResponse.aaData[0].Lng.toDouble()
                )
            )
            //latList.add(vehicleDetailResponse.aaData[0].Lat.toDouble())
            // lngList.add(vehicleDetailResponse.aaData[0].Lng.toDouble())
            if (points.size > 1) {
                val startLatitude: Double = points[points.size - 2].latitude
                val startLongitude: Double = points[points.size - 2].longitude
                val endLatitude: Double = points[points.size - 1].latitude
                val endLongitude: Double = points[points.size - 1].longitude
                CalculateBearingAngle(startLatitude,startLongitude,endLatitude,endLongitude)
                        // Creates a CameraPosition from the builder
              //  animateMarker(marker,LatLng(endLatitude,endLongitude),false)

            }

            binding.tvDataDate.text = "Updated at: " + vehicleDetailResponse.aaData[0].DataDateTime

            if (vehicleDetailResponse.aaData[0].FuelStatus == "0") {
                binding.tvFuelStatus.text = "Inactive"
            } else {
                binding.tvFuelStatus.text = "Active"
            }
            binding.tvSpeed.text = vehicleDetailResponse.aaData[0].Speed + "k/h"
            binding.tvDistance.text = vehicleDetailResponse.aaData[0].Distance.toString() + "KM"
            binding.tvDataDate.text = vehicleDetailResponse.aaData[0].DataDateTime
            calculateStoppageTime(vehicleDetailResponse.aaData[0].StoppageTime)
            calculateLastStoppageTime(vehicleDetailResponse.aaData[0].ParkingTimeALasttStop)
            calculateTotalParkingTime(vehicleDetailResponse.aaData[0].TotalParkingTime)
            calculateMovingLastStopTime(vehicleDetailResponse.aaData[0].MovingFromLastStop)
            calculateTotalMovingTime(vehicleDetailResponse.aaData[0].TotalMoving)

            binding.tvIgnitionStatus.text = vehicleDetailResponse.aaData[0].IgnitionStatus
        }
    }
    private fun animateMarkerForDrivingBehaviourRoute(
        myMap: GoogleMap, marker: Marker, directionPoint: List<LatLng>,
        hideMarker: Boolean
    ) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj = myMap.projection

        val duration: Long = 600
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            var i = 0
            override fun run() {


                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                try {
                    val newlocation = Location(directionPoint[i + 1].toString())
                    marker.setAnchor(0.5f, 0.5f)
                    bearingBetweenLocations(directionPoint[i], directionPoint[i + 1])
                    val rotate: Float = Angle_Bring
                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(directionPoint[i].latitude,directionPoint[i].longitude)) // Sets the center of the map to Mountain View
                        .zoom(17f)
                        .bearing(rotate)// Sets the zoom
                        // Sets the orientation of the camera to east
                        .tilt(30f)            // Sets the tilt of the camera to 30 degrees
                        .build()
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    marker.rotation = rotate
                    if (i < directionPoint.size) {
                        marker.position = directionPoint[i]
                    }
                    i++
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 100)
                    } else {
                        if (hideMarker) {
                            marker.isVisible = false
                        } else {
                            marker.isVisible = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }


    private fun bearingBetweenLocations(position: LatLng, position1: LatLng): Double {
        val PI = 3.14159
        val lat1 = position.latitude * PI / 180
        val long1 = position.longitude * PI / 180
        val lat2 = position1.latitude * PI / 180
        val long2 = position1.longitude * PI / 180
        val dLon = long2 - long1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon))
        var brng = Math.atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        Angle_Bring = brng.toFloat()
        return brng
    }


    fun animateMarker(
        marker: Marker, toPosition: LatLng,
        hideMarker: Boolean
    ) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj: Projection = mMap.getProjection()
        val startPoint: Point = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 5000
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t: Float = interpolator.getInterpolation(
                    elapsed.toFloat() /
                            duration
                )
                val lng = t * toPosition.longitude + (1 - t) *
                        startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) *
                        startLatLng.latitude

                marker.position = LatLng(lat, lng)
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    if (hideMarker) {
                        marker.isVisible = false
                    } else {
                        marker.isVisible = true
                    }
                }
            }
        })
    }

    private fun  bitmapDescriptorFromVector(context: Context, vectorResId:Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight())
        val bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
        val canvas =  Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    /*
    * Calculate stoppage time*/
    fun calculateStoppageTime(stoppageTime: String) {
        var sDays = stoppageTime.substringBefore("-")
        val sHours = stoppageTime.substringAfter("-")
        if (sDays == "00") {
            var newStrg = sHours
            val mString = newStrg.split(":").toTypedArray()
            if (mString[0] == "00") {
                binding.tvStoppageTime.text = mString[1] + "M " + mString[2] + "S "
            } else {
                binding.tvStoppageTime.text = mString[0] + "H " + mString[1] + "M "
            }

        } else {
            binding.tvStoppageTime.text = sDays + " Days"
        }
    }

    /*
* Calculate atLastStop time*/
    fun calculateLastStoppageTime(stoppageTime: String) {
        var sDays = stoppageTime.substringBefore("-")
        val sHours = stoppageTime.substringAfter("-")
        if (sDays == "00") {
            var newStrg = sHours
            val mString = newStrg.split(":").toTypedArray()
            if (mString[0] == "00") {
                binding.tvParkAtLastStop.text = mString[1] + "M " + mString[2] + "S "
            } else {
                binding.tvParkAtLastStop.text = mString[0] + "H " + mString[1] + "M "
            }

        } else {
            binding.tvParkAtLastStop.text = sDays + " Days"
        }
    }

    /*
* Calculate totalParking time*/
    fun calculateTotalParkingTime(stoppageTime: String) {
        var sDays = stoppageTime.substringBefore("-")
        val sHours = stoppageTime.substringAfter("-")
        if (sDays == "00") {
            var newStrg = sHours
            val mString = newStrg.split(":").toTypedArray()
            if (mString[0] == "00") {
                binding.tvParkingTimeTotal.text = mString[1] + "M " + mString[2] + "S"
            } else {
                binding.tvParkingTimeTotal.text = mString[0] + "H " + mString[1] + "M"
            }

        } else {
            binding.tvParkingTimeTotal.text = sDays + " Days"
        }
    }

    /*
* Calculate lastStopMoving time*/
    fun calculateMovingLastStopTime(stoppageTime: String) {
        var sDays = stoppageTime.substringBefore("-")
        val sHours = stoppageTime.substringAfter("-")
        if (sDays == "00") {
            var newStrg = sHours
            val mString = newStrg.split(":").toTypedArray()
            if (mString[0] == "00") {
                binding.tvMovingFromLastStop.text = mString[1] + "M " + mString[2] + "S"
            } else {
                binding.tvMovingFromLastStop.text = mString[0] + "H " + mString[1] + "M"
            }

        } else {
            binding.tvMovingFromLastStop.text = sDays + " Days"
        }
    }

    /*
* Calculate total moving time*/
    fun calculateTotalMovingTime(movingTime: String) {
        var sDays = movingTime.substringBefore("-")
        val sHours = movingTime.substringAfter("-")
        if (sDays == "00") {
            var newStrg = sHours
            val mString = newStrg.split(":").toTypedArray()
            if (mString[0] == "00") {
                binding.tvTotalMovingTime.text = mString[1] + "M " + mString[2] + "S"
            } else {
                binding.tvTotalMovingTime.text = mString[0] + "H " + mString[1] + "M"
            }

        } else {
            binding.tvTotalMovingTime.text = sDays + " Days"
        }
    }


    fun CalculateBearingAngle(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): Float {
        val Phi1 = Math.toRadians(startLatitude)
        val Phi2 = Math.toRadians(endLatitude)
        val DeltaLambda = Math.toRadians(endLongitude - startLongitude)
        val Theta: Double = atan2(
            sin(DeltaLambda) * cos(Phi2),
            cos(Phi1) * sin(Phi2) - sin(Phi1) * cos(Phi2) * cos(DeltaLambda)
        )
        Angle_Bring = Math.toDegrees(Theta).toFloat()
        return Math.toDegrees(Theta).toFloat()
    }

    override fun isNetworkConnected(): Boolean {
        if(com.humbhi.blackbox.ui.utils.Network.isNetworkAvailable(this)) {
            return true
        }
        return false
    }

    override fun isShowLoading(): Boolean {
        if (isFirstTime) {
            binding.fLayout.visibility = View.GONE
            binding.llProgress.progressLayout.visibility = View.VISIBLE
            isFirstTime = false
        }

        return true
    }

    override fun isHideLoading(): Boolean {
        binding.fLayout.visibility = View.VISIBLE
        binding.llProgress.progressLayout.visibility = View.GONE
        return false
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onPause() {
        handler.removeCallbacks(runnable) //stop handler when activity not visible
        super.onPause()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAlerts->{

                if (binding.rvAlerts.isShown){
                    binding.rvAlerts.visibility = View.GONE
                }
                else{
                    binding.rvAlerts.visibility = View.VISIBLE
                    if (alertList?.isNotEmpty() == true){
                        binding.rvAlerts.visibility = View.VISIBLE

                        val layoutManager = LinearLayoutManager(this)
                        binding.rvAlerts.layoutManager = layoutManager
                        adapter = VehicleAlertAdapter(this,alertList!!)
                        binding.rvAlerts.adapter = adapter
                        binding.nsScroll.fullScroll(View.FOCUS_UP)
                    }
                    else{
                        showErrorMessage("No alerts found for this vehicle.")
                        binding.rvAlerts.visibility = View.GONE
                    }
                }
            }
        }
    }
}