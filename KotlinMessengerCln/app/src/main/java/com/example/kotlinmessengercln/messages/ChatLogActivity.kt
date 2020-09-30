package com.example.kotlinmessengercln.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
     var toUser : Users? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

     recyclerViewChatLog.adapter = adapter


        database = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
       toUser = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
        if (toUser != null){
            supportActionBar?.title = toUser?.username
        }

      listenForMessages()

    }
// Cloud Firestore veritabanından User-Message koleksiyonundaki bilgileri güncel kullanıcıya göre text ve görüntüsünü çektik
// Mesajlaşmak için karşılıklı mesaj yollama olaylarını ve ek olarak kullanıcıların profil görüntüsünü de gösterdik.
    private fun listenForMessages(){


        val fromMessageId = FirebaseAuth.getInstance().uid
       // val username = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
        val toMessageId = toUser?.userId

        if(fromMessageId != null && toMessageId != null){

            //val databaseMessage = database.collection("User-Messages").document(fromMessageId).collection(toMessageId).orderBy("date",Query.Direction.ASCENDING)
            val databaseMessage = database.collection("User-Messages/$fromMessageId/$toMessageId").orderBy("date",Query.Direction.ASCENDING).get().addOnSuccessListener {
                val user = it.documents
                user.forEach {

                    val chatMessage = it.toObject(ChatMessage::class.java)
                    if (chatMessage != null) {
                        if (chatMessage.fromMessageId == FirebaseAuth.getInstance().uid) {
                            val currentUser = LatestMessagesActivity.currentUser
                            adapter.add(ChatLogFromAdapter(chatMessage.text, currentUser!!))
                        } else {
                            adapter.add(ChatLogToAdapter(chatMessage.text, toUser!!))
                        }


                    }

                }

            }

        }

    }

// User-Messages Database ini  modelini oluşturduğumuz ChatMessage modeline tanımladık ve database görüntüsünü oluşturduk.
    fun performSendMessage(view: View){

        val textMessage = enterMessageChatLogEdittext.text.toString()
        val fromMessageId = FirebaseAuth.getInstance().uid
        //val username = intent.getParcelableExtra<Users>(NewMessageActivity.USER_KEY)
        val toMessageId = toUser?.userId
        val date = Timestamp.now()
        val chatId = database.collection("User-Messages/$fromMessageId/$toMessageId").document().id
        val chatMessage = ChatMessage(chatId,textMessage,fromMessageId,toMessageId,date)

        if (fromMessageId == null) return
        if (toMessageId == null) return

        // Karşılıklı Mesajlaşmanın olduğu ChatLogActivity için bir database oluşturduk.
        val fromMessage = database.collection("User-Messages/$fromMessageId/$toMessageId").document(chatId).set(chatMessage)
        fromMessage.addOnSuccessListener {
           enterMessageChatLogEdittext.text.clear()
           recyclerViewChatLog.scrollToPosition((adapter.itemCount - 1))
        }
        // Karşılıklı Mesajlaşmanın olduğu Activity için bir database oluşturduk.
        val toMessage = database.collection("User-Messages/$toMessageId/$fromMessageId").document(chatId)
        toMessage.set(chatMessage).addOnSuccessListener {

        }

        // Mesajlaşmaların son durumlarını gösteren LatestMessagesActivity için de bir database oluşturduk.
        // Yukarıdaki database ile aynı  karmaşıklığı gidermek ve kullanımk rahatlığı oluşturmak için.
        val latestMessagesFromRef = database.collection("latest-messages").document(chatId)
        latestMessagesFromRef.set(chatMessage).addOnSuccessListener {

        }

        // Mesajlaşmaların son durumlarını gösteren LatestMessagesActivity için de bir database oluşturduk.
        // Yukarıdaki database ile aynı  karmaşıklığı gidermek ve kullanımk rahatlığı oluşturmak için.
        val latestMessagesToRef = database.collection("latest-messages").document(chatId)
        latestMessagesToRef.set(chatMessage).addOnSuccessListener {

        }

    }

}


