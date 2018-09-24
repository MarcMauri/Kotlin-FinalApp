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
import com.google.firebase.firestore.*
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.adapters.ChatAdapter
import es.marcmauri.finalapp.models.Message
import es.marcmauri.finalapp.utils.toast
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.HashMap


class ChatFragment : Fragment() {

    private lateinit var _view: View

    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatBDRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpChatBtn()

        subscribeToChatMessages()

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

    private fun setUpChatBtn() {
        _view.button_send.setOnClickListener {
            val messageText = _view.et_message.text.toString()
            if (messageText.isNotEmpty()) {
                val photo = currentUser.photoUrl?.let { currentUser.photoUrl.toString() } ?: run { "" }
                val message = Message(currentUser.uid, messageText, photo, Date())
                saveMessage(message)
                _view.et_message.setText("")
            }
        }
    }

    /* This function will save a message into Firebase */
    private fun saveMessage(message: Message) {
        val newMessage = HashMap<String, Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageURL"] = message.profileImageURL
        newMessage["sentAt"] = message.sentAt

        chatBDRef.add(newMessage)
                .addOnCompleteListener {
                    activity!!.toast("Message added!")
                }
                .addOnFailureListener {
                    activity!!.toast("Message error, try again!")
                }
    }

    private fun subscribeToChatMessages() {
        chatSubscription = chatBDRef
                .orderBy("sentAt", Query.Direction.DESCENDING)
                .limit(100)
                .addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
                    override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                        exception?.let {
                            activity!!.toast("Exception!")
                            return
                        }

                        snapshot?.let {
                            messageList.clear()
                            val messages = it.toObjects(Message::class.java)
                            messageList.addAll(messages.asReversed())
                            adapter.notifyDataSetChanged()
                            _view.recyclerView.smoothScrollToPosition(messageList.size)
                        }
                    }
                })
    }


    override fun onDestroy() {
        chatSubscription?.remove()
        super.onDestroy()
    }
}
