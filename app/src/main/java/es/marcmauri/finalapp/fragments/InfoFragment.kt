package es.marcmauri.finalapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.squareup.picasso.Picasso

import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.utils.CircleTransform
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatBDRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_info, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()

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
        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).fit().centerCrop().transform(CircleTransform()).into(_view.iv_infoAvatar)
        } ?: run {
            Picasso.get().load(R.drawable.ic_person).fit().centerCrop().transform(CircleTransform()).into(_view.iv_infoAvatar)
        }
    }
}
