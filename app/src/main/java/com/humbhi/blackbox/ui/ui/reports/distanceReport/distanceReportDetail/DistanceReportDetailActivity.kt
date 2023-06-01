package com.humbhi.blackbox.ui.ui.reports.distanceReport.distanceReportDetail

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Html
import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDistanceReportDetailBinding
import com.humbhi.blackbox.ui.data.models.ObjTravelReport
import com.humbhi.blackbox.ui.utils.polylineAnimater.MapAnimator

class DistanceReportDetailActivity : AppCompatActivity(),OnMapReadyCallback, OnMapLoadedCallback
,OnInfoWindowClickListener, ConnectionCallbacks
, OnConnectionFailedListener {
    private lateinit var binding:ActivityDistanceReportDetailBinding
    private lateinit var dataList:ArrayList<ObjTravelReport>
    private lateinit var vehicleName:String
    private lateinit var mMap: GoogleMap
    private lateinit var marker:Marker
    var Angle_Bring = 0f
    val points: ArrayList<LatLng> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanceReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getIntentData()
        setToolbar()
    }

    private fun getIntentData(){
        val bundle = intent.extras
        dataList = bundle?.getParcelableArrayList<ObjTravelReport>("DistanceDetail")!!
        vehicleName = bundle.getString("VehicleName").toString()
        Log.e("CHECK_DETAILS",dataList.size.toString())
        for (i in dataList.indices){
            points.add(LatLng(dataList[i].getStartLat().toDouble(),dataList[i].getStartLongi().toDouble()))
        }

    }

    private fun setToolbar(){
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.tvTitle.text = vehicleName
        binding.toolbar.ivBack.setOnClickListener { finish() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (mMap!=null){
            // Start location market
            marker = mMap.addMarker(
                MarkerOptions().position(LatLng(
                    dataList[0].getStartLongi().toDouble(),
                    dataList[0].getStartLongi().toDouble()
                ))
                    .title("Start Location :" + Html.fromHtml(Html.fromHtml(dataList.get(0).StartLocation).toString()))
                    .snippet(dataList.get(0).StartDateTime)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top))
            )!!

            // set End Marker
            marker = mMap.addMarker(
                MarkerOptions().position(LatLng(
                    points.get(points.size-1).latitude,
                    points.get(points.size-1).longitude
                ))
                    .title("End Location :" + Html.fromHtml(Html.fromHtml(dataList.get(0).StartLocation).toString()))
                    .snippet(dataList.get(points.size-1).StartDateTime)

                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top))
            )!!

            var polyOptions = PolylineOptions()
            for (locationPoints in dataList.indices){
                polyOptions = PolylineOptions()
                    .add(LatLng(dataList[locationPoints].getStartLat().toDouble(),dataList[locationPoints].getStartLongi().toDouble()))
                    .width(10f)
                    .color(Color.BLACK)

                Log.e("LocationCoordinates",dataList[locationPoints].getStartLat())
            }


            mMap.addPolyline(polyOptions)

        }
    }

    override fun onMapLoaded() {

    }

    override fun onInfoWindowClick(marker: Marker) {

    }

    override fun onConnected(bundle: Bundle?) {

    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}