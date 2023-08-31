package com.humbhi.blackbox.ui.ui.livetracking

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityLiveTrackingBinding
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.LiveTrackingResponse
import com.humbhi.blackbox.ui.data.models.LiveTrackingVehicleDataResponse
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.utils.Constants

class LiveTrackingActivity : AppCompatActivity(), View.OnClickListener, LiveTrackingView, OnMapReadyCallback{
    private lateinit var binding: ActivityLiveTrackingBinding
    private lateinit var liveTrackingPresenter: LiveTrackingPresenter
    private var clusterManager: ClusterManager<LiveTrackingVehicleDataResponse>? = null
    private var mMap: GoogleMap? = null
    private lateinit var custId: String
    val latlng: ArrayList<LatLng> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
        liveTrackingPresenter = LiveTrackingPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMasterSecure())
        )
        binding.toolbar.tvTitle.text = "Live Tracking"
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        BottomSheetBehavior.from(binding.fBottomSheet).apply {
            peekHeight = 300
            this.state = BottomSheetBehavior.STATE_HIDDEN

        }
        //custId = CommonData.getCustIdFromDB()
        custId = CommonData.getCustIdFromDB()
        Log.e("CustIdCheck", custId)
        liveTrackingPresenter.hitLiveTrackingVehicleDataApi(custId, "","","", "0", 0, 70, "", "0", "")
        binding.llTotal.setOnClickListener(this)
        binding.rbTotal.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llTotal -> {
                if (binding.llRunning.visibility == View.GONE) {
                    binding.llRunning.visibility = View.VISIBLE
                    binding.llIdeal.visibility = View.VISIBLE
                    binding.llStopped.visibility = View.VISIBLE
                    binding.llInactive.visibility = View.VISIBLE
                    binding.llTowed.visibility = View.VISIBLE
                } else {
                    binding.llRunning.visibility = View.GONE
                    binding.llIdeal.visibility = View.GONE
                    binding.llStopped.visibility = View.GONE
                    binding.llInactive.visibility = View.GONE
                    binding.llTowed.visibility = View.GONE
                }

            }
            R.id.rbTotal -> {
                if (binding.llRunning.visibility == View.GONE) {
                    binding.llRunning.visibility = View.VISIBLE
                    binding.llIdeal.visibility = View.VISIBLE
                    binding.llStopped.visibility = View.VISIBLE
                    binding.llInactive.visibility = View.VISIBLE
                    binding.llTowed.visibility = View.VISIBLE
                } else {
                    binding.llRunning.visibility = View.GONE
                    binding.llIdeal.visibility = View.GONE
                    binding.llStopped.visibility = View.GONE
                    binding.llInactive.visibility = View.GONE
                    binding.llTowed.visibility = View.GONE
                }

            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun getLiveVehicleDetail(liveTrackingResponse: LiveTrackingResponse) {


        for (i in 0 until liveTrackingResponse.aaData.size) {
            latlng.add(
                LatLng(
                    liveTrackingResponse.aaData[i].Latitude,
                    liveTrackingResponse.aaData[i].Longitude
                )
            )

            mMap?.addMarker(
                MarkerOptions().position(latlng[i])
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_truck_green_top))!!
            )
            val cameraPosition = CameraPosition.Builder()
                .target(latlng[0])
                .zoom(7f)
                .build()
            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            clusterManager = ClusterManager(this, mMap)
            mMap?.setOnCameraIdleListener(clusterManager)
            mMap?.setOnMarkerClickListener(clusterManager)
            addItems(liveTrackingResponse.aaData[0].Latitude,liveTrackingResponse.aaData[0].Longitude,liveTrackingResponse.aaData[i],latlng)
        }

    }


    private fun  bitmapDescriptorFromVector(context: Context, vectorResId:Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight())
        val bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
        val canvas =  Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun addItems(
        lati: Double,
        longi: Double,
        liveTrackingVehicleDataResponse: LiveTrackingVehicleDataResponse,
        latlng: ArrayList<LatLng>
    ) {

        // Set some lat/lng coordinates to start with.
        var lat = lati
        var lng = longi

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0 until latlng.size) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            val offsetItem = LiveTrackingVehicleDataResponse(latlng[i].latitude,latlng[i].longitude,"Location text")
            clusterManager!!.addItem(offsetItem)
        }
    }

    override fun isNetworkConnected(): Boolean {
        if(com.humbhi.blackbox.ui.utils.Network.isNetworkAvailable(this)) {
            return true
        }
        return false
    }

    override fun isShowLoading(): Boolean {
        return true
    }

    override fun isHideLoading(): Boolean {
        return true
    }

    override fun showErrorMessage(string: String) {

    }
}

class MyItem(
    lat: Double,
    lng: Double,
    title: String,
    snippet: String
) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }

    init {
        position = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
    }
}