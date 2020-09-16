package com.example.kotlinmessengercln.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.adapter.ChatLogFromAdapter
import com.example.kotlinmessengercln.adapter.ChatLogToAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)



       val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = username

        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ChatLogFromAdapter())
        adapter.add(ChatLogToAdapter())
        adapter.add(ChatLogFromAdapter())
        adapter.add(ChatLogToAdapter())
        adapter.add(ChatLogFromAdapter())
        adapter.add(ChatLogToAdapter())
        adapter.add(ChatLogFromAdapter())
        adapter.add(ChatLogToAdapter())

        recyclerViewChatLog.adapter = adapter
    }
}
