package com.humbhi.blackbox.ui.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityNotificationBinding
import com.humbhi.blackbox.ui.Utility.EndlessRecyclerOnScrollListener
import com.humbhi.blackbox.ui.adapters.NotificationListAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.NotificationResponseModel
import com.humbhi.blackbox.ui.data.network.RestClient

class NotificationActivity : AppCompatActivity(),NotificationView {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var adapter: NotificationListAdapter
    private lateinit var notificationPresenter: NotificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationPresenter = NotificationPresenterImpl(this, DataManagerImpl(RestClient.getRetrofitBuilderForHitec()))

        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Notifications"
        binding.toolbar.ivBack.setOnClickListener { finish() }
        notificationPresenter.getAllNotificationListAPI(CommonData.getCustIdFromDB())
    }

    override fun getNotificationListData(notificationDataList: NotificationResponseModel) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvNotifications.layoutManager = layoutManager
        binding.rvNotifications.adapter = adapter
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

    }
}