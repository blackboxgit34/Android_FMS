package com.humbhi.blackbox.ui.ui.alerts.setAlert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.humbhi.blackbox.databinding.ActivitySetAlertBinding

class SetAlertActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySetAlertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}