package com.humbhi.blackbox.ui.ui.routePlayback

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityRoutePlayBackBinding
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.models.DrivingBehaviourRouteDataModel
import com.humbhi.blackbox.ui.data.models.RoutePlaybackResponseModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.ColouredPoint
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Response
import java.io.IOException
import java.util.*

class RoutePlayBack : AppCompatActivity(),RoutePlaybackView, OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    SeekBar.OnSeekBarChangeListener , RetrofitResponse {
    private lateinit var binding: ActivityRoutePlayBackBinding
    private lateinit var mPresenter: RoutePlaybackPresenter
    private lateinit var mMap: GoogleMap
    private lateinit var marker:Marker
    private lateinit var marker2:Marker
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
    lateinit var  RoutePlaybackResponseModel: RoutePlaybackResponseModel
    private var movementSpeed:Int = 500
    //    var  handler: Handler = Handler()
    private val mapTypes = arrayOf("Standard", "Satellite", "Terrain", "Hybrid")
    var runnable: Runnable ?= null
    lateinit var DrivingBehaviourRouteDataModel: DrivingBehaviourRouteDataModel
    var playPauseString = "Pause"
    var playPause = false
    var thread: Thread ?= null
    var i = 1
    var speed = ""
    var vehicleType = ""
    var start = false
    var startTime = ""
    var endTime = ""

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
        thread = Thread()
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
            if(thread != null){
                thread!!.interrupt()
            }
            finish()
        }
        binding.speedGauge.speedTo(100F)
        getIntentData()
    }

    private fun getIntentData()
    {
        if (intent.hasExtra("tableName")){
            tableName = intent.getStringExtra("tableName").toString()
            fromDate = intent.getStringExtra("fromDate").toString()
            toDate = intent.getStringExtra("endDate").toString()
            vehicleName = intent.getStringExtra("vehicleName").toString()
            showStoppages = intent.getStringExtra("showStoppages").toString()
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
                toDate + "%20${endTime}"
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
        points.clear()
        Log.e("VALUE_CHECK",routePlaybackResponseModel.DistanceTravelled)
        RoutePlaybackResponseModel = routePlaybackResponseModel
        binding.tvTotalDistanceTravel.text = routePlaybackResponseModel.DistanceTravelled.replace("km(s)","")
        binding.tvDistance.text = routePlaybackResponseModel.DistanceTravelled.replace("km(s)","")
        for (i in 0 until routePlaybackResponseModel.RouteDataList.size){
            points.add(LatLng(routePlaybackResponseModel.RouteDataList[i].Latitude,routePlaybackResponseModel.RouteDataList[i].Longitude))
            Log.e("i",i.toString())
        }
        if (mMap!=null){
            if (routePlaybackResponseModel.RouteDataList.isNotEmpty()) {
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
                    } else if (vehicleType.equals("Bus", ignoreCase = true)) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_new_bus_green
                            )
                        )
                    } else if (vehicleType.equals(
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
                bearingBetweenLocations(points[0], points[1])
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
                binding.llRoutePlaybackDetails.visibility = View.GONE
                showErrorMessage("No route history found for this vehicle.")
            }
        }

        if (showStoppages.equals("1")){
            val scale = resources.displayMetrics.density
            val dpAsPixels = (180 * scale + 0.5f)
            mMap.setPadding(0,0,0,dpAsPixels.toInt())
            getStoppagesApi()
        }

    }

    override fun getDrivingBehavourRouteData(drivingBehaviourRouteDataModel: DrivingBehaviourRouteDataModel) {
        points.clear()
        binding.tvTotalDistanceTravel.text = drivingBehaviourRouteDataModel.DistanceTravelled.replace("km(s)","")
        DrivingBehaviourRouteDataModel = drivingBehaviourRouteDataModel
        for (i in 0 until drivingBehaviourRouteDataModel.RouteDataList.size){
            points.add(LatLng(drivingBehaviourRouteDataModel.RouteDataList[i].Latitude.toDouble(),drivingBehaviourRouteDataModel.RouteDataList[i].Longitude.toDouble()))
        }

        if (mMap!=null){
            if (drivingBehaviourRouteDataModel.RouteDataList.isNotEmpty()) {
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
                        .icon(bitmapDescriptorFromVector(this, R.drawable.green_flag))!!
                )!!
                val scale = resources.displayMetrics.density
                val dpAsPixels = (180 * scale + 0.5f)
                mMap.setPadding(0,0,0,dpAsPixels.toInt())
                val builder = LatLngBounds.Builder()
                builder.include(points.get(0))
                builder.include(points.get(points.size - 1))
                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                mMap.moveCamera(cu)
                val c = LatLngBounds.Builder()
                for (m in drivingBehaviourRouteDataModel.RouteDataList) {
                    c.include(LatLng(m.Latitude,m.Longitude))
                }
                val bounds1 = c.build()
                val width = resources.displayMetrics.widthPixels
                val height = resources.displayMetrics.heightPixels
                val CamerLoc = CameraUpdateFactory.newLatLngBounds(bounds1, width, height, 200)
                mMap.animateCamera(CamerLoc)

                Log.e("TotalListSize",drivingBehaviourRouteDataModel.RouteDataList.size.toString())

                //timerworking();
                //    val rotate = Angle_Bring.toDouble()
                marker = mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            points.get(0).latitude,
                            points.get(0).longitude
                        )
                    )
                        .flat(true)
                )!!

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
                    } else if (vehicleType.equals("Bus", ignoreCase = true)) {
                        marker.setIcon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_green_truck_final
                            )
                        )
                    } else if (vehicleType.equals(
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
                bearingBetweenLocations(points[i-1], points[i])
                if(!Angle_Bring.toString().equals("0.0")) {
                    marker.rotation = Angle_Bring
                }

                val sourcePoints: ArrayList<ColouredPoint> = ArrayList()

                for (filetrData in drivingBehaviourRouteDataModel.RouteDataList.indices){
                    if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("OS")){
                        sourcePoints.add(ColouredPoint(LatLng(drivingBehaviourRouteDataModel.RouteDataList[filetrData].Latitude,drivingBehaviourRouteDataModel.RouteDataList[filetrData].Longitude), R.color.voilet_red))
                    }
                    if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("RT")){
                        sourcePoints.add(ColouredPoint(LatLng(drivingBehaviourRouteDataModel.RouteDataList[filetrData].Latitude,drivingBehaviourRouteDataModel.RouteDataList[filetrData].Longitude), R.color.red))
                    }
                    if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("HA")){
                        sourcePoints.add(ColouredPoint(LatLng(drivingBehaviourRouteDataModel.RouteDataList[filetrData].Latitude,drivingBehaviourRouteDataModel.RouteDataList[filetrData].Longitude),  R.color.blue))
                    }
                    if (drivingBehaviourRouteDataModel.RouteDataList[filetrData].datatype.equals("HB")){
                        sourcePoints.add(ColouredPoint(LatLng(drivingBehaviourRouteDataModel.RouteDataList[filetrData].Latitude,drivingBehaviourRouteDataModel.RouteDataList[filetrData].Longitude),  R.color.secondary_yellow))
                    }
                    else{
                        sourcePoints.add(ColouredPoint(LatLng(drivingBehaviourRouteDataModel.RouteDataList[filetrData].Latitude,drivingBehaviourRouteDataModel.RouteDataList[filetrData].Longitude),  R.color.black))
                    }
                }
                binding.llDrivingVoilationIndicators.visibility = View.VISIBLE
                showPolyline(sourcePoints)
            }
            else{
                binding.llRoutePlaybackDetails.visibility = View.GONE
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
            if (currentPoint.color == currentColor) {
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
        return true
    }

    override fun isShowLoading(): Boolean {
        progress.setTitle("Loading")
        progress.setMessage("Wait while loading...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress.show()
        return true
    }

    override fun isHideLoading(): Boolean {
        progress.dismiss()
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "", string)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        Log.e("FlaggGet",flag)
        if (flag.equals("DrivingBehaveRoute"))
        {
            Log.e("FlaggGet","Drivingggg")
            binding.durationLayout.visibility = View.GONE
            binding.llRoutePlaybackDetails.visibility = View.VISIBLE
            hitDrivingBehaviourRouteAPI()
        }
        else{
            Log.e("FlaggGet","NORMALLLL")
            hitApi()
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
            if (i < directionPoint.size) {
                binding.tvCurrentSpeed.text = routePlaybackResponseModel.RouteDataList[i].speed
                binding.sSeekBar.progress = i
                binding.sSeekBar.max = routePlaybackResponseModel.RouteDataList.size
                binding.tvLocation.text = routePlaybackResponseModel.RouteDataList[i].Location
                binding.tvDateAndTime.text = routePlaybackResponseModel.RouteDataList[i].dDate
                binding.tvCurrentDistanceCover.text = routePlaybackResponseModel.RouteDataList[i].Cumuladistance
                marker.position = directionPoint[i]
                val position2 = CameraPosition.builder()
                    .target(directionPoint[i])
                    .zoom(16f)
                    .build()
                myMap.moveCamera(CameraUpdateFactory.newCameraPosition(position2))
                bearingBetweenLocations(directionPoint[i-1], directionPoint[i])
                val rotate: Float = Angle_Bring
                if(Angle_Bring.toString() != "0.0") {
                    marker.rotation = rotate
                }
                marker.setAnchor(0.5f, 0.5f)
                marker.isFlat = true
            }
            else {
                playPause = false
                playPauseString = "pause"
                binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow_24))
                binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                }

            val drivingDuration = routePlaybackResponseModel.Drivingduration
            val drivingDurationtokenizer = StringTokenizer(drivingDuration,"-")
            val drivingDurationDay = drivingDurationtokenizer.nextToken()
            val drivingDurationHMS = drivingDurationtokenizer.nextToken()
            val drivingDurationtokenizer1 = StringTokenizer(drivingDurationHMS,":")
            val drivingDurationHourValue = drivingDurationtokenizer1.nextToken().toInt()
            val drivingDurationMinuteValue = drivingDurationtokenizer1.nextToken().toInt()
            val drivingDurationSecondValue = drivingDurationtokenizer1.nextToken().toInt()
            if(!drivingDurationDay.toString().equals("00")){
                val drivingDurationhour = drivingDurationDay.toInt()*24+drivingDurationHourValue
                binding.Drivingduration.text= "${drivingDurationhour}H :"+drivingDurationMinuteValue+"M"
            }
            else{
                binding.Drivingduration.text= "${drivingDurationHourValue}H :"+drivingDurationMinuteValue+"M"
            }
            val Stoppageduration = routePlaybackResponseModel.Stoppageduration
            val Stoppagedurationtokenizer = StringTokenizer(Stoppageduration,"-")
            val StoppagedurationDay = Stoppagedurationtokenizer.nextToken()
            val StoppagedurationHMS = Stoppagedurationtokenizer.nextToken()
            val Stoppagedurationtokenizer1 = StringTokenizer(StoppagedurationHMS,":")
            val StoppagedurationHourValue = Stoppagedurationtokenizer1.nextToken().toInt()
            val StoppagedurationMinuteValue = Stoppagedurationtokenizer1.nextToken().toInt()
            val StoppagedurationSecondValue = Stoppagedurationtokenizer1.nextToken().toInt()
            if(!StoppagedurationDay.toString().equals("00")){
                val Stoppagedurationhour = StoppagedurationDay.toInt()*24+StoppagedurationHourValue
                binding.Stoppageduration.text= "${Stoppagedurationhour}H :"+StoppagedurationMinuteValue+"M"
            }
            else{
                binding.Stoppageduration.text= "${StoppagedurationHourValue}H :"+StoppagedurationMinuteValue+"M"
            }
            val Idlingduration = routePlaybackResponseModel.Idlingduration
            val Idlingdurationtokenizer = StringTokenizer(Idlingduration,"-")
            val IdlingdurationDay = Idlingdurationtokenizer.nextToken()
            val IdlingdurationHMS = Idlingdurationtokenizer.nextToken()
            val Idlingdurationtokenizer1 = StringTokenizer(IdlingdurationHMS,":")
            val IdlingdurationHourValue = Idlingdurationtokenizer1.nextToken().toInt()
            val IdlingdurationMinuteValue = Idlingdurationtokenizer1.nextToken().toInt()
            val IdlingdurationSecondValue = Idlingdurationtokenizer1.nextToken().toInt()
            if(!IdlingdurationDay.toString().equals("00")){
                val Idlingdurationhour = IdlingdurationDay.toInt()*24+IdlingdurationHourValue
                binding.Idlingduration.text= "${Idlingdurationhour}H :"+IdlingdurationMinuteValue+"M"
            }
            else{
                binding.Idlingduration.text= "${IdlingdurationHourValue}H :"+IdlingdurationMinuteValue+"M"
            }
            i++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun animateMarkerForDrivingBehaviourRoute(
        myMap: GoogleMap, marker: Marker, directionPoint: List<LatLng>,
        hideMarker: Boolean,routePlaybackResponseModel: DrivingBehaviourRouteDataModel
    ) {
        try {
            if (i < directionPoint.size) {
                bearingBetweenLocations(directionPoint[i-1], directionPoint[i])
                val rotate: Float = Angle_Bring
                if(Angle_Bring.toString() != "0.0") {
                    marker.rotation = rotate
                }
                marker.isFlat = true
                marker.setAnchor(0.5f, 0.5f)
                marker.position = directionPoint[i]
                val position2 = CameraPosition.builder()
                    .target(directionPoint[i])
                    .zoom(16f)
                    .build()
                myMap.moveCamera(CameraUpdateFactory.newCameraPosition(position2))
                binding.tvCurrentSpeed.text = routePlaybackResponseModel.RouteDataList[i].speed
                binding.sSeekBar.progress = i
                binding.sSeekBar.max = routePlaybackResponseModel.RouteDataList.size
                binding.tvLocation.text = routePlaybackResponseModel.RouteDataList[i].Location
                binding.tvDateAndTime.text = routePlaybackResponseModel.RouteDataList[i].dDate
                binding.tvCurrentDistanceCover.text = routePlaybackResponseModel.RouteDataList[i].Cumuladistance
            }
            else{
                playPause = false
                playPauseString = "pause"
                binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow_24))
                binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                binding.tvFourX.setTextColor(resources.getColor(R.color.black))
            }
            i++
        }
        catch (e: Exception) {
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, longi), 14.0f))
                    marker2 = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lat, longi)).flat(true).icon(bitmapDescriptorFromVector(this, R.drawable.ic_bxs_map_pin))
                    )!!
                    marker.position = LatLng(lat,longi)
                    hm1?.put(marker2, obj)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun moveVehicle(){
        if (flag == "DrivingBehaveRoute") {
            thread = object : Thread() {
                override fun run() {
                    try {
                        while (!isInterrupted) {
                            sleep(movementSpeed.toLong())
                            runOnUiThread {
                                try {
                                    animateMarkerForDrivingBehaviourRoute(
                                        mMap,
                                        marker,
                                        points,
                                        true,
                                        DrivingBehaviourRouteDataModel
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
        else{
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
    }

    // speed controls
    private fun speedControls() {
        binding.PlayAndPause.setOnClickListener {
            if (playPause == false) {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                when (speed) {
                    "1x" -> {
                        movementSpeed = 200
                    }
                    "2x" -> {
                        movementSpeed = 100
                    }
                    "3x" -> {
                        movementSpeed = 70
                    }
                    "4x" -> {
                        movementSpeed = 40
                    }
                    else -> {
                        movementSpeed = 500
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

            binding.tvOneX.setOnClickListener {
//                binding.llLocation.visibility = View.VISIBLE
//                binding.durationLayout.visibility  = View.VISIBLE
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
                    movementSpeed = 200
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
            }

            binding.tvTwoX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
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
                    movementSpeed = 100
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
            }

            binding.tvThreeX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
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
                    movementSpeed = 70
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
                }
            }

            binding.tvFourX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
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
                    movementSpeed = 40
                    BottomSheetBehavior.from(binding.fBottomSheet).apply {
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    moveVehicle()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(thread != null){
            thread!!.interrupt()
        }
        finish()
    }

}