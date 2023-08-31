package com.humbhi.blackbox.ui.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class RetryDialogFragment : DialogFragment() {
    interface RetryDialogListener {
        fun onRetry()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Network request failed. Retry?")
                .setPositiveButton("Retry") { _, _ ->
                    val listener = activity as RetryDialogListener?
                    listener?.onRetry()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
