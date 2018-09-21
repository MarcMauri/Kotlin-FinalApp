package es.marcmauri.finalapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.adapters.ChatAdapter
import es.marcmauri.finalapp.models.Message
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*


class ChatFragment : Fragment() {

    private lateinit var _view: View

    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatBDRef: CollectionReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpChatButton()

        return _view
    }

    private fun setUpChatDB() {
        chatBDRef = store.collection("chat")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)

        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.adapter = adapter
    }

    private fun setUpChatButton() {
        _view.button_send.setOnClickListener {
            val messageText = _view.et_message.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(currentUser.uid, messageText, currentUser.photoUrl.toString(), Date())
                // Guardaremos el mensaje en Firebase
                _view.et_message.setText("")
            }
        }
    }


}
