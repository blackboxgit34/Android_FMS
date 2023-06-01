package com.humbhi.blackbox.ui.Utility;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.humbhi.blackbox.R;


public class ApproveOTPFragment extends Fragment implements View.OnClickListener {

    TextView textView;
    Button approveBtn;
    public String otpText = "";
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.approve_otp_layout,container,false);
        textView = (TextView) view.findViewById(R.id.otptext);
        approveBtn = (Button) view.findViewById(R.id.approve);
        approveBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //text = (TextView) getActivity().findViewById(R.id.otptext);
        //approveBtn = (Button) getActivity().findViewById(R.id.approve);
        //approveBtn.setOnClickListener(this);
        communicator = (Communicator) getActivity();
        textView.setText(otpText);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.approve){

            communicator.respond(otpText);
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            //Toast.makeText(getActivity(),"called",Toast.LENGTH_SHORT).show();
        }
    }

    public void setOtpText(String data){
        otpText = data;
    }
}
