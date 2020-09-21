package com.example.kotlinmessengercln.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.adapter.ChatLogFromAdapter
import com.example.kotlinmessengercln.adapter.ChatLogToAdapter
import com.example.kotlinmessengercln.model.ChatMessage
import com.example.kotlinmessengercln.model.Users
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    private lateinit var database : FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storage : FirebaseStorage

     val adapter = GroupAdapter<ViewHolder>()
     //var toUser = Users("","","","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

     recyclerViewChatLog.adapter = adapter


        database = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
      val  toUser = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
        if (toUser != null){
            supportActionBar?.title = toUser.username
        }

      //setupDummyData()
      listenForMessages()

    }



    private fun listenForMessages(){
       // val chatId = database.collection("Message").document().id
        val databaseMessage = database.collection("Message").orderBy("date",Query.Direction.ASCENDING)
        databaseMessage.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                exception.printStackTrace()
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (snapshot != null){

                    if (!snapshot.isEmpty){


                        //  val chatMessage = ChatMessage("","","","")
                        val documentsMessage = snapshot.documents
                        // val adapter = GroupAdapter<ViewHolder>()






                        for (document in documentsMessage){

                            val textMessage = document.get("text") as String
                            val fromMessageId = document.get("fromMessageId") as String
                            val toMessageId = document.get("toMessageId") as String
                            val chatMessage = ChatMessage("",textMessage,"","")
                            val frmMessageId = ChatMessage("","",fromMessageId,"")
                            val toMessageID = ChatMessage("","","",toMessageId)
                            if (textMessage != null){

                                if (frmMessageId.fromMessageId == FirebaseAuth.getInstance().uid){
                                    Log.d("ChatLogActivity", "111111111111")
                                    Log.d("ChatLogActivity", textMessage)

                                    adapter.add(ChatLogFromAdapter(chatMessage))
                                }else {
                                    Log.d("ChatLogActivity", "22222222222")
                                    adapter.add(ChatLogToAdapter(chatMessage))
                                }
                            }


                            // val fromMessage = ChatMessage("",textMessage,"","")
                            //val fromUser = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
                            // adapter.add(ChatLogFromAdapter(fromMessage))

                        }


                        // recyclerViewChatLog.adapter = adapter



                    }
                }
// TODO: 19.09.2020 LastestMessagedan güncel kullanıcı biligisini çekeceksin


            }


        }


    }


    fun performSendMessage(view: View){



        val chatId = database.collection("Message").document().id
        val textMessage = enterMessageChatLogEdittext.text.toString()
        val fromMessageId = FirebaseAuth.getInstance().uid // burası sıkıntılı
        val username = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
        val toMessageId = username?.userId
        val date = Timestamp.now()
        val chatMessage = ChatMessage(chatId,textMessage,fromMessageId,toMessageId!!,date)



        database.collection("Message").document(chatId).set(chatMessage).addOnSuccessListener {
                Log.d("ChatLogActivity", "new Message: ${chatId!!}")
        }


    }
/*
    private fun setupDummyData(){

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatLogFromAdapter("From Message"))
        adapter.add(ChatLogToAdapter("To Message"))
        adapter.add(ChatLogFromAdapter("From Message"))
        adapter.add(ChatLogToAdapter("To Message"))
        adapter.add(ChatLogFromAdapter("From Message"))
        adapter.add(ChatLogToAdapter("To Message"))
        adapter.add(ChatLogFromAdapter("From Message"))
        adapter.add(ChatLogToAdapter("To Message"))
        adapter.add(ChatLogFromAdapter("From Message"))
        adapter.add(ChatLogToAdapter("To Message"))
        recyclerViewChatLog.adapter = adapter
    }

     val databaseMessage = database.collection("Message")
        databaseMessage.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                exception.printStackTrace()
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (snapshot != null){

                    if (!snapshot.isEmpty){


                        val chatMessage = ChatMessage("","","","")
                        val documentsMessage = snapshot.documents
                        // val adapter = GroupAdapter<ViewHolder>()

                        documentsMessage.forEach {

                            val textMessage = it.get("text") as String

                                val fromMessage = ChatMessage("",textMessage,"","")
                                //val fromUser = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
                                adapter.add(ChatLogFromAdapter(fromMessage.text.toString()))

                                // Burası çalışıyor.
                                val toMessage = ChatMessage("",textMessage,"","")
                                adapter.add(ChatLogToAdapter(toMessage.text.toString()))


                            // recyclerViewChatLog.adapter = adapter

                        }

                    }
                }
// TODO: 19.09.2020 LastestMessagedan güncel kullanıcı biligisini çekeceksin


            }


        }




 */
    /*

         val databaseMessage = database.collection("Message")
        databaseMessage.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                exception.printStackTrace()
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (snapshot != null){

                    if (!snapshot.isEmpty){


                      //  val chatMessage = ChatMessage("","","","")
                        val documentsMessage = snapshot.documents
                        // val adapter = GroupAdapter<ViewHolder>()






                            for (document in documentsMessage){

                                val textMessage = document.get("text") as String
                                val fromMessageId = document.get("fromMessageId") as String
                                val toMessageId = document.get("toMessageId") as String
                                val chatMessage = ChatMessage("",textMessage,"","")
                                val frmMessageId = ChatMessage("","",fromMessageId,"")
                                val toMessageID = ChatMessage("","","",toMessageId)
                                if (textMessage != null){

                                    if (frmMessageId.fromMessageId == FirebaseAuth.getInstance().uid){
                                        Log.d("ChatLogActivity", "111111111111")
                                        Log.d("ChatLogActivity", textMessage)

                                        adapter.add(ChatLogFromAdapter(chatMessage))
                                    }else {
                                        Log.d("ChatLogActivity", "22222222222")
                                        adapter.add(ChatLogToAdapter(chatMessage))
                                    }
                                }


                               // val fromMessage = ChatMessage("",textMessage,"","")
                                //val fromUser = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
                               // adapter.add(ChatLogFromAdapter(fromMessage))

                            }


                                // recyclerViewChatLog.adapter = adapter



                    }
                }
// TODO: 19.09.2020 LastestMessagedan güncel kullanıcı biligisini çekeceksin


            }


        }




     */
}


