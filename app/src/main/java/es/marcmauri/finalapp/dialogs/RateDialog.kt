package es.marcmauri.finalapp.dialogs

import android.support.v7.app.AlertDialog
import android.support.v4.app.DialogFragment

import android.app.Dialog
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.models.NewRateEvent
import es.marcmauri.finalapp.models.Rate
import es.marcmauri.finalapp.utils.RxBus
import es.marcmauri.finalapp.utils.toast
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class RateDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rate, null)

        return AlertDialog.Builder(context!!)
                .setTitle(R.string.dialog_title)
                .setView(view)
                .setPositiveButton(R.string.dialog_ok) { _, _ ->
                    activity!!.toast("Pressed Ok")
                    val textRate = view.et_rateFeedback.text.toString()
                    val imgURL = FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString() ?: run { "" }
                    val rate = Rate(textRate, view.ratingBarFeedback.rating, Date(), imgURL)
                    // Publicar el nuevo Rate en el Event Bus
                    RxBus.publish(NewRateEvent(rate))
                }
                .setNegativeButton(R.string.dialog_cancel) { _, _ ->
                    activity!!.toast("Pressed Cancel")
                }
                .create()
    }
}