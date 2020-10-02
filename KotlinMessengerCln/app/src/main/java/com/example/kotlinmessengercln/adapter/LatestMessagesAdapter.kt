package com.example.kotlinmessengercln.adapter

import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.model.ChatMessage
import com.example.kotlinmessengercln.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessagesAdapter(val chatMessage: ChatMessage): Item<ViewHolder>() {
    var chatPartnerUser: Users? = null
    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.latestMessagesChatTextview.text = chatMessage.text

        val chatPartnerId: String
        if (chatMessage.fromMessageId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toMessageId.toString()
        }else {
            chatPartnerId = chatMessage.toMessageId.toString()
        }
        val ref = FirebaseFirestore.getInstance().collection("Users").document(chatPartnerId)
        ref.get().addOnSuccessListener {
            chatPartnerUser = it.toObject(Users::class.java)
              viewHolder.itemView.latestMessagesUsernameTextview.text = chatPartnerUser?.username
              Picasso.get().load(chatPartnerUser?.downloadUrl).into(viewHolder.itemView.imageViewLatestMessages)



        }


    }
}