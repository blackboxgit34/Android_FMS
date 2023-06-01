package com.humbhi.blackbox.ui.ui.settings

import android.R
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.humbhi.blackbox.databinding.ActivitySettingsBinding
import com.humbhi.blackbox.ui.adapters.AllIconsAdapter
import com.humbhi.blackbox.ui.data.models.AllIconsModel
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Response
import java.io.IOException


class SettingsActivity : AppCompatActivity(), RetrofitResponse {
    private lateinit var binding:ActivitySettingsBinding
    var list: ArrayList<AllIconsModel> = java.util.ArrayList<AllIconsModel>()
    private lateinit var adapter: AllIconsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "Setting & Customization"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        // on radio button check change
//        if(binding.idRBLight.isChecked) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//        else if(binding.idRBDark.isChecked) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }
//        else
//        {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        }

        getVehicleIcons()
    }

    private fun getVehicleIcons() {
        Retrofit2(this, this, Constants.REQ_GET_ALL_VEHICLE_ICONS,
            Constants.GET_ALL_VEHICLE_ICONS.toString())
            .callService(true)
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {

        when(requestCode){

            Constants.REQ_GET_ALL_VEHICLE_ICONS->if (response!!.isSuccessful){
                try {
                    list.clear()
                    val jsonArray = JSONArray(response!!.body()!!.string())
                    var i = 0

                    while (i < jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val model = AllIconsModel()
                        model.id = obj.getString("ID")
                        model.text = obj.getString("Text")
                        list.add(model)
                        i++
                    }
                    val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                        3,  //The number of Columns in the grid
                        LinearLayoutManager.VERTICAL
                    )
                    binding.rvRecycler.setLayoutManager(
                        staggeredGridLayoutManager
                    )
                    adapter = AllIconsAdapter(object : AllIconsAdapter.VehicleIconSelection {
                        override fun onVehicleSelection(position: Int) {
                         /*   when(list[position].text){
                                "Car"->{
                                    binding.llCars.visibility = View.VISIBLE
                                    binding.llTrucks.visibility = View.GONE
                                    binding.llBus.visibility = View.GONE
                                    binding.tvHeading.visibility = View.VISIBLE
                                    binding.tvSelectVehicle.visibility = View.VISIBLE
                                }
                                "Bus"->{
                                    binding.llCars.visibility = View.GONE
                                    binding.llTrucks.visibility = View.GONE
                                    binding.llBus.visibility = View.VISIBLE
                                    binding.tvHeading.visibility = View.VISIBLE
                                    binding.tvSelectVehicle.visibility = View.VISIBLE
                                }
                                "Ambulance"->{
                                    binding.llCars.visibility = View.GONE
                                    binding.llTrucks.visibility = View.GONE
                                    binding.llBus.visibility = View.GONE
                                    binding.tvHeading.visibility = View.VISIBLE
                                    binding.tvSelectVehicle.visibility = View.VISIBLE
                                }
                                "OilTanker"->{
                                    binding.llCars.visibility = View.GONE
                                    binding.llTrucks.visibility = View.GONE
                                    binding.llBus.visibility = View.GONE
                                    binding.tvHeading.visibility = View.VISIBLE
                                    binding.tvSelectVehicle.visibility = View.VISIBLE
                                }
                                "Truck"->{
                                    binding.llCars.visibility = View.GONE
                                    binding.llTrucks.visibility = View.VISIBLE
                                    binding.llBus.visibility = View.GONE
                                    binding.tvHeading.visibility = View.VISIBLE
                                    binding.tvSelectVehicle.visibility = View.VISIBLE
                                }
                                "Other"->{
                                    binding.llCars.visibility = View.VISIBLE
                                    binding.llTrucks.visibility = View.GONE
                                    binding.llBus.visibility = View.GONE
                                    binding.tvHeading.visibility = View.VISIBLE
                                    binding.tvSelectVehicle.visibility = View.VISIBLE
                                }
                                "Bike"->{
                                    binding.llCars.visibility = View.GONE
                                    binding.llTrucks.visibility = View.GONE
                                    binding.llBus.visibility = View.GONE
                                    binding.tvHeading.visibility = View.VISIBLE
                                    binding.tvSelectVehicle.visibility = View.VISIBLE
                                }
                            } */
                            Toast.makeText(this@SettingsActivity,"This feature is coming soon.", Toast.LENGTH_SHORT).show()
                        }
                    }, list)
                    binding.rvRecycler.setAdapter(adapter)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


}