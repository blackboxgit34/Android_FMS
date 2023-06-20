package com.humbhi.blackbox.ui.ui.dashboard

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FragmentDashboardBinding
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.*
import com.humbhi.blackbox.ui.data.models.CheckDriverBehaviour.CheckDashPage.CheckDashPageModel
import com.humbhi.blackbox.ui.data.models.CheckInstalledDevices.CheckInstalledDevicesModel
import com.humbhi.blackbox.ui.data.network.AsyncApicall
import com.humbhi.blackbox.ui.data.network.CommonResponse
import com.humbhi.blackbox.ui.data.network.MqttApiClient
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.banner.BillBanner
import com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingBehaviourActivity
import com.humbhi.blackbox.ui.ui.vehicleStatus.VehicleStatusActivity
import com.humbhi.blackbox.ui.utils.ApiCallsHelper.ApiClient
import com.humbhi.blackbox.ui.utils.Constants
import gen._base._base_java__rjava_resources.srcjar.R.id.async
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.eclipse.paho.client.mqttv3.MqttClient
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import kotlin.properties.Delegates

open class DashboardFragment : Fragment() , RetrofitResponse{
    private lateinit var binding: FragmentDashboardBinding
    lateinit var VehicleCountData:VehicleCountData
    lateinit var FleetData : FleetUtilizationResponse
    lateinit var FuelData : VehicleMielageResponse
    lateinit var DrivingData : DriverBehaviourDataModel
    lateinit var SpeedData : SpeedAnalysisResponse
    var DistanceChart: BarChart? = null
    private var riskyDrivers:Int = 0
    private var excellentDrivers:Int = 0
    private var moderateDrivers: Int = 0
    lateinit var checkDashPageResponse : CheckDashPageModel
    lateinit var checkInstalledDevicesResponse : CheckInstalledDevicesModel
    var DriverBehaviourAvailability = ""
    var DriverBehaviourAvailabilityClick = ""
    val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        getUserData()
        getAllResult()
        binding.swipeView.setOnRefreshListener {
            binding.swipeView.isRefreshing = true
            getAllResult()
        }
        binding.llDrivingBehaviour.setOnClickListener {
            if (DriverBehaviourAvailability != "") {
                if (DriverBehaviourAvailability == "true") {
                    if (DriverBehaviourAvailabilityClick == "true") {
                        val intent = Intent(activity, DrivingBehaviourActivity::class.java)
                        intent.putExtra("excellent", excellentDrivers)
                        intent.putExtra("risky", riskyDrivers)
                        intent.putExtra("moderate", moderateDrivers)
                        intent.putExtra("totalVehicle", binding.tvTotalVehicle.text.toString())
                        startActivity(intent)
                    }
                } else {
                    binding.progress.progressLayout.visibility = View.VISIBLE
                    Api.CheckInstalledDevices(CommonData.getCustIdFromDB()).enqueue(object: Callback<CheckInstalledDevicesModel>{
                        override fun onResponse(
                            call: Call<CheckInstalledDevicesModel>,
                            response: Response<CheckInstalledDevicesModel>
                        ) {
                            binding.progress.progressLayout.visibility = View.GONE
                            checkInstalledDevicesResponse = response.body()!!
                            if(checkInstalledDevicesResponse.data[0].Cnt==0){
                                activity?.let { it1 ->
                                    showConfirmationDialog(
                                        it1,
                                        "To enable Driving Behavior Monitoring feature, you need TM33+ or TM88 or AIS140 devices. Please contact your sales executive.",
                                        "Are you Intrested ?"
                                    ){ saveInterest() }
                                }
                            }
                            else{
                                Api.checkIfFreeTrail(CommonData.getCustIdFromDB()).enqueue(object: Callback<CheckDashPageModel>{
                                    override fun onResponse(
                                        call: Call<CheckDashPageModel>,
                                        response: Response<CheckDashPageModel>
                                    ) {
                                        binding.progress.progressLayout.visibility = View.GONE
                                        checkDashPageResponse = response.body()!!
                                        if(checkDashPageResponse.data==1){
                                            Constants.alertDialog(activity,"Thank you. Your Free Trial will be activated in 24 hrs.")
                                        }
                                        else{
                                            Api.checkFreeTrail(CommonData.getCustIdFromDB()).enqueue(object : Callback<CheckDashPageModel>{
                                                override fun onResponse(
                                                    call: Call<CheckDashPageModel>,
                                                    response: Response<CheckDashPageModel>
                                                ) {
                                                    binding.progress.progressLayout.visibility = View.GONE
                                                    checkDashPageResponse = response.body()!!
                                                    if(checkDashPageResponse.data==1){
                                                        activity?.let { it1 ->
                                                            showConfirmationDialog(
                                                                it1,
                                                                "Your free trial is over. To enable this feature click enable, 25 will be added to your existing plan monthly per vehicle.",
                                                                "Do you want to enable this feature?"
                                                            ) { enableFeature() }
                                                        }
                                                    }
                                                    else{
                                                        activity?.let { it1 ->
                                                            showConfirmationDialog(
                                                                it1,
                                                                "Driving Behavior Monitoring feature, for just Rs 25, Start your 1 month free Trial today.",
                                                                "Do you want to start your free trail?"
                                                            ) { saveTrail() }
                                                        }
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<CheckDashPageModel>,
                                                    t: Throwable
                                                ) {
                                                    binding.progress.progressLayout.visibility = View.GONE
                                                    if(t is SocketTimeoutException){
                                                        Constants.alertDialog(activity,"Connection time out.")
                                                    }
                                                    else{
                                                        Constants.alertDialog(activity,"Something went wrong.")
                                                    }
                                                }
                                            })
                                        }
                                    }
                                    override fun onFailure(
                                        call: Call<CheckDashPageModel>,
                                        t: Throwable
                                    ) {
                                        binding.progress.progressLayout.visibility = View.GONE
                                        if(t is SocketTimeoutException){
                                            Constants.alertDialog(activity,"Connection time out.")
                                        }
                                        else{
                                            Constants.alertDialog(activity,"Something went wrong.")
                                        }
                                    }

                                })
                            }
                        }

                        override fun onFailure(call: Call<CheckInstalledDevicesModel>, t: Throwable) {
                            binding.progress.progressLayout.visibility = View.GONE
                            if(t is SocketTimeoutException){
                                Constants.alertDialog(activity,"Connection time out.")
                            }
                            else{
                                Constants.alertDialog(activity,"Something went wrong.")
                            }
                        }
                    })
                }
            }
        }

        binding.llMoving.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","M")
            startActivity(intent)
        }

        binding.llParked.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","P")
            startActivity(intent)
        }
        binding.llUnreachable.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","U")
            startActivity(intent)
        }

        binding.llIgnitionOn.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","I")
            startActivity(intent)
        }

        binding.llHighspeed.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","H")
            startActivity(intent)
        }

        binding.llBatteryDisconnected.setOnClickListener {
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","BD")
            startActivity(intent)
        }

        binding.cvReminders.setOnClickListener {
            Toast.makeText(activity,"Coming soon.",Toast.LENGTH_LONG).show()
        }

        binding.llAllVehicle.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","")
            startActivity(intent)
        }
        return binding.root
    }

    private fun saveInterest() {
        binding.progress.progressLayout.visibility = View.VISIBLE
        Api.saveInterest(CommonData.getCustIdFromDB()).enqueue(object: Callback<CheckDashPageModel>{
            override fun onResponse(
                call: Call<CheckDashPageModel>,
                response: Response<CheckDashPageModel>
            ) {
                binding.progress.progressLayout.visibility = View.GONE
                checkDashPageResponse = response.body()!!
                if(checkDashPageResponse.data==1){
                    Constants.alertDialog(activity,"Thank you for showing interest in Driving behavior monitoring. Sales executive will call you soon.")
                }
                else{
                    Constants.alertDialog(activity,"Something went wrong.")
                }
            }

            override fun onFailure(call: Call<CheckDashPageModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                if(t is SocketTimeoutException){
                    Constants.alertDialog(activity,"Connection time out.")
                }
                else{
                    Constants.alertDialog(activity,"Something went wrong.")
                }
            }
        })
    }

    private fun enableFeature(){
        binding.progress.progressLayout.visibility = View.VISIBLE
        Api.enableFeature(CommonData.getCustIdFromDB()).enqueue(object:Callback<CheckDashPageModel>{
            override fun onResponse(
                call: Call<CheckDashPageModel>,
                response: Response<CheckDashPageModel>
            ) {
                binding.progress.progressLayout.visibility = View.GONE
                checkDashPageResponse = response.body()!!
                if(checkDashPageResponse.data==1){
                    Constants.alertDialog(activity,"Driving Behavior Monitoring enabled.")
                }
                else{
                    Constants.alertDialog(activity,"Something went wrong.")
                }
            }

            override fun onFailure(call: Call<CheckDashPageModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                if(t is SocketTimeoutException){
                    Constants.alertDialog(activity,"Connection time out.")
                }
                else{
                    Constants.alertDialog(activity,"Something went wrong.")
                }
            }
        })
    }

    private  fun saveTrail() {
        binding.progress.progressLayout.visibility = View.VISIBLE
        Api.saveFreeTrail(CommonData.getCustIdFromDB()).enqueue(object:Callback<CheckDashPageModel>{
            override fun onResponse(
                call: Call<CheckDashPageModel>,
                response: Response<CheckDashPageModel>
            ) {
                binding.progress.progressLayout.visibility = View.GONE
                checkDashPageResponse = response.body()!!
                if(checkDashPageResponse.data==1){
                    Constants.alertDialog(activity,"Thank you. Your Free Trial will be activated in 24 hrs.")
                }
                else{
                    Constants.alertDialog(activity,"Something went wrong.")
                }
            }

            override fun onFailure(call: Call<CheckDashPageModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                if(t is SocketTimeoutException){
                    Constants.alertDialog(activity,"Connection time out.")
                }
                else{
                    Constants.alertDialog(activity,"Something went wrong.")
                }
            }
        })

    }

    private fun getUserData(){
        Retrofit2(activity,this,
            Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
            Constants.EXPIRE_ACCOUNT_DETAILS+"custid="+ CommonData.getCustIdFromDB()).callService(true)
    }

    private fun DisplayVehicleData(VehicleData:VehicleCountData){
        binding.tvTotalVehicle.text = VehicleData.TotalVehicles.toString()
        binding.tvMovingVehicle.text = VehicleData.Moving.toString()
        binding.tvParkedVehicle.text = VehicleData.Parked.toString()
        binding.tvUnreachVehicle.text = VehicleData.Unreach.toString()
        binding.tvBatDisconnectionVehicle.text = VehicleData.Ignitionon.toString()
        binding.tvHighspeed.text = VehicleData.Hispeed.toString()
        binding.tvTotal.text = VehicleData.TotalVehicles.toString()
        binding.tvBatteryDisconneted.text = VehicleData.batteryvoltage.toString()
        binding.llTotalVehicleMain.visibility = View.VISIBLE
        binding.llTotalVhclProgress.visibility = View.GONE
    }

    private fun DisplayFleetData(fleetUtilizationResponse: FleetUtilizationResponse){
        val totalNumbers = fleetUtilizationResponse.Moving + fleetUtilizationResponse.Parked + fleetUtilizationResponse.IgnitionON
        val movingPrecentage = (fleetUtilizationResponse.Moving.toFloat() / totalNumbers.toFloat()) * 100
        val parkedPrecentage = (fleetUtilizationResponse.Parked.toFloat() / totalNumbers.toFloat()) * 100
        val idealPrecentage = (fleetUtilizationResponse.IgnitionON.toFloat() / totalNumbers.toFloat()) * 100
        binding.tvMovingPercentage.text = String.format(Locale.ENGLISH,"%.2f", movingPrecentage).toDouble().toString()
        binding.tvParkedPercentage.text = String.format(Locale.ENGLISH,"%.2f", parkedPrecentage).toDouble().toString()
        binding.tvIdealPercentage.text = String.format(Locale.ENGLISH,"%.2f", idealPrecentage).toDouble().toString()
        binding.llFleetProgress.visibility = View.GONE
        binding.clFleet.visibility = View.VISIBLE
        setFleetChart(
            fleetUtilizationResponse.Moving,
            fleetUtilizationResponse.Parked,
            fleetUtilizationResponse.IgnitionON
        )
    }

    private fun DisplayFuelData(vehicleMielageResponse: VehicleMielageResponse){
        if (vehicleMielageResponse.BestMileage==0 && vehicleMielageResponse.WorstMileage==0){
            binding.rlFuelNotAvailable.visibility = View.VISIBLE
            binding.clFuel.visibility = View.GONE
            binding.llFuelProgress.visibility = View.GONE
            if(vehicleMielageResponse.notworking>0){
                binding.noFuelAvailable.text = "No consumption today"
            }
        }
        else {
            binding.rlFuelNotAvailable.visibility = View.GONE
            binding.tvHighestAvg.text = vehicleMielageResponse.BestMileage.toString()
            binding.tvAboveAvg.text = vehicleMielageResponse.WorstMileage.toString()
            binding.clFuel.visibility = View.VISIBLE
            binding.llFuelProgress.visibility = View.GONE
            setFuelChart(vehicleMielageResponse.BestMileage, vehicleMielageResponse.WorstMileage)
        }
    }

    private fun DisplaySpeedData(speedAnalysisResponse: SpeedAnalysisResponse){
        binding.tvOverspeed.text = speedAnalysisResponse.OverspeedVehicles.toString()
        binding.tvAvgSpeed.text = speedAnalysisResponse.NonOverspeedVehicles.toString()
        binding.llOverspeed.visibility = View.VISIBLE
        binding.llAvgSpeed.visibility = View.VISIBLE
        binding.llOverspeedProgress.visibility = View.GONE
        binding.llAvgSpeedProgress.visibility = View.GONE
    }

    private fun DisplayDriverData(driverBehaviourDashModel: DriverBehaviourDataModel){
        if (driverBehaviourDashModel.data.isNotEmpty()){
            binding.llDrivingData.visibility = View.VISIBLE
            binding.rlDrivingNotAvailable.visibility = View.GONE
            binding.tvHarshAccCount.text = driverBehaviourDashModel.data[0].HAPer.toString()
            binding.tvHarshBreakCount.text = driverBehaviourDashModel.data[0].HBPer.toString()
            binding.tvHarshOverspeedCount.text = driverBehaviourDashModel.data[0].OSPer.toString()
            binding.tvRashTurnCount.text = driverBehaviourDashModel.data[0].RTPer.toString()
            binding.clDriver.visibility = View.VISIBLE
            binding.llDrivingBehavProgress.visibility = View.GONE
            riskyDrivers = driverBehaviourDashModel.data[0].RDPer.toInt()
            excellentDrivers = driverBehaviourDashModel.data[0].ExDPer.toInt()
            moderateDrivers = driverBehaviourDashModel.data[0].ModPer.toInt()
            DriverBehaviourAvailabilityClick = "true"
            setDistancGraphData(driverBehaviourDashModel.data[0].HAPer,driverBehaviourDashModel.data[0].HBPer,driverBehaviourDashModel.data[0].OSPer,driverBehaviourDashModel.data[0].RTPer)
        }
        else{
            DriverBehaviourAvailabilityClick = "false"
            binding.llDrivingData.visibility = View.GONE
            binding.rlDrivingNotAvailable.visibility = View.VISIBLE
            binding.clDriver.visibility = View.VISIBLE
            binding.llDrivingBehavProgress.visibility = View.GONE
        }
    }

    private fun setFleetChart(moving: Int, parked: Int, ideal: Int) {
        val pieEntries = ArrayList<PieEntry>()
        val label = "type"
        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["1"] = moving
        typeAmountMap["2"] = parked
        typeAmountMap["3"] = ideal

        //initializing colors for the entries
        val colors2 = ArrayList<Int>()
        colors2.add(Color.parseColor("#A2B06D"))
        colors2.add(Color.parseColor("#FFD200"))
        colors2.add(Color.parseColor("#FF4646"))

        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }
        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors2
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false)

        binding.fleetChart.data = pieData
        binding.fleetChart.setDrawEntryLabels(false)
        binding.fleetChart.isRotationEnabled = true
        binding.fleetChart.isDrawHoleEnabled = true
        binding.fleetChart.description = null
        binding.fleetChart.setDrawEntryLabels(false)
        binding.fleetChart.legend.isEnabled = false
        binding.fleetChart.animateY(3000)
        binding.fleetChart.invalidate()
        binding.fleetChart.holeRadius = 80f
        binding.fleetChart.setHoleColor(ContextCompat.getColor(requireActivity(), R.color.primary_little_fade))
    }

    // Driving behaviour chart data
    private fun setDistancGraphData(HA:Double,HB:Double,OS:Double,RT:Double) {
        DistanceChart = binding.DistanceChart
        val desc: Description
        val L: Legend
        L = DistanceChart!!.legend
        desc = DistanceChart!!.description
        desc.text = "" // this is the weirdest way to clear something!!
        L.isEnabled = false
        val leftAxis = DistanceChart!!.axisLeft
        val rightAxis = DistanceChart!!.axisRight
        val xAxis = DistanceChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        leftAxis.textSize = 10f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
        val data = BarData(setData(HA,HB,OS,RT))
        data.barWidth = 0.4f // set custom bar width
        DistanceChart!!.data = data
        DistanceChart!!.setFitBars(true) // make the x-axis fit exactly all bars
        DistanceChart!!.invalidate() // refresh
        DistanceChart!!.setScaleEnabled(false)
        DistanceChart!!.isDoubleTapToZoomEnabled = false
        //DistanceChart.setBackgroundResource(R.drawable.round_bars);
        DistanceChart!!.setBackgroundColor(Color.BLACK)
        DistanceChart!!.animateXY(2000, 2000)
        DistanceChart!!.setDrawBorders(false)
        DistanceChart!!.description = desc
        data.setValueTextColor(Color.rgb(255, 255, 255))
        DistanceChart!!.setDrawValueAboveBar(true)
    }

    private fun setData(HA:Double,HB:Double,OS:Double,RT:Double): BarDataSet? {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, HA.toFloat()))
        entries.add(BarEntry(1f, HB.toFloat()))
        entries.add(BarEntry(2f, OS.toFloat()))
        entries.add(BarEntry(3f, RT.toFloat()))

        val set = BarDataSet(entries, "")
        set.setColors(
            ContextCompat.getColor(DistanceChart!!.context, R.color.blue),
            ContextCompat.getColor(DistanceChart!!.context, R.color.secondary_yellow),
            ContextCompat.getColor(DistanceChart!!.context, R.color.red),
            ContextCompat.getColor(DistanceChart!!.context, R.color.voilet_red),
        )
        set.valueTextColor = Color.rgb(255, 255, 255)
        return set
    }

    // fuel chart data
    private fun setFuelChart(bestMileage: Int, worstMileage: Int) {
        val pieEntries = ArrayList<PieEntry>()
        val label = "type"
        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["1"] = bestMileage
        typeAmountMap["2"] = worstMileage

        //initializing colors for the entries
        val colors2 = ArrayList<Int>()
        colors2.add(Color.parseColor("#FF4646"))
        colors2.add(Color.parseColor("#FFD200"))

        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }
        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors2
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false)

        binding.fuelChart.data = pieData

        binding.fuelChart.setDrawEntryLabels(false)
        binding.fuelChart.isRotationEnabled = true
        binding.fuelChart.isDrawHoleEnabled = false
        binding.fuelChart.description = null
        binding.fuelChart.setDrawEntryLabels(false)
        binding.fuelChart.legend.isEnabled = false
        binding.fuelChart.animateY(4000)
        binding.fuelChart.invalidate()
        binding.fuelChart.setHoleColor(this.resources.getColor(R.color.primary_little_fade))
    }

     @OptIn(DelicateCoroutinesApi::class)
     private fun getAllResult() {
         val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
         GlobalScope.launch(Dispatchers.IO) {
             try {
                 val vehicelData =
                     AsyncApicall.callApi { Api.getVehicleCount(CommonData.getCustIdFromDB()) }
                 withContext(Dispatchers.Main) {
                     if (isAdded) {
                         VehicleCountData = vehicelData.body()!!
                         DisplayVehicleData(VehicleCountData)
                     }
                 }

                 val fleetData =
                     AsyncApicall.callApi { Api.getFleetUtilization(CommonData.getCustIdFromDB()) }
                 withContext(Dispatchers.Main) {
                     if (isAdded) {
                         FleetData = fleetData.body()!!
                         DisplayFleetData(FleetData)
                     }
                 }

                 val SppedData =
                     AsyncApicall.callApi { Api.getSpeedAnalysis(CommonData.getCustIdFromDB()) }
                 withContext(Dispatchers.Main) {
                     if (isAdded) {
                         if(binding.swipeView.isRefreshing == true){
                             binding.swipeView.isRefreshing = false
                         }
                         SpeedData = SppedData.body()!!
                         DisplaySpeedData(SpeedData)
                     }
                 }

                 val checkDashPageApi =
                     AsyncApicall.callApi { Api.checkDashPage(CommonData.getCustIdFromDB()) }
                 withContext(Dispatchers.Main) {
                     if (isAdded) {
                         checkDashPageResponse = checkDashPageApi.body()!!
                         if (checkDashPageResponse.data == 1) {
                             withContext(Dispatchers.IO) {
                                 val DriverBehaviour = AsyncApicall.callApi { Api.getDriverBehaviour(CommonData.getCustIdFromDB()) }
                                 withContext(Dispatchers.Main) {
                                     if (isAdded) {
                                         DrivingData = DriverBehaviour.body()!!
                                         DriverBehaviourAvailability = "true"
                                         DisplayDriverData(DrivingData)
                                     }
                                 }
                             }
                         } else {
                             DriverBehaviourAvailability = "false"
                             binding.llDrivingData.visibility = View.VISIBLE
                             binding.rlDrivingNotAvailable.visibility = View.GONE
                             binding.tvHarshAccCount.text = "12"
                             binding.tvHarshBreakCount.text = "19"
                             binding.tvHarshOverspeedCount.text = "14"
                             binding.tvRashTurnCount.text = "28"
                             binding.clDriver.visibility = View.VISIBLE
                             binding.llDrivingBehavProgress.visibility = View.GONE
                             riskyDrivers = VehicleCountData.TotalVehicles / 2
                             excellentDrivers = VehicleCountData.TotalVehicles / 10
                             moderateDrivers = (VehicleCountData.TotalVehicles / 2.5).toInt()
                             setDistancGraphData(16.44, 26.03, 19.18, 38.36)
                         }
                     }
                 }

                 val FuelMilageData = AsyncApicall.callApi { Api.getMileageAnalysis(CommonData.getCustIdFromDB()) }
                 withContext(Dispatchers.Main) {
                     if (isAdded) {
                         FuelData = FuelMilageData.body()!!
                         DisplayFuelData(FuelData)
                     }
                 }
             }
             catch (e:Exception){
                 if(e is SocketTimeoutException) {
                     withContext(Dispatchers.Main) {
                         Toast.makeText(activity, "Connection time out.", Toast.LENGTH_SHORT).show()
                     }
                 }
             }
         }
     }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode) {
            Constants.REQ_EXPIRE_ACCOUNT_DETAILS -> {
                try {
                    var fuelRod = false
                    var tempRod = false
                    var msg = ""
                    val result = JSONObject(response!!.body()!!.string())
                    val table = result.getJSONArray("Table")
                    for (i in 0 until table.length()) {
                        val jsonObject = table.getJSONObject(0)
                        fuelRod = jsonObject.getBoolean("FuelRodActive")
                        tempRod = jsonObject.getBoolean("tepsensor")
                        msg = jsonObject.getString("msg")
                        if (msg != "null") {
                            binding.marqueeText.text = msg
                        }
                        else{
                            binding.marqueeText.text = "Welcome to Blackbox GPS application, you can track your vehicles from this app."
                        }
                        binding.marqueeText.isSelected = true
                    }
                    CommonData.setFuelRodStatus(fuelRod)
                    CommonData.setTempRodStatus(tempRod)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        onYesClicked: () -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_confirm, null)
        builder.setView(view)
        val yesButton: Button = view.findViewById(R.id.dialog_button_yes)
        val noButton: Button = view.findViewById(R.id.dialog_button_no)
        val Title: TextView = view.findViewById(R.id.dialog_title)
        val Msg: TextView = view.findViewById(R.id.dialog_message)
        Title.text = title
        Msg.text = message
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        yesButton.setOnClickListener {
            onYesClicked.invoke()
            dialog.dismiss()
        }
        noButton.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

}