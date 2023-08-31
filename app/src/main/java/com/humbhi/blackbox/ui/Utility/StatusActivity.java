package com.humbhi.blackbox.ui.Utility;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.databinding.ActivityStatusBinding;


public class StatusActivity extends AppCompatActivity {
    ActivityStatusBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolBar.ivMenu.setVisibility(View.GONE);
        binding.toolBar.ivBack.setVisibility(View.VISIBLE);
        binding.toolBar.ivBell.setVisibility(View.GONE);
        binding.toolBar.tvTitle.setText("User Guidelines");
        binding.toolBar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent mainIntent = getIntent();
        if(mainIntent.hasExtra("status")){
            if(mainIntent.getStringExtra("status").equals("Expired")){
                binding.header.setText("The renewal process will take 4 hours to restart the service.");
                binding.textView1.setVisibility(View.GONE);
            }
            else if(mainIntent.getStringExtra("status").equals("Inactive Device(PD)")){
                binding.header.setText("The activation process will take minimum 7 working days.");
                binding.textView1.setText("In reference to the issue being faced by the customer during the time of Renewal/Reactivation of their device, we would like to clarify that this happens usually when the customer doesn't recharge the device timely or before the eSIM validity expiry. Actually, we provide a validity certificate to the customer during the time of installation of the device in the vehicle for the first time. The customer who recharges the device timely never faces such an issue.\n" +
                        "\n*Permanent Disconnection (PD): The E-SIM placed in the device is permanently disconnected by the eSIM Provider after 1 month from the date of expiry of the E-SIMs if the customer doesn't recharge/renew the device(eSIM) timely. To revive the E-SIMs we have to go through the Resumption Process which takes 7 working days minimum. The whole process is performed under the eSIM provider's guidelines which are mentioned below:\n" +
                        "\nThe day when the customer makes the payment anytime between 09.30 AM to 03.00 PM\n" +
                        "The same day our Account Team verify the payment & confirm the status in the evening.\n" +
                        "The next day if the payment is confirmed, we send the eSIM(ICCID) to the eSIM provider to procure IMSI which takes 2-3 working days(excl. Sat-Sun)\n" +
                        "\nOnce the IMSI status gets confirmed or procured by the eSIM Provider, we raise the Service Request(SR) which results us activating the BSNL profile and it takes 3-4 working days(Excl. Sat-Sun).\n" +
                        "\nAfter the BSNL profile activation, we've to reset(hard-reset) the device where we need the customer's help to do the same so that we can perform RSU trigger activity.\n" +
                        "\n*RSU(Remote Switching Unit) Trigger: It is a technical activity through which we forcefully connect the device to the network(BSNL).\n" +
                        "This is the most challenging phase for us while performing the RSU Trigger activity. When we trigger the RSU we get the report by the next day whether the RSU triggered successfully or not.\n" +
                        "In case if the RSU is triggered successfully:\n" +
                        "\nOnce the RSU is triggered successfully, we raise a service request (SR) again which result us activating the 2nd Profile like Airtel/Vodafone and it takes 1-3 days\n" +
                        "\nIn case if the RSU is failed to trigger:\n" +
                        "The process is repeated until the RSU gets triggered successfully and it leads to a delay in the Reactivation.\n" +
                        "This is Sensorise Process to revive a Permanently Disconnected(PD) eSIM. Process It will be take 7 to 8 Working Days ");
            }
        }
        else{
            binding.header.setText(mainIntent.getStringExtra("transStatus"));
        }
    }
}
