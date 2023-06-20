package com.humbhi.blackbox.ui.ui.vehicleStatus

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityVehicleStatusBinding
import com.humbhi.blackbox.ui.adapters.VehicleStatusAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusDataItem
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity
import com.humbhi.blackbox.ui.ui.livetracking.LiveCarActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.IntentConstant

class VehicleStatusActivity : AppCompatActivity(), View.OnClickListener,VehicleStatusView {
    private lateinit var binding: ActivityVehicleStatusBinding
    private lateinit var vehicleStatusPresenter: VehicleStatusPresenter
    private lateinit var adapter: VehicleStatusAdapter
    private lateinit var progress: ProgressDialog
    var startlimit = 0
    var limit = 20
    var list : ArrayList<VehicleLiveStatusDataItem> = ArrayList()
    var search = ""
    var totalRecords = 0
    var statusFilter:String = ""
    private var handler: Handler? = null
//    var loadMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progress = ProgressDialog(this)
        vehicleStatusPresenter = VehcileStatusPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMasterSecure())
        )
        binding.tvFilter.setOnClickListener(this)
        setToolbarOptions()
        handler = Handler(Looper.myLooper()!!)
        binding.etSearchBar.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                search = v.text.toString().toUpperCase()
                Api()
                return@OnEditorActionListener true
            }
            false
        })

        if(intent.hasExtra("filterValue")) {
            when (intent.getStringExtra("filterValue")) {
                "M" -> {
                    statusFilter = "M"
                }
                "P" -> {
                    statusFilter = "P"
                }
                "U" -> {
                    statusFilter = "U"
                }
                "I" -> {
                    statusFilter = "I"
                }
                "H" -> {
                    statusFilter = "H"
                }
                else -> {
                    statusFilter = ""
                }
            }
        }
        startGettingOnlineDataFromApi()
        binding.swipeRefreshLayout.setOnRefreshListener {
            performRefresh()
        }
    }

    fun stopRepeatingTask() {
        handler!!.removeCallbacksAndMessages(null)
    }

    private fun performRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        list.clear()
        Api()
    }

    var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                list.clear()
                Api()
            } catch (e: Exception) {

            }
            handler!!.postDelayed(this,60*1000)
        }
    }

    private fun startGettingOnlineDataFromApi() {
        try {
            handler!!.post(mStatusChecker)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun setToolbarOptions() {
        binding.toolBar.ivMenu.visibility = View.GONE
        binding.toolBar.ivBack.visibility = View.VISIBLE
        binding.toolBar.ivBell.visibility = View.GONE
        binding.toolBar.tvTitle.text = "Live Tracking"
        binding.toolBar.ivBack.setOnClickListener {
            stopRepeatingTask()
            finish()
        }
    }

    private fun Api(){
        binding.llProgressLayout.progressLayout.visibility = View.VISIBLE
        vehicleStatusPresenter.hitLiveStatusApi(CommonData.getCustIdFromDB(),statusFilter,"0",startlimit,limit,search,"0","")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvFilter -> {
                val dialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.vehicle_filterbottomsheet, null)
                val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
                val rbMoving = view.findViewById<Button>(R.id.rbMoving)
                val rbParked = view.findViewById<Button>(R.id.rbParked)
                val rbUnreach = view.findViewById<Button>(R.id.rbUnreach)
                val rbHiSpeed = view.findViewById<Button>(R.id.rbHiSpeed)
                val rbIgnitionOn = view.findViewById<Button>(R.id.rbIgnitionOn)
                val rbAll = view.findViewById<Button>(R.id.rbAll)
                rbMoving.setOnClickListener {
                    statusFilter = "M"
                }
                rbParked.setOnClickListener {
                    statusFilter = "P"
                }
                rbUnreach.setOnClickListener {
                    statusFilter = "U"
                }
                rbHiSpeed.setOnClickListener {
                    statusFilter = "H"
                }
                rbIgnitionOn.setOnClickListener {
                    statusFilter = "I"
                }
                rbAll.setOnClickListener {
                    statusFilter = ""
                }
                btnClose.setOnClickListener {
                    binding.loadMore.visibility = View.GONE
                    startlimit = 0
                    list.clear()
                    vehicleStatusPresenter.hitLiveStatusApi(CommonData.getCustIdFromDB(),statusFilter,"0",startlimit,limit,binding.etSearchBar.text.toString().trim(),"0","")
                    dialog.dismiss()
                }
                dialog.setCancelable(false)
                dialog.setContentView(view)
                dialog.show()
            }
        }
    }

    override fun getVehicleStatus(listData: VehicleLiveStatusModel) {
        if(binding.swipeRefreshLayout.isRefreshing == true){
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.tvTotalCount.text = listData.iTotalRecords.toString()+" Vehicle"
        val layoutManager = LinearLayoutManager(this)
        totalRecords = listData.iTotalRecords
        for(i in 0 until  listData.aaData.size) {
            list.add(listData.aaData[i])
        }
        binding.rvVehicle.layoutManager = layoutManager
        adapter = VehicleStatusAdapter(
            this,
            object : VehicleStatusAdapter.VehicleDetails {
                override fun onVehicleSelection(position: Int) {
                    stopRepeatingTask()
                    val bundle = Bundle()
                    bundle.putString(IntentConstant.VEHICLE_NAME, list[position].VehicleName)
                    // ExplicitIntentUtil.startActivity(this@VehicleStatusActivity,VehicleDetailActivity::class.java,bundle)
                    val intent = Intent(this@VehicleStatusActivity, LiveCarActivity::class.java)
                    intent.putExtra("vehicleName", list[position].VehicleName)
                    intent.putExtra("vehicleNameIcon", list[position].VIconName)
                    startActivity(intent)
                }
            },
            list)
        binding.rvVehicle.adapter = adapter
       // loadMore = false
        binding.rvVehicle.scrollToPosition(startlimit)
        binding.rvVehicle.visibility = View.VISIBLE
        if(totalRecords>20){
            binding.loadMore.visibility = View.VISIBLE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                startlimit += 20
                Api()
            }
        }
//        binding.rvVehicle.addOnScrollListener( object : EndlessRecyclerOnScrollListener(){
//            override fun onLoadMore() {
//                if(loadMore==false){
//                    if(list.size<totalRecords){
//                        loadMore = true
//                        startlimit += 20
//                        Api()
//                    }
//                }
//            }
//        })
    }

    override fun isNetworkConnected(): Boolean {
        CommonUtil.isNetworkAvailable(this)
        return true
    }

    override fun isShowLoading(): Boolean {
        binding.llMainLayout.visibility = View.VISIBLE
        binding.llProgressLayout.progressLayout.visibility = View.VISIBLE
        return true
    }

    override fun isHideLoading(): Boolean {
        binding.llProgressLayout.progressLayout.visibility = View.GONE
        binding.llMainLayout.visibility = View.VISIBLE
        return true
    }

    override fun showErrorMessage(string: String) {
      //  CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }

}