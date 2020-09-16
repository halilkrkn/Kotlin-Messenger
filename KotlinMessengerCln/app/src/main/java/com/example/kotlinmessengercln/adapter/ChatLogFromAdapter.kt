package com.example.kotlinmessengercln.adapter

import com.example.kotlinmessengercln.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ChatLogFromAdapter : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return  R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }
}