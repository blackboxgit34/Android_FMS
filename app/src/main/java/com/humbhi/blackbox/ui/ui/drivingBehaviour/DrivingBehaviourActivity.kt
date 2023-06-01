package com.humbhi.blackbox.ui.ui.drivingBehaviour

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDrivingBehaviourBinding
import com.humbhi.blackbox.ui.adapters.ExcellentDriverAdapter
import com.humbhi.blackbox.ui.adapters.RiskyDriverAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.ExcellentDriverDataModel
import com.humbhi.blackbox.ui.data.models.RiskyDriverModel
import com.humbhi.blackbox.ui.data.models.TotalVoilationDataModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverMonthlyComparison.DriverMonthlyComparison
import com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverToDriverComparison.DriverToDriverComparisonActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingLimit.DrivingLimitActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.NoDrivingReport.NoDrivingReportActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.ScoreCard.ScoreCardActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.consolidateReport.DrBehavConsolidateReportActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.driverClassificationReports.SafeDriversActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.driverVoilations.TotalVoilationPresenter
import com.humbhi.blackbox.ui.ui.drivingBehaviour.driverVoilations.TotalVoilationPresenterImpl
import com.humbhi.blackbox.ui.ui.drivingBehaviour.driverVoilations.TotalVoilationView
import com.humbhi.blackbox.ui.ui.drivingBehaviour.harshAccelerationReport.HarshAccelerationActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.harshBreakingReport.HarshBreakingActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.rashTurn.RashTurnActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DrivingBehaviourActivity : AppCompatActivity(), TotalVoilationView {
    private lateinit var binding: ActivityDrivingBehaviourBinding
    private lateinit var mPresenter: TotalVoilationPresenter
    private lateinit var adapter: ExcellentDriverAdapter
    private lateinit var riskyAdapter: RiskyDriverAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrivingBehaviourBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        mPresenter = TotalVoilationPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        binding.cvHarshBreakReport.setOnClickListener {
            val intent = Intent(this, HarshBreakingActivity::class.java)
            startActivity(intent)
        }
        binding.llScore.setOnClickListener {
           val intent = Intent(this, ScoreCardActivity::class.java)
            startActivity(intent)
        }
        binding.llDrivingLimit.setOnClickListener {
           val intent = Intent(this, DrivingLimitActivity::class.java)
            startActivity(intent)
        }
        binding.llNoDrivingHours.setOnClickListener {
            val intent = Intent(this, NoDrivingReportActivity::class.java)
            startActivity(intent)
        }
        binding.cvHarshAccelerationReport.setOnClickListener {
            val intent = Intent(this, HarshAccelerationActivity::class.java)
            startActivity(intent)
        }

        binding.cvRashTurnReport.setOnClickListener {
            val intent = Intent(this, RashTurnActivity::class.java)
            startActivity(intent)
        }
        binding.cvConsolidateVoilationReport.setOnClickListener {
            val intent = Intent(this, DrBehavConsolidateReportActivity::class.java)
            startActivity(intent)
        }
        binding.llSafeDriverReports.setOnClickListener {
            val intent = Intent(this, SafeDriversActivity::class.java)
            intent.putExtra("title","Safe Drivers Report")
            intent.putExtra("rankType","Safe")
            startActivity(intent)
        }
        binding.llModerate.setOnClickListener {
            val intent = Intent(this, SafeDriversActivity::class.java)
            intent.putExtra("title","Moderate Drivers Report")
            intent.putExtra("rankType","Moderate")
            startActivity(intent)
        }
        binding.llRiskyDriverReport.setOnClickListener {
            val intent = Intent(this, SafeDriversActivity::class.java)
            intent.putExtra("title","Risky Drivers Report")
            intent.putExtra("rankType","Risky")
            startActivity(intent)
        }

        mPresenter.callDriverTotalVoilationBehaviourApi(CommonData.getCustIdFromDB())
        binding.cvRoute.setOnClickListener {
            val intent = Intent(this, DrivingRouteInitialActivity::class.java)
            startActivity(intent)
        }

        // Risky Drivers
        binding.cvRiskyDriver.setOnClickListener {
            // hide unhide excellent driver
            if (binding.rvRiskyDrivers.isShown) {
                binding.rvRiskyDrivers.visibility = View.GONE
                binding.vViewRisky.visibility = View.GONE
                binding.tvRiskyDetails.text = "See Details"
                binding.ivArrowRisky.setImageDrawable(getDrawable(R.drawable.ic_bx_chevron_right))
            } else {
                binding.rvRiskyDrivers.visibility = View.VISIBLE
                binding.vViewRisky.visibility = View.VISIBLE
                binding.tvRiskyDetails.text = "Hide Details"
                binding.ivArrowRisky.setImageDrawable(getDrawable(R.drawable.ic_chevron_down))
            }
            mPresenter.callRiskyDriverApi(CommonData.getCustIdFromDB(), "Risky")
        }

        binding.tvRiskyDetails.setOnClickListener {
            if (binding.rvRiskyDrivers.isShown) {
                binding.rvRiskyDrivers.visibility = View.GONE
                binding.vViewRisky.visibility = View.GONE
                binding.tvRiskyDetails.text = "See Details"
                binding.ivArrowRisky.setImageDrawable(getDrawable(R.drawable.ic_bx_chevron_right))
            } else {
                binding.rvRiskyDrivers.visibility = View.VISIBLE
                binding.vViewRisky.visibility = View.VISIBLE
                binding.tvRiskyDetails.text = "Hide Details"
                binding.ivArrowRisky.setImageDrawable(getDrawable(R.drawable.ic_chevron_down))
            }
            mPresenter.callRiskyDriverApi(CommonData.getCustIdFromDB(), "Risky")
        }


        // Excellent Drivers
        binding.cvExcellent.setOnClickListener {

            // hide unhide excellent driver
            if (binding.rvExcellentDrivers.isShown) {
                binding.rvExcellentDrivers.visibility = View.GONE
                binding.vViewExcellent.visibility = View.GONE
                binding.tvSeeExcellentDriverDetails.text = "See Details"
                binding.ivArrowExcellent.setImageDrawable(getDrawable(R.drawable.ic_bx_chevron_right))
            } else {
                binding.rvExcellentDrivers.visibility = View.VISIBLE
                binding.vViewExcellent.visibility = View.VISIBLE
                binding.tvSeeExcellentDriverDetails.text = "Hide Details"
                binding.ivArrowExcellent.setImageDrawable(getDrawable(R.drawable.ic_chevron_down))
            }
            mPresenter.callExcellentDriverApi(CommonData.getCustIdFromDB(), "Excellent")
        }

        binding.tvSeeExcellentDriverDetails.setOnClickListener {
            if (binding.rvExcellentDrivers.isShown) {
                binding.rvExcellentDrivers.visibility = View.GONE
                binding.vViewExcellent.visibility = View.GONE
                binding.tvSeeExcellentDriverDetails.text = "See Details"
                binding.ivArrowExcellent.setImageDrawable(getDrawable(R.drawable.ic_bx_chevron_right))
            } else {
                binding.rvExcellentDrivers.visibility = View.VISIBLE
                binding.vViewExcellent.visibility = View.VISIBLE
                binding.tvSeeExcellentDriverDetails.text = "Hide Details"
                binding.ivArrowExcellent.setImageDrawable(getDrawable(R.drawable.ic_chevron_down))
            }
            mPresenter.callExcellentDriverApi(CommonData.getCustIdFromDB(), "Excellent")
        }

        binding.drvrComparisonMonthly.setOnClickListener {
            val intent = Intent(this,DriverMonthlyComparison::class.java)
            startActivity(intent)
        }

        binding.drvrToDrvrComparison.setOnClickListener {
            val intent = Intent(this,DriverToDriverComparisonActivity::class.java)
            startActivity(intent)
        }

        // set driver classifications
        getIntentData()
    }

    private fun getIntentData(){
        val riskyDrivers:Int
        val excellentDrivers:Int
        val moderateDrivers: Int
        if (intent.hasExtra("excellent")){
            excellentDrivers = intent.getIntExtra("excellent",0)
            riskyDrivers = intent.getIntExtra("risky",0)
            moderateDrivers = intent.getIntExtra("moderate",0)
            setDriverClassificationChart(riskyDrivers,excellentDrivers,moderateDrivers)
            binding.tvExDriver.text = excellentDrivers.toString()
            binding.tvRskyDriver.text = riskyDrivers.toString()
            binding.tvModerateDriver.text = moderateDrivers.toString()
            binding.tvTotalVehicle.text = intent.getStringExtra("totalVehicle")

        }

    }

    private fun setToolbar() {
        binding.toolbar.tvTitle.text = "Driving Behaviour"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setTotalVoilationChart(
        harshAcc: Int,
        harshBreaking: Int,
        overspeed: Int,
        rashTurn: Int,
        riskyDriver: Int,
        exellentDriver: Int
    ) {
        val pieEntries = ArrayList<PieEntry>()
        val label = "type"

        //initializing data

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["1"] = harshAcc
        typeAmountMap["2"] = harshBreaking
        typeAmountMap["3"] = overspeed
        typeAmountMap["4"] = rashTurn
        typeAmountMap["5"] = riskyDriver
        typeAmountMap["6"] = exellentDriver

        //initializing colors for the entries

        //initializing colors for the entries
        val colors2 = ArrayList<Int>()
        colors2.add(Color.parseColor("#82A9F7"))
        colors2.add(Color.parseColor("#FFD200"))
        colors2.add(Color.parseColor("#FF4646"))
        colors2.add(Color.parseColor("#c71585"))
        colors2.add(Color.parseColor("#FF6D2D"))
        colors2.add(Color.parseColor("#A2B06D"))

        //input data and fit data into pie chart entry


        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        //providing color list for coloring different entries
        pieDataSet.colors = colors2
        //grouping the data set from entry to chart
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false)

        binding.fuelChart.data = pieData

        binding.fuelChart.setDrawEntryLabels(false)
        binding.fuelChart.isRotationEnabled = true
        binding.fuelChart.isDrawHoleEnabled = false
        binding.fuelChart.description = null
        binding.fuelChart.setDrawEntryLabels(false)
        binding.fuelChart.legend.isEnabled = false
        binding.fuelChart.animateY(3000)
        binding.fuelChart.invalidate()
        binding.fuelChart.setHoleColor(resources.getColor(R.color.primary_little_fade))
    }



    private fun setDriverClassificationChart(
        riskyDriver: Int,
        exellentDriver: Int,
        moderateDriver: Int
    ) {
        val pieEntries = ArrayList<PieEntry>()
        val label = "type"

        //initializing data

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["1"] = riskyDriver
        typeAmountMap["2"] = exellentDriver
        typeAmountMap["3"] = moderateDriver
        //initializing colors for the entries

        //initializing colors for the entries
        val colors2 = ArrayList<Int>()
        colors2.add(Color.parseColor("#FF4646"))
        colors2.add(Color.parseColor("#A2B06D"))
        colors2.add(Color.parseColor("#FFD200"))

        //input data and fit data into pie chart entry


        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        //providing color list for coloring different entries
        pieDataSet.colors = colors2
        //grouping the data set from entry to chart
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false)

        binding.driverClassificationChart.data = pieData

        binding.driverClassificationChart.setDrawEntryLabels(false)
        binding.driverClassificationChart.isRotationEnabled = true
        binding.driverClassificationChart.isDrawHoleEnabled = false
        binding.driverClassificationChart.description = null
        binding.driverClassificationChart.setDrawEntryLabels(false)
        binding.driverClassificationChart.legend.isEnabled = false
        binding.driverClassificationChart.animateY(3000)
        binding.driverClassificationChart.invalidate()
        binding.driverClassificationChart.setHoleColor(resources.getColor(R.color.primary_little_fade))
    }

    override fun getDriverTotalVoilationData(totalVoilationDataModel: TotalVoilationDataModel) {
        val totalNumber = totalVoilationDataModel.data[0].HAPer + totalVoilationDataModel.data[0].HBPer + totalVoilationDataModel.data[0].OSPer + totalVoilationDataModel.data[0].RTPer
        binding.tvHarshAcc.text =
            String.format(Locale.ENGLISH,"%.2f", (totalVoilationDataModel.data[0].HAPer.toFloat() / totalNumber.toFloat()) * 100).toDouble()
                .toString() + " %"
        binding.tvHarshBreak.text =
            String.format(Locale.ENGLISH,"%.2f", (totalVoilationDataModel.data[0].HBPer.toFloat() / totalNumber.toFloat()) * 100).toDouble()
                .toString() + " %"
        binding.tvHarshOverspeed.text =
            String.format(Locale.ENGLISH,"%.2f", (totalVoilationDataModel.data[0].OSPer.toFloat() / totalNumber.toFloat()) * 100).toDouble()
                .toString() + " %"
        binding.tvRashTurn.text =
            String.format(Locale.ENGLISH,"%.2f", (totalVoilationDataModel.data[0].RTPer.toFloat() / totalNumber.toFloat()) * 100).toDouble()
                .toString() + " %"
        binding.tvTotalVoilationRiskyDrivers.text =
            String.format(Locale.ENGLISH,"%.2f", (totalVoilationDataModel.data[0].RDPer.toFloat() / totalNumber.toFloat()) * 100).toDouble()
                .toString() + " %"
        binding.tvTotalVoilationExDriver.text =
            String.format(Locale.ENGLISH,"%.2f", (totalVoilationDataModel.data[0].ExDPer.toFloat() /totalNumber.toFloat()) * 100).toDouble()
                .toString() + " %"


        setTotalVoilationChart(
            totalVoilationDataModel.data[0].HAPer.toInt(),
            totalVoilationDataModel.data[0].HBPer.toInt(),
            totalVoilationDataModel.data[0].OSPer.toInt(),
            totalVoilationDataModel.data[0].RTPer.toInt(),
            totalVoilationDataModel.data[0].RDPer.toInt(),
            totalVoilationDataModel.data[0].ExDPer.toInt()
        )
    }

    override fun getExcellentDriversData(excellentDriverDataModel: ExcellentDriverDataModel) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvExcellentDrivers.layoutManager = layoutManager
        adapter = ExcellentDriverAdapter(this, excellentDriverDataModel.data)
        binding.rvExcellentDrivers.adapter = adapter

    }

    override fun getRiskyDriverData(riskyDriverModel: RiskyDriverModel) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvRiskyDrivers.layoutManager = layoutManager
        riskyAdapter = RiskyDriverAdapter(this, riskyDriverModel.data)
        binding.rvRiskyDrivers.adapter = riskyAdapter

    }

    override fun isNetworkConnected(): Boolean {
        return true
    }

    override fun isShowLoading(): Boolean {
        return true
    }

    override fun isHideLoading(): Boolean {
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }
}