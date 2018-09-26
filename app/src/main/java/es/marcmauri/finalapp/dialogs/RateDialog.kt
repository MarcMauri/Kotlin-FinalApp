package es.marcmauri.finalapp.dialogs

import android.support.v7.app.AlertDialog
import android.support.v4.app.DialogFragment

import android.app.Dialog
import android.os.Bundle
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.utils.toast

class RateDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        AlertDialog.Builder(context!!)
                .setTitle(R.string.dialog_title)
                .setView(R.layout.dialog_rate)
                .setPositiveButton(R.string.dialog_ok) { dialog, which ->
                    activity!!.toast("Pressed Ok")
                }
                .setNegativeButton(R.string.dialog_cancel) { dialog, which ->
                    activity!!.toast("Pressed Cancel")
                }

        return super.onCreateDialog(savedInstanceState)
    }
}