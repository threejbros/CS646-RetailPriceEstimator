package com.zybooks.retailpriceestimator

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class InvalidOutputDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.invalid_output)
        builder.setMessage(R.string.invalid_output_message)
        builder.setPositiveButton(R.string.ok, null)
        return builder.create()
    }
}