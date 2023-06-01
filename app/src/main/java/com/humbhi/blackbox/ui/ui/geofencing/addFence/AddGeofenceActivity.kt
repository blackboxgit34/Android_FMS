package com.humbhi.blackbox.ui.ui.geofencing.addFence

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.humbhi.blackbox.databinding.ActivityAddGeofenceBinding

class AddGeofenceActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddGeofenceBinding
    private lateinit var mMap: GoogleMap

    var context: Context? = null
    var arraylistoflatlng: List<LatLng>? = null
    var polylineList: List<Polyline>? = null

    var projection: Projection? = null
    var latitude = 0.0
    var longitude = 0.0

    var Is_MAP_Moveable = false
    private var line: Polyline? = null
    private var polygon: Polygon? = null

    var available = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGeofenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        BottomSheetBehavior.from(binding.fBottomSheet).apply {
            peekHeight = 350
            this.state = BottomSheetBehavior.STATE_EXPANDED

        }
        binding.toolbar.tvTitle.text = "Add Geofence"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivAdd.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btDraw.setOnClickListener {
            Is_MAP_Moveable = !Is_MAP_Moveable
        }

        arraylistoflatlng = ArrayList()
        polylineList = ArrayList()
        binding.rlFrame!!.setOnTouchListener { v, event ->
            val x = event.x
            val y = event.y
            val x_co = Math.round(x)
            val y_co = Math.round(y)
            projection = mMap.projection
            val x_y_points = Point(x_co, y_co)
            val latLng = mMap.projection.fromScreenLocation(x_y_points)
            latitude = latLng.latitude
            longitude = latLng.longitude
            val eventaction = event.action
            if (available) {
                //clear the previous polygon first. Write code here
                available = false
            }
            when (eventaction) {
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_MOVE -> {
                    // finger moves on the screen
                    (arraylistoflatlng as ArrayList<LatLng>).add(LatLng(latitude, longitude))
                    val rectOptions = PolylineOptions()
                        .addAll(arraylistoflatlng as ArrayList<LatLng>)
                        .geodesic(true)
                        .color(Color.BLACK)
                    line = mMap.addPolyline(rectOptions)
                    (polylineList as ArrayList<Polyline>).add(line!!)
                }
                MotionEvent.ACTION_UP -> {
                    // finger leaves the screen
                    polygon = mMap.addPolygon(
                        PolygonOptions()
                            .addAll(arraylistoflatlng as ArrayList<LatLng>)
                            .fillColor(Color.argb(100, 20, 137, 238))
                    )
                    available = true
                }
            }
            Is_MAP_Moveable
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}