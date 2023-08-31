package com.humbhi.blackbox.ui.ui.routePlayback

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityRoutePlayBackBinding
import com.humbhi.blackbox.ui.Utility.LatLngEvaluator
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.models.DBVoilationCountsModel
import com.humbhi.blackbox.ui.data.models.DrivingBehaviourRouteDataModel
import com.humbhi.blackbox.ui.data.models.MarkerData
import com.humbhi.blackbox.ui.data.models.RoutePlaybackResponseModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.livetracking.LiveCarActivity
import com.humbhi.blackbox.ui.utils.ColouredPoint
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.Locale
import java.util.StringTokenizer
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class RoutePlayBack : AppCompatActivity(),RoutePlaybackView, OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    SeekBar.OnSeekBarChangeListener , RetrofitResponse {
    private lateinit var binding: ActivityRoutePlayBackBinding
    private lateinit var mPresenter: RoutePlaybackPresenter
    private lateinit var mMap: GoogleMap
    private lateinit var marker:Marker
    var Angle_Bring = 0f
    var points: ArrayList<LatLng> = ArrayList()
    private lateinit var progress: ProgressDialog
    private lateinit var tableName:String
    private lateinit var fromDate:String
    private lateinit var toDate:String
    private lateinit var vehicleName:String
    private lateinit var showStoppages:String
    private lateinit var flag:String
    var hm1: HashMap<Marker, Any>? = null
    var thread : Thread ?= null
    lateinit var  RoutePlaybackResponseModel: RoutePlaybackResponseModel
    private var movementSpeed:Int = 1500
    //    var  handler: Handler = Handler()
    private val mapTypes = arrayOf("Standard", "Satellite", "Terrain", "Hybrid")
    lateinit var DrivingBehaviourRouteDataModel: DrivingBehaviourRouteDataModel
    var playPauseString = "Pause"
    var playPause = false
    var i = 1
    var speed = ""
    var vehicleType = ""
    var start = false
    var startTime = ""
    var endTime = ""
    var backToLive = ""
    private var lastKnownPositionIndex = 0
    var animator : ObjectAnimator? = null
    var speedList: Double = 0.0
    var markerList : ArrayList<Marker> = ArrayList()
    var executor = Executors.newSingleThreadExecutor()
    var OriginalVoilationMarkersList: ArrayList<MarkerData> = ArrayList()
    var DuplicateVoilationMarkersList: ArrayList<MarkerData> = ArrayList()
    var VoilationMarkers: ArrayList<Marker> = ArrayList()
    var mainHandler = Handler(Looper.getMainLooper())
    var zoom = 0.0f
    var handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutePlayBackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progress = ProgressDialog(this)
        val mapFragment = supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mPresenter = RoutePlaybackPresenterImpl(this, DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster()))
        setToolbarDetails()
        speedControls()
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
        binding.stoppage.setOnClickListener {
            if (markerList.size==0) {
                binding.stoppage.setColorFilter(R.color.primary_main_orange)
                getStoppagesApi()
            }
            else{
                binding.stoppage.setColorFilter(R.color.black)
                for (marker in markerList) {
                    marker.remove()
                }
                markerList.clear()
            }
        }

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

    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Route Playback"
        binding.toolbar.ivBack.setOnClickListener {
            if(intent.hasExtra("backToLive")){
                val intent = Intent(this,LiveCarActivity::class.java)
                intent.putExtra("vehicleName",vehicleName)
                startActivity(intent)
                finish()
            }
            else {
                finish()
            }
        }
        binding.speedGauge.speedTo(100F)
        binding.cbHA.setOnCheckedChangeListener { _, isChecked -> updateMarkers("HA", isChecked)}
        binding.cbHB.setOnCheckedChangeListener { _, isChecked -> updateMarkers("HB", isChecked)}
        binding.cbRT.setOnCheckedChangeListener { _, isChecked -> updateMarkers("RT", isChecked)}
        binding.cbOS.setOnCheckedChangeListener { _, isChecked -> updateMarkers("OS", isChecked)}
        getIntentData()
    }

    private fun getIntentData()
    {
        if (intent.hasExtra("tableName")){
            tableName = intent.getStringExtra("tableName").toString()
            fromDate = intent.getStringExtra("fromDate").toString()
            toDate = intent.getStringExtra("endDate").toString()
            vehicleName = intent.getStringExtra("vehicleName").toString()
        //    showStoppages = intent.getStringExtra("showStoppages").toString()
            flag = intent.getStringExtra("flag").toString()
            if(intent.hasExtra("startTime")){
                startTime = intent.getStringExtra("startTime").toString()
            }
            else{
                startTime = "%2000:00:00"
            }
            if( intent.hasExtra("endTime")){
                endTime = intent.getStringExtra("endTime").toString()
            }
            else{
                endTime = "%2023:59:00"
            }
            binding.toolbar.tvTitle.text = vehicleName
        }
        else{
            startTime = "%2000:00:00"
            endTime = "%2023:59:00"
        }
    }

    private fun hitApi()
    {
        mPresenter.hitRoutePlaybackApi(
            tableName,
            fromDate + "%20${startTime}",
            toDate + "%20${endTime}"
        )
    }

    private fun hitDrivingBehaviourRouteAPI()
    {
        mPresenter.hitDrivingBehaviourRouteAPI(
            tableName,
            fromDate + "%20${startTime}",
            toDate + "%20${endTime}",
             vehicleName
        )
    }

    private fun getStoppagesApi()
    {
        Retrofit2(
            this,
            this,
            Constants.REQ_GET_ROUTE_STOPPAGES,
            Constants.GET_ROUTE_STOPPAGES.toString() + "tableName=" + tableName
                    + "&fromDate=" + fromDate + "%20${startTime}"+ "&toDate=" + toDate + "%20${endTime}")
            .callService(false)
    }

    override fun getRoutePlaybackResponse(routePlaybackResponseModel: RoutePlaybackResponseModel) {
        progress.dismiss()
        points.clear()
        if (mMap!=null){
            if (routePlaybackResponseModel.RouteDataList.isNotEmpty()) {
                RoutePlaybackResponseModel = routePlaybackResponseModel
                binding.tvTotalDistanceTravel.text = routePlaybackResponseModel.DistanceTravelled.replace("km(s)","")
                binding.tvDistance.text = routePlaybackResponseModel.DistanceTravelled.replace("km(s)","")
                for (i in 0 until routePlaybackResponseModel.RouteDataList.size){
                    points.add(LatLng(routePlaybackResponseModel.RouteDataList[i].Latitude,routePlaybackResponseModel.RouteDataList[i].Longitude))
                    speedList += routePlaybackResponseModel.RouteDataList[i].speed.toDouble()
                    Log.e("i",i.toString())
                }
                val result = (speedList / routePlaybackResponseModel.RouteDataList.size.toDouble()).toString()
                val formattedResult = String.format("%.2f", result.toDouble())
                binding.tvAverageSpeed.text = formattedResult
                // Start location market
                marker = mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            points[0].latitude,
                            points[0].longitude
                        )
                    )
                        .title("Start Location :" + routePlaybackResponseModel.RouteDataList[0].Location)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.ic_bxs_flag))!!
                )!!
                // set End Marker
                marker = mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            points[points.size - 1].latitude,
                            points[points.size - 1].longitude
                        )
                    )
                        .icon(bitmapDescriptorFromVector(this, R.drawable.green_flag))!!
                )!!
                binding.tvStartocation.text = routePlaybackResponseModel.RouteDataList[0].Location
                binding.tvEndLocation.text = routePlaybackResponseModel.RouteDataList[points.size-1].Location
                val b = LatLngBounds.Builder()
                for (m in routePlaybackResponseModel.RouteDataList) {
                    b.include(LatLng(m.Latitude,m.Longitude))
                }
                val bounds1 = b.build()
                val width = resources.displayMetrics.widthPixels
                val height = resources.displayMetrics.heightPixels
                val CamerLoc1 = CameraUpdateFactory.newLatLngBounds(bounds1, width, height, 150)
                mMap.animateCamera(CamerLoc1)
                val rotate = Angle_Bring.toDouble()

                if (flag.equals("DistanceReport")){
                    // hide car
                }
                else{
                    val scale = resources.displayMetrics.density
                    val dpAsPixels = (180 * scale + 0.5f)
                    mMap.setPadding(0,0,0,dpAsPixels.toInt())
                    //     CurrentDistance.setText("Distance: " + distancediff + " kms");
                    marker = mMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                points.get(0).latitude,
                                points.get(0).longitude
                            )
                        )
                            .flat(true)
                            .rotation(rotate.toFloat())
                    )!!

                }
                vehicleType = routePlaybackResponseModel.type
                if (vehicleType != "Other") {
                    if (vehicleType.equals("OilTanker", ignoreCase = true)) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_green_truck_final
                            )
                        )
                    } else if (vehicleType.equals("Car", ignoreCase = true)) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_car_green_with_shadow
                            )
                        )
                    }
                    else if (vehicleType.equals("Bus", ignoreCase = true)) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_new_bus_green
                            )
                        )
                    }
                    else if (vehicleType.equals(
                            "Ambulance",
                            ignoreCase = true
                        )
                    ) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_green_truck_final
                            )
                        )
                    } else if (vehicleType.equals(
                            "Truck",
                            ignoreCase = true
                        )
                    ) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_green_truck_final
                            )
                        )
                    } else if (vehicleType.equals("RoadRoller", ignoreCase = true)) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_car_green_with_shadow
                            )
                        )
                    } else {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_green_truck_final
                            )
                        )
                    }
                }
                else {
                    marker.setIcon(
                        bitmapDescriptorFromVector(
                            this,
                            R.drawable.ic_green_truck_final
                        )
                    )
                }
                marker.isFlat=true
                marker.setAnchor(0.5f,0.5f)
                if(points.size>1) {
                    bearingBetweenLocations(points[0], points[1])
                }
                if(!Angle_Bring.toString().equals("0.0")) {
                    marker.rotation = Angle_Bring
                }
                startAnim(points)

                if (flag.equals("DistanceReport")) {
                    binding.llRoutePlaybackDetails.visibility = View.GONE
                    binding.cvDistanceDetails.visibility = View.VISIBLE
                    val c = LatLngBounds.Builder()
                    for (m in routePlaybackResponseModel.RouteDataList) {
                        c.include(LatLng(m.Latitude,m.Longitude))
                    }
                    val bounds2 = c.build()
                    val width1 = resources.displayMetrics.widthPixels
                    val height1 = resources.displayMetrics.heightPixels
                    val CamerLoc2 = CameraUpdateFactory.newLatLngBounds(bounds2, width1, height1, 150)
                    mMap.animateCamera(CamerLoc2)
                }
            }
            else{
                progress.dismiss()
                binding.llRoutePlaybackDetails.visibility = View.GONE
                showErrorMessage("No route history found for this vehicle.")
            }
        }

//        if (showStoppages.equals("1")){
//            val scale = resources.displayMetrics.density
//            val dpAsPixels = (180 * scale + 0.5f)
//            mMap.setPadding(0,0,0,dpAsPixels.toInt())
//            getStoppagesApi()
//        }

    }

    override fun getDrivingBehavourRouteData(drivingBehaviourRouteDataModel: DrivingBehaviourRouteDataModel) {
        points.clear()
        if (mMap!=null){
            if (drivingBehaviourRouteDataModel.RouteDataList.isNotEmpty()) {
                getCounts()
                binding.tvTotalDistanceTravel.text = drivingBehaviourRouteDataModel.DistanceTravelled.replace("km(s)","")
                DrivingBehaviourRouteDataModel = drivingBehaviourRouteDataModel
                for (i in 0 until drivingBehaviourRouteDataModel.RouteDataList.size)
                {
                    points.add(LatLng(drivingBehaviourRouteDataModel.RouteDataList[i].Latitude, drivingBehaviourRouteDataModel.RouteDataList[i].Longitude))
                    OriginalVoilationMarkersList.add(MarkerData(drivingBehaviourRouteDataModel.RouteDataList[i].Latitude,
                        drivingBehaviourRouteDataModel.RouteDataList[i].Longitude,
                        drivingBehaviourRouteDataModel.RouteDataList[i].datatype,
                        drivingBehaviourRouteDataModel.RouteDataList[i].speed,
                        drivingBehaviourRouteDataModel.RouteDataList[i].Location,
                        drivingBehaviourRouteDataModel.RouteDataList[i].dDate,
                    ))
                }
                binding.tvDistance.text = drivingBehaviourRouteDataModel.DistanceTravelled.replace("km(s)","")
                binding.tvStartocation.text = drivingBehaviourRouteDataModel.RouteDataList[0].Location
                binding.tvEndLocation.text = drivingBehaviourRouteDataModel.RouteDataList[points.size-1].Location
                // Start location market
                marker = mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            drivingBehaviourRouteDataModel.RouteDataList[0].Latitude,
                            drivingBehaviourRouteDataModel.RouteDataList[0].Longitude
                        )
                    )
                        .title("Start Location :" + drivingBehaviourRouteDataModel.RouteDataList[0].Location)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.ic_bxs_flag))!!
                )!!

                // set End Marker
                marker = mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            points[points.size - 1].latitude,
                            points[points.size - 1].longitude
                        )
                    )
                        .title("End Location :" + drivingBehaviourRouteDataModel.RouteDataList[points.size-1].Location)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.green_flag))!!
                )!!
                val scale = resources.displayMetrics.density
                val dpAsPixels = (120 * scale + 0.5f)
                mMap.setPadding(0,0,0,dpAsPixels.toInt())
                val builder = LatLngBounds.Builder()
                for(cord in points){
                    builder.include(LatLng(cord.latitude,cord.longitude))
                }
                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                mMap.moveCamera(cu)

                Log.e("TotalListSize",drivingBehaviourRouteDataModel.RouteDataList.size.toString())

                if(drivingBehaviourRouteDataModel.RouteDataList.size>1) {
                    bearingBetweenLocations(points[i - 1], points[i])
                }

                val sourcePoints: ArrayList<ColouredPoint> = ArrayList()

                for (filetrData in drivingBehaviourRouteDataModel.RouteDataList.indices){
                    val latitude = drivingBehaviourRouteDataModel.RouteDataList[filetrData].Latitude
                    val longitude = drivingBehaviourRouteDataModel.RouteDataList[filetrData].Longitude
                    if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("RT")) {
                        sourcePoints.add(ColouredPoint(LatLng(latitude, longitude), ContextCompat.getColor(this, R.color.voilet_red)))
                    } else if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("OS")) {
                        sourcePoints.add(ColouredPoint(LatLng(latitude, longitude), ContextCompat.getColor(this, R.color.red)))
                    } else if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("HA")) {
                        sourcePoints.add(ColouredPoint(LatLng(latitude, longitude), ContextCompat.getColor(this, R.color.blue)))
                    } else if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("HB")) {
                        sourcePoints.add(ColouredPoint(LatLng(latitude, longitude), ContextCompat.getColor(this, R.color.secondary_yellow)))
                    }
                    else{
                        sourcePoints.add(ColouredPoint(LatLng(latitude, longitude), ContextCompat.getColor(this, R.color.black)))
                    }
                }
                binding.llDrivingVoilationIndicators.visibility = View.VISIBLE
                showPolyline(sourcePoints)
            }
            else{
                progress.dismiss()
                binding.cvDistanceDetails.visibility = View.GONE
                showErrorMessage("No route history found for this vehicle.")
            }
        }
    }

    // coloured polyline
    private fun showPolyline(points: List<ColouredPoint>) {
        if (points.size < 2) return
        var ix = 0
        var currentPoint: ColouredPoint = points[ix]
        var currentColor: Int = currentPoint.color
        val currentSegment: MutableList<LatLng> = ArrayList()
        currentSegment.add(currentPoint.coords)
        ix++
        while (ix < points.size) {
            currentPoint = points[ix]
            if (currentPoint.color === currentColor) {
                currentSegment.add(currentPoint.coords)
            } else {
                currentSegment.add(currentPoint.coords)
                mMap.addPolyline(
                    PolylineOptions()
                        .addAll(currentSegment)
                        .color(currentColor)
                        .width(8f)
                )
                currentColor = currentPoint.color
                currentSegment.clear()
                currentSegment.add(currentPoint.coords)
            }
            ix++
        }
        mMap.addPolyline(
            PolylineOptions()
                .addAll(currentSegment)
                .color(currentColor)
                .width(8f)
        )
    }

    private fun  bitmapDescriptorFromVector(context: Context, vectorResId:Int):BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas =  Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun isNetworkConnected(): Boolean {
        if(com.humbhi.blackbox.ui.utils.Network.isNetworkAvailable(this)) {
            return true
        }
        return false
    }

    override fun isShowLoading(): Boolean {
        progress.setTitle("Loading")
        progress.setMessage("Wait while loading...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress.show()
        return true
    }

    override fun isHideLoading(): Boolean {
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "", string)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        mMap.moveCamera(
            CameraUpdateFactory
                .newCameraPosition(
                    CameraPosition.Builder()
                        .target(LatLng(30.709588, 76.810326))
                        .zoom(11f)
                        .build()
                )
        )
        val currentZoomLevel = mMap.cameraPosition.zoom
        zoom = currentZoomLevel
        Log.e("FlaggGet",flag)
        if (flag.equals("DrivingBehaveRoute"))
        {
            Log.e("FlaggGet","Drivingggg")
            binding.llRoutePlaybackDetails.visibility = View.GONE
            binding.cvDistanceDetails.visibility = View.VISIBLE
            hitDrivingBehaviourRouteAPI()
        }
        else{
            Log.e("FlaggGet","NORMALLLL")
            hitApi()
        }
        mMap.setOnCameraIdleListener {
            // Get the current zoom level of the map
            val currentZoomLevel = mMap.cameraPosition.zoom
            zoom = currentZoomLevel
        }
    }

    override fun onMapLoaded() {

    }

    override fun onInfoWindowClick(p0: Marker) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun startAnim(pointsNew : ArrayList<LatLng>) {
        if (mMap != null) {
            if(flag == "DistanceReport"){
                val b = LatLngBounds.Builder()
                for (m in pointsNew) {
                    b.include(LatLng(m.latitude,m.longitude))
                }
                val bounds1 = b.build()
                val width = resources.displayMetrics.widthPixels
                val height = resources.displayMetrics.heightPixels
                val CamerLoc = CameraUpdateFactory.newLatLngBounds(bounds1, width, height, 150)
                mMap.animateCamera(CamerLoc)
            }
            val Points = ArrayList<LatLng>()
            val scale = resources.displayMetrics.density
            val dpAsPixels = (180 * scale + 0.5f)
            mMap.setPadding(0,0,0,dpAsPixels.toInt())
            val lineOptionsNew = PolylineOptions()
            lineOptionsNew.color(Color.BLACK)
            lineOptionsNew.width(8f)
            for(i in 0 until pointsNew.size-1) {
                val position = LatLng(pointsNew[i].latitude, pointsNew[i].longitude)
                val position1 = LatLng(pointsNew[i+1].latitude, pointsNew[i+1].longitude)
                Points.add(position)
                Points.add(position1)
            }
            lineOptionsNew.addAll(Points)
            try {
                mMap.addPolyline(lineOptionsNew)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(applicationContext, "Map not ready", Toast.LENGTH_LONG).show()
        }
    }

//    private fun startAnimForDriveingBehaviourRoute() {
//        if (mMap != null) {
//            MapAnimator.getInstance().animateRouteWithMultiplePoly(mMap, points,overSpeedpoints)
//        } else {
//            Toast.makeText(applicationContext, "Map not ready", Toast.LENGTH_LONG).show()
//        }
//    }


    private fun animateMarker(
        myMap: GoogleMap, marker: Marker, directionPoint: List<LatLng>,
        hideMarker: Boolean,routePlaybackResponseModel: RoutePlaybackResponseModel
    ) {
        try {
//            val builder = LatLngBounds.Builder()
//            for (marker in routePlaybackResponseModel.RouteDataList) {
//                builder.include(LatLng(marker.Latitude,marker.Longitude))
//            }
//// Build the bounds object
//            // Build the bounds object
//            val bounds = builder.build()
//// Animate the camera to show all markers within the bounds
//            // Animate the camera to show all markers within the bounds
//            val padding = 150 // adjust this as desired
//
//            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
//            mMap.animateCamera(cu)
            if(i==directionPoint.size){
                playPause = false
                playPauseString = "pause"
                animator!!.cancel()
                thread!!.interrupt()
                binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow_24))
                binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                binding.tvFourX.setTextColor(resources.getColor(R.color.black))
            }
            else{
                binding.tvCurrentSpeed.text = routePlaybackResponseModel.RouteDataList[i].speed
                binding.sSeekBar.progress = i
                binding.sSeekBar.max = routePlaybackResponseModel.RouteDataList.size
                binding.tvLocation.text = routePlaybackResponseModel.RouteDataList[i].Location
                binding.tvDateAndTime.text = routePlaybackResponseModel.RouteDataList[i].dDate
                binding.tvCurrentDistanceCover.text = routePlaybackResponseModel.RouteDataList[i].Cumuladistance
                animator?.cancel()
                animator = ObjectAnimator.ofObject(
                    marker,
                    "position",
                    LatLngEvaluator(),
                    marker.position,
                    directionPoint[i]
                )
                animator!!.duration = movementSpeed.toLong()
                if(i<directionPoint.size-1) {
                    handler.post {
                        val position2 = CameraPosition.builder()
                            .target(directionPoint[i])
                            .zoom(zoom)
                            .build()
                        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(position2))
                    }
                }
                animator!!.start()
                if(directionPoint.size>1) {
                    bearingBetweenLocations(directionPoint[i - 1], directionPoint[i])
                    val rotate: Float = Angle_Bring
                    if (Angle_Bring.toString() != "0.0") {
                        marker.rotation = rotate
                    }
                }
                marker.setAnchor(0.5f, 0.5f)
                marker.isFlat = true
            }
            val drivingDuration = routePlaybackResponseModel.Drivingduration
            if(!drivingDuration.equals("")) {
                val drivingDurationtokenizer = StringTokenizer(drivingDuration, "-")
                val drivingDurationDay = drivingDurationtokenizer.nextToken()
                val drivingDurationHMS = drivingDurationtokenizer.nextToken()
                val drivingDurationtokenizer1 = StringTokenizer(drivingDurationHMS, ":")
                val drivingDurationHourValue = drivingDurationtokenizer1.nextToken().toInt()
                val drivingDurationMinuteValue = drivingDurationtokenizer1.nextToken().toInt()
                val drivingDurationSecondValue = drivingDurationtokenizer1.nextToken().toInt()
                if (!drivingDurationDay.toString().equals("00")) {
                    val drivingDurationhour = drivingDurationDay.toInt() * 24 + drivingDurationHourValue
                    binding.Drivingduration.text = "${drivingDurationhour}H :" + drivingDurationMinuteValue + "M"
                } else {
                    binding.Drivingduration.text = "${drivingDurationHourValue}H :" + drivingDurationMinuteValue + "M"
                }
            }
            val Stoppageduration = routePlaybackResponseModel.Stoppageduration
            if(!Stoppageduration.equals("")) {
                val Stoppagedurationtokenizer = StringTokenizer(Stoppageduration, "-")
                val StoppagedurationDay = Stoppagedurationtokenizer.nextToken()
                val StoppagedurationHMS = Stoppagedurationtokenizer.nextToken()
                val Stoppagedurationtokenizer1 = StringTokenizer(StoppagedurationHMS, ":")
                val StoppagedurationHourValue = Stoppagedurationtokenizer1.nextToken().toInt()
                val StoppagedurationMinuteValue = Stoppagedurationtokenizer1.nextToken().toInt()
                val StoppagedurationSecondValue = Stoppagedurationtokenizer1.nextToken().toInt()
                if (!StoppagedurationDay.toString().equals("00")) {
                    val Stoppagedurationhour = StoppagedurationDay.toInt() * 24 + StoppagedurationHourValue
                    binding.Stoppageduration.text = "${Stoppagedurationhour}H :" + StoppagedurationMinuteValue + "M"
                } else {
                    binding.Stoppageduration.text = "${StoppagedurationHourValue}H :" + StoppagedurationMinuteValue + "M"
                }
            }
            val Idlingduration = routePlaybackResponseModel.Idlingduration
            if(!Idlingduration.equals("")) {
                val Idlingdurationtokenizer = StringTokenizer(Idlingduration, "-")
                val IdlingdurationDay = Idlingdurationtokenizer.nextToken()
                val IdlingdurationHMS = Idlingdurationtokenizer.nextToken()
                val Idlingdurationtokenizer1 = StringTokenizer(IdlingdurationHMS, ":")
                val IdlingdurationHourValue = Idlingdurationtokenizer1.nextToken().toInt()
                val IdlingdurationMinuteValue = Idlingdurationtokenizer1.nextToken().toInt()
                val IdlingdurationSecondValue = Idlingdurationtokenizer1.nextToken().toInt()
                if (!IdlingdurationDay.toString().equals("00")) {
                    val Idlingdurationhour = IdlingdurationDay.toInt() * 24 + IdlingdurationHourValue
                    binding.Idlingduration.text = "${Idlingdurationhour}H :" + IdlingdurationMinuteValue + "M"
                } else {
                    binding.Idlingduration.text =
                        "${IdlingdurationHourValue}H :" + IdlingdurationMinuteValue + "M"
                }
            }
            i++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bearingBetweenLocations(position: LatLng, position1: LatLng) {
        val PI = 3.14159
        val lat1 = position.latitude * PI / 180
        val long1 = position.longitude * PI / 180
        val lat2 = position1.latitude * PI / 180
        val long2 = position1.longitude * PI / 180
        val dLon = long2 - long1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)
        var brng = Math.atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        Angle_Bring = brng.toFloat()
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        try {
            var lat: Double
            var longi: Double
            val data = JSONArray(response!!.body()!!.string())
            for (i in 0 until data.length()) {
                val obj = data.getJSONObject(i)
                lat = obj.getDouble("StopLatitude")
                longi = obj.getDouble("StopLongitude")
                if (lat != 0.0 && longi != 0.0) {
                    mMap.addMarker(MarkerOptions().position(LatLng(lat, longi)).flat(true).icon(bitmapDescriptorFromVector(this, R.drawable.stop_marker )))
                        ?.let { markerList.add(it) }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun moveVehicle() {
        thread = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(movementSpeed.toLong())
                        runOnUiThread {
                            try {
                                animateMarker(
                                    mMap,
                                    marker,
                                    points,
                                    true,
                                    RoutePlaybackResponseModel
                                )
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                } catch (ignored: InterruptedException) {
                }
            }
        }
        thread!!.start()
    }

    // speed controls
    private fun speedControls() {
        binding.PlayAndPause.setOnClickListener {
            if (playPause == false) {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                when (speed) {
                    "1x" -> {
                        movementSpeed = 1500
                        binding.tvOneX.setTextColor(resources.getColor(R.color.white))
                        binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                        binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                        binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                    }

                    "2x" -> {
                        movementSpeed = 1500 / 2
                        binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                        binding.tvTwoX.setTextColor(resources.getColor(R.color.white))
                        binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                        binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                    }

                    "3x" -> {
                        movementSpeed = 1500 / 3
                        binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                        binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                        binding.tvThreeX.setTextColor(resources.getColor(R.color.white))
                        binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                    }

                    "4x" -> {
                        movementSpeed = 1500 / 4
                        binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                        binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                        binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                        binding.tvFourX.setTextColor(resources.getColor(R.color.white))
                    }

                    else -> {
                        movementSpeed = 1500
                    }
                }
                BottomSheetBehavior.from(binding.fBottomSheet).apply {
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
                moveVehicle()
                binding.llRoutePlaybackDetails.visibility = View.VISIBLE
                playPause = true
                binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                    this!!.setTint(resources.getColor(R.color.white))
                })
                playPauseString = "play"
                binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                binding.tvFourX.setTextColor(resources.getColor(R.color.black))
            } else {
//                binding.durationLayout.visibility  = View.GONE
//                binding.llLocation.visibility = View.GONE
                playPause = false
                playPauseString = "pause"
                movementSpeed = 600000000
                thread!!.interrupt()
                binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow_24))
                binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                binding.tvFourX.setTextColor(resources.getColor(R.color.black))
            }
        }

            binding.tvOneX.setOnClickListener {
//                binding.llLocation.visibility = View.VISIBLE
//                binding.durationLayout.visibility  = View.VISIBLE
                if (thread != null) {
                    binding.tvOneX.setTextColor(resources.getColor(R.color.white))
                    binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                    binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                    binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                    speed = "1x"
                    playPause = true
                    binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                        this!!.setTint(resources.getColor(R.color.white))
                    })
                    playPauseString = "play"
                    thread!!.interrupt()
                    movementSpeed = 1500
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
                }
            }

            binding.tvTwoX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                if (thread != null) {
                    binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                    binding.tvTwoX.setTextColor(resources.getColor(R.color.white))
                    binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                    binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                    speed = "2x"
                    playPause = true
                    binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                        this!!.setTint(resources.getColor(R.color.white))
                    })
                    playPauseString = "play"
                    thread!!.interrupt()
                    movementSpeed = 1500 / 2
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
                }
            }

            binding.tvThreeX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                if (thread != null) {
                    binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                    binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                    binding.tvThreeX.setTextColor(resources.getColor(R.color.white))
                    binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                    speed = "3x"
                    playPause = true
                    binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                        this!!.setTint(resources.getColor(R.color.white))
                    })
                    playPauseString = "play"
                    thread!!.interrupt()
                    movementSpeed = 1500 / 3
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
                }
            }

            binding.tvFourX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                if (thread != null) {
                    binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                    binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                    binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                    binding.tvFourX.setTextColor(resources.getColor(R.color.white))
                    speed = "4x"
                    playPause = true
                    binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                        this!!.setTint(resources.getColor(R.color.white))
                    })
                    playPauseString = "play"
                    thread!!.interrupt()
                    movementSpeed = 1500 / 4
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
                }
            }
    }

    // get exact location using latitude and longi
    fun getAddress(context: Context?, lat: Double, lng: Double) : String {
        val add = ""
        executor.execute(Runnable {
            val geocoder = Geocoder(context!!, Locale.getDefault())
            try {
                val addresses =
                    geocoder.getFromLocation(lat, lng, 1)
                val obj = addresses!![0]
                val add = obj.getAddressLine(0)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
        return add
    }

    override fun onDestroy() {
        super.onDestroy()
        if(animator!=null) {
            animator!!.cancel()
        }
        if(thread!=null){
            thread!!.interrupt()
        }
        handler.removeCallbacksAndMessages(null)
        finish()
    }

    private fun updateMarkers(data:String, isChecked:Boolean) {
        for (marker in VoilationMarkers) {
            if(marker.title.equals(data)) {
                marker.remove()
            }
        }
        DuplicateVoilationMarkersList.clear() // Clear all existing markers
        if (isChecked) {
            for (markers in OriginalVoilationMarkersList) {
                if (markers.dataType.equals(data)) {
                    DuplicateVoilationMarkersList.add(markers)
                    mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                        override fun getInfoContents(p0: Marker): View? {
                            if(!marker.title.equals("Start Location :") && !marker.title.equals("End Location :")) {
                                // Inflate the custom layout for the marker info window
                                val inflater = LayoutInflater.from(this@RoutePlayBack)
                                val view = inflater.inflate(R.layout.marker_layout_info, null)
                                // Find the views within the layout
                                val textTitle = view.findViewById<TextView>(R.id.Location)
                                val textDescription = view.findViewById<TextView>(R.id.DateAndTime)
                                val textSpeed = view.findViewById<TextView>(R.id.Speed)
                                // Customize the content of the marker info window
                                val markerData: MarkerData? = getMarkerData(p0)
                                if (markerData != null) {
                                    textTitle.text = "Location: " + markerData.location
                                    textDescription.text = "Date and Time :" + markerData.date
                                    textSpeed.text =
                                        "Speed :" + markerData.speed.toString() + " km/h"
                                }
                                return view
                            }
                            return null
                        }
                        override fun getInfoWindow(p0: Marker): View? {
                            return null // Return null to use the default info window
                        }
                    })
                }
            }
            for (markers in DuplicateVoilationMarkersList) {
                val position = LatLng(markers.latitude, markers.longitude)
                val markerOptions = MarkerOptions().position(position)
                when (data) {
                    "OS" -> markerOptions.icon(
                        bitmapDescriptorFromVector(this, R.drawable.os_marker)
                    ).title("OS")

                    "RT" -> markerOptions.icon(
                        bitmapDescriptorFromVector(this, R.drawable.rt_marker)
                    ).title("RT")

                    "HA" -> markerOptions.icon(
                        bitmapDescriptorFromVector(this, R.drawable.ha_marker)
                    ).title("HA")

                    "HB" -> markerOptions.icon(
                        bitmapDescriptorFromVector(this, R.drawable.hb_marker)
                    ).title("HB")
                }
                mMap.addMarker(markerOptions)?.let { VoilationMarkers.add(it) }
            }
        }
    }

    private fun getMarkerData(marker: Marker): MarkerData? {
        // Iterate over the list of results from liveStatus and find the matching marker
        for (result in OriginalVoilationMarkersList) {
            val markerPosition = marker.position
            val resultPosition = LatLng(result.latitude, result.longitude)
            if (markerPosition == resultPosition) {
                return result
            }
        }
        return null // No matching data found
    }

    fun getCounts(){
        executor.execute{
            val api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
            api.getVoilationCounts(tableName, fromDate + "%20${startTime}",toDate + "%20${endTime}").enqueue(object: Callback<DBVoilationCountsModel>{
                override fun onResponse(
                    call: Call<DBVoilationCountsModel>,
                    response: Response<DBVoilationCountsModel>
                ) {
                    mainHandler.post{
                        progress.dismiss()
                        val responseX = response.body()
                        if (responseX != null) {
                            binding.hbCount.text = responseX.data[0].HBc.toString()
                            binding.haCount.text = responseX.data[0].HAc.toString()
                            binding.rtCount.text = responseX.data[0].RTc.toString()
                            binding.osCount.text = responseX.data[0].OSc.toString()
                        }
                    }
                }

                override fun onFailure(call: Call<DBVoilationCountsModel>, t: Throwable) {
                    mainHandler.post{
                        progress.dismiss()
                        if (t is SocketTimeoutException) {
                            Constants.alertDialog(
                                this@RoutePlayBack,
                                "Please check your network connection"
                            )
                        } else {
                            Constants.alertDialog(
                                this@RoutePlayBack,
                                "Something went wrong"
                            )
                        }
                    }
                }
            })
        }
    }

}