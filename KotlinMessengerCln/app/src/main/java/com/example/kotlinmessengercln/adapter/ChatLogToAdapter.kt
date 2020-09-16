package com.example.kotlinmessengercln.adapter

import com.example.kotlinmessengercln.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ChatLogToAdapter: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}