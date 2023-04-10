package com.zybooks.retailpriceestimator

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class zeroInputDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.invalid_input)
        builder.setMessage(R.string.zero_input_message)
        builder.setPositiveButton(R.string.ok, null)
        return builder.create()
    }
}