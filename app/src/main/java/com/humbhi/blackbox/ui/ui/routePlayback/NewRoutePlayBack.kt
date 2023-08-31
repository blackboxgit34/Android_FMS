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
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.humbhi.blackbox.ui.data.models.DrivingBehaviourRouteDataModel
import com.humbhi.blackbox.ui.data.models.RoutePlaybackResponseModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.livetracking.LiveCarActivity
import com.humbhi.blackbox.ui.utils.ColouredPoint
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Response
import java.io.IOException
import java.util.StringTokenizer

class NewRoutePlayBack : AppCompatActivity(),RoutePlaybackView, OnMapReadyCallback,
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
    private var movementSpeed:Int = 4000
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
    var handler = Handler(Looper.getMainLooper())
    private var currentSpeed = 1 // Default speed is 1x
    private var isPlaying = false // Default state is pausedF
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
            Constants.GET_ROUTE_STOPPAGES.toString() + "tableName=" + tableName + "&fromDate=" + fromDate + "%20${startTime}"+ "&toDate=" + toDate + "%20${endTime}")
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
            speedList += routePlaybackResponseModel.RouteDataList[i].speed.toDouble()
            Log.e("i",i.toString())
        }
        val result = (speedList / routePlaybackResponseModel.RouteDataList.size.toDouble()).toString()
        val formattedResult = String.format("%.2f", result.toDouble())
        binding.tvAverageSpeed.text = formattedResult
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
        for (i in 0 until drivingBehaviourRouteDataModel.RouteDataList.size)
        {
            points.add(LatLng(drivingBehaviourRouteDataModel.RouteDataList[i].Latitude, drivingBehaviourRouteDataModel.RouteDataList[i].Longitude))
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
                vehicleType = drivingBehaviourRouteDataModel.type
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
                if(drivingBehaviourRouteDataModel.RouteDataList.size>1) {
                    bearingBetweenLocations(points[i - 1], points[i])
                }
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
            }
            else {
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

    private fun bitmapDescriptorFromVector(context: Context, vectorResId:Int):BitmapDescriptor {
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
        progress.dismiss()
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "", string)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.moveCamera(
            CameraUpdateFactory
                .newCameraPosition(
                    CameraPosition.Builder()
                        .target(LatLng(30.709588, 76.810326))
                        .zoom(15.5f)
                        .build()
                )
        )
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
        myMap: GoogleMap,
        marker: Marker,
        directionPoint: List<LatLng>,
        hideMarker: Boolean,
        routePlaybackResponseModel: RoutePlaybackResponseModel
    ) {
        try {
            val loopDelay = when (currentSpeed) {
                1 -> 4000L
                2 -> 3000L
                3 -> 2000L
                4 -> 1000L
                else -> 4000L
            }

            // Loop through the directionPoint with delay based on currentSpeed
            for (i in 0 until directionPoint.size) {
                if (!isPlaying) {
                    // Pause the animation loop if not playing
                    break
                }

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
                animator!!.start()

                val position2 = CameraPosition.builder()
                    .target(directionPoint[i])
                    .zoom(16f)
                    .build()
                myMap.moveCamera(CameraUpdateFactory.newCameraPosition(position2))

                if (i > 0) {
                    bearingBetweenLocations(directionPoint[i - 1], directionPoint[i])
                    val rotate: Float = Angle_Bring
                    if (Angle_Bring.toString() != "0.0") {
                        marker.rotation = rotate
                    }
                }

                marker.setAnchor(0.5f, 0.5f)
                marker.isFlat = true

                // Delay between each iteration based on currentSpeed
                Thread.sleep(loopDelay)
            }

            // Reset the animation and other states after the loop
            animator?.cancel()
            playPause = false
            playPauseString = "pause"
            binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow_24))
            binding.tvOneX.setTextColor(resources.getColor(R.color.black))
            binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
            binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
            binding.tvFourX.setTextColor(resources.getColor(R.color.black))

            // Rest of the code...

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

    private fun moveVehicle() {
        if (flag == "DrivingBehaveRoute") {
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
        } else {
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

    // speed controls
    private fun speedControls() {
        binding.PlayAndPause.setOnClickListener {
            if (playPause == false) {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                when (currentSpeed) {
                    1 -> {
                        movementSpeed = 4000
                    }
                    2 -> {
                        movementSpeed = 3000
                    }
                    3 -> {
                        movementSpeed = 2000
                    }
                    4 -> {
                        movementSpeed = 1000
                    }
                    else -> {
                        movementSpeed = 4000
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
                if(animator!=null) {
                    animator!!.pause()
                }
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
                currentSpeed = 1
                if(animator!=null) {
                    animator!!.resume()
                }
                BottomSheetBehavior.from(binding.fBottomSheet).apply {
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            binding.tvTwoX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                binding.tvTwoX.setTextColor(resources.getColor(R.color.white))
                binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
                binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                currentSpeed = 2
                playPause = true
                binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                    this!!.setTint(resources.getColor(R.color.white))
                })
                playPauseString = "play"
                if(animator!=null) {
                    animator!!.resume()
                }
                BottomSheetBehavior.from(binding.fBottomSheet).apply {
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            binding.tvThreeX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
                binding.tvOneX.setTextColor(resources.getColor(R.color.black))
                binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
                binding.tvThreeX.setTextColor(resources.getColor(R.color.white))
                binding.tvFourX.setTextColor(resources.getColor(R.color.black))
                currentSpeed = 3
                playPause = true
                binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                    this!!.setTint(resources.getColor(R.color.white))
                })
                playPauseString = "play"
                if(animator!=null) {
                    animator!!.resume()
                }
                BottomSheetBehavior.from(binding.fBottomSheet).apply {
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        binding.tvFourX.setOnClickListener {
//                binding.durationLayout.visibility  = View.VISIBLE
//                binding.llLocation.visibility = View.VISIBLE
            binding.tvOneX.setTextColor(resources.getColor(R.color.black))
            binding.tvTwoX.setTextColor(resources.getColor(R.color.black))
            binding.tvThreeX.setTextColor(resources.getColor(R.color.black))
            binding.tvFourX.setTextColor(resources.getColor(R.color.white))
            currentSpeed = 4
            playPause = true
            binding.playPauseIcon.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_24).apply {
                this!!.setTint(resources.getColor(R.color.white))
            })
            playPauseString = "play"
            if(animator!=null) {
                animator!!.resume()
            }
            BottomSheetBehavior.from(binding.fBottomSheet).apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

}