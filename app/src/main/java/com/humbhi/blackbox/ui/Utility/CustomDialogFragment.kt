package com.humbhi.blackbox.ui.Utility

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.humbhi.blackbox.R

class CustomDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_layout, null)

        val messageTextView = view.findViewById<TextView>(R.id.tvMessage)
        messageTextView.text = "* All scores are out of 100. e.g., 100 represents no violation, 0 represents excessive violation."

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        builder.setCancelable(true)
        builder.setPositiveButton("OK", null)
        return builder.create()
    }

    companion object {
        fun newInstance(): CustomDialogFragment {
            return CustomDialogFragment()
        }
    }
}
