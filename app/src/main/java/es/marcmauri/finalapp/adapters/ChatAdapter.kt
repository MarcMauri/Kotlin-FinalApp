package es.marcmauri.finalapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.models.Message
import es.marcmauri.finalapp.utils.CircleTransform
import es.marcmauri.finalapp.utils.inflate
import kotlinx.android.synthetic.main.fragment_chat_item_left.view.*
import kotlinx.android.synthetic.main.fragment_chat_item_right.view.*
import java.text.SimpleDateFormat

class ChatAdapter(val items: List<Message>, val userId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.fragment_chat_item_right
    private val layoutLeft = R.layout.fragment_chat_item_left


    override fun getItemViewType(position: Int) = if (items[position].authorId == userId) MY_MESSAGE else GLOBAL_MESSAGE

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MY_MESSAGE -> ViewHolderR(parent.inflate(layoutRight))
            else -> ViewHolderL(parent.inflate(layoutLeft))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            MY_MESSAGE -> (holder as ViewHolderR).bind(items[position])
            GLOBAL_MESSAGE -> (holder as ViewHolderL).bind(items[position])
        }
    }


    //** View holders **//
    class ViewHolderR(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) = with(itemView) {
            tv_messageRight.text = message.message
            tv_timeRight.text = SimpleDateFormat("hh:mm").format(message.sentAt)

            if (message.profileImageURL.isEmpty()) {
                Picasso.get().load(R.drawable.ic_person).resize(100, 100)
                        .centerCrop().transform(CircleTransform()).into(iv_profileRight)
            } else {
                Picasso.get().load(message.profileImageURL).resize(100, 100)
                        .centerCrop().transform(CircleTransform()).into(iv_profileRight)
            }
        }
    }

    class ViewHolderL(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) = with(itemView) {
            tv_messageLeft.text = message.message
            tv_timeLeft.text = SimpleDateFormat("hh:mm").format(message.sentAt)

            if (message.profileImageURL.isEmpty()) {
                Picasso.get().load(R.drawable.ic_person).resize(100, 100)
                        .centerCrop().transform(CircleTransform()).into(iv_profileLeft)
            } else {
                Picasso.get().load(message.profileImageURL).resize(100, 100)
                        .centerCrop().transform(CircleTransform()).into(iv_profileLeft)
            }
        }
    }
}