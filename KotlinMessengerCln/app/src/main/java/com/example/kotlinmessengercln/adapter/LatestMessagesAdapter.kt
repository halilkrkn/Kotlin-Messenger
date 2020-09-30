package com.example.kotlinmessengercln.adapter

import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.model.ChatMessage
import com.example.kotlinmessengercln.model.Users
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessagesAdapter(val chatMessage: ChatMessage): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
      //  viewHolder.itemView.latestMessagesUsernameTextview.text = users.username
       viewHolder.itemView.latestMessagesChatTextview.text = chatMessage.text
      //  Picasso.get().load(users.downloadUrl).into(viewHolder.itemView.imageViewLatestMessages)

    }
}