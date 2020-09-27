package com.example.kotlinmessengercln.adapter

import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.model.Users
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatLogFromAdapter(val text: String?, val users: Users) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return  R.layout.chat_from_row
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewFromRow.text = text
        Picasso.get().load(users.downloadUrl).into(viewHolder.itemView.imageViewFromRow)
    }
}