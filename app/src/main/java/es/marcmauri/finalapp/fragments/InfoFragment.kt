package es.marcmauri.finalapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.models.TotalMessagesEvent
import es.marcmauri.finalapp.utils.CircleTransform
import es.marcmauri.finalapp.utils.RxBus
import es.marcmauri.finalapp.utils.toast
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_info.view.*
import java.util.EventListener

class InfoFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatBDRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null
    private lateinit var infoBusListener: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_info, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()

        // Total Messages Firebase style
        //subscribeToTotalMessagesFirebaseStyle()

        // Total Messages Event Bus + Reactive Style
        subscribeToTotalMessagesEventBusReactiveStyle()

        return _view
    }

    private fun setUpChatDB() {
        chatBDRef = store.collection("chat")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI() {
        _view.tv_infoEmail.text = currentUser.email
        _view.tv_infoName.text = if (currentUser.displayName.isNullOrEmpty()) getString(R.string.info_no_name) else currentUser.displayName
        currentUser.photoUrl?.let { photo ->
            Picasso.get().load(photo).fit().centerCrop().transform(CircleTransform()).into(_view.iv_infoAvatar)
        } ?: run {
            Picasso.get().load(R.drawable.ic_person).fit().centerCrop().transform(CircleTransform()).into(_view.iv_infoAvatar)
        }
    }

    private fun subscribeToTotalMessagesFirebaseStyle() {
        chatSubscription = chatBDRef.addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let {
                    activity!!.toast("Exception!")
                    return
                }

                querySnapshot?.let {
                    _view.tv_infoTotalMessages.text = "${it.size()}"
                }
            }
        })
    }

    private fun subscribeToTotalMessagesEventBusReactiveStyle() {
        infoBusListener = RxBus.listen(TotalMessagesEvent::class.java).subscribe {
            _view.tv_infoTotalMessages.text = "${it.total}"
        }
    }


    //** Events **//

    override fun onDestroyView() {
        infoBusListener.dispose()
        chatSubscription?.remove()
        super.onDestroyView()
    }
}
