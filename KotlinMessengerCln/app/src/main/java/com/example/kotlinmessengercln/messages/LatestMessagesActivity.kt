package com.example.kotlinmessengercln.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kotlinmessengercln.registerLogin.LoginActivity
import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.adapter.ChatLogFromAdapter
import com.example.kotlinmessengercln.adapter.ChatLogToAdapter
import com.example.kotlinmessengercln.adapter.LatestMessagesAdapter
import com.example.kotlinmessengercln.adapter.NewMessageGroupAdapter
import com.example.kotlinmessengercln.model.ChatMessage
import com.example.kotlinmessengercln.model.Users
import com.example.kotlinmessengercln.registerLogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.activity_new_message.*

class LatestMessagesActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    companion object {
        var currentUser: Users? = null
        var TAG = "LatestMessages"
    }

    val adapter = GroupAdapter<ViewHolder>()
    var toUser: Users? = null


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        /*  When ile Aynısı sadece if kullanılarak yapıldı.
        if (item.itemId == R.id.menu_new_messages){
              // new Message Avtivity Yönlendirme
          }
          if (item.itemId == R.id.menu_sign_out){
              val intent = Intent(applicationContext,LoginActivity::class.java)
              startActivity(intent)
          }
          */
        // When = switch Case  veya if-else gibi
        when (item.itemId) {
            R.id.menu_new_messages -> {
                // new Message Activity Intent
                val intent = Intent(
                    applicationContext,
                    NewMessageActivity::class.java
                )
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                mAuth.signOut() // Kullanıcı Çıkışı oluşturuldu.
                val intent = Intent(applicationContext, LoginActivity::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        recyclerViewLatestMessages.adapter = adapter
        recyclerViewLatestMessages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        //setupDummyRows()
        listenForLatestMessages()
        fetchCurrentUser()
        //currentUser()
        verifyUserIsLoggedIn()

    }


    //Kullanıcı ilk girişini yaptıktan sonra otomatik girişi yapılacak.
    fun currentUser() {
        // Eğer kayıtlı ve ilk girşini yaptıysa artık ondan sonra güncel olarak girişi yapılacak
        val currentUser = mAuth.currentUser
        if (currentUser != null) {

            val intent = Intent(applicationContext, LatestMessagesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Kullanıcı doğrulaması yapıldı.
    private fun verifyUserIsLoggedIn() {
        val uid = mAuth.uid
        if (uid == null) {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    // ChatLog Activityde Mesajlaşma esnasında Kullanıcın profil fotosunun gösterilmesi için
    private fun fetchCurrentUser() {
        val userId = FirebaseAuth.getInstance().uid ?: ""

        val docRef = database.collection("Users").document(userId).get().addOnSuccessListener {

            currentUser = it.toObject(Users::class.java)
            Log.d("LatestMessages", "Current User : ${currentUser?.downloadUrl}")


        }
    }

    private val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {

            adapter.add(LatestMessagesAdapter(it))
        }

    }


    private fun listenForLatestMessages() {
        val toMessageId = toUser?.userId
        val fromMessageId = FirebaseAuth.getInstance().uid
        val chatId = database.collection("User-Messages/$fromMessageId/$toMessageId").document().id

        val ref = database.collection("latest-messages").orderBy("date", Query.Direction.DESCENDING)

        ref.addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(applicationContext, error.localizedMessage, Toast.LENGTH_SHORT).show()

            }else{
                if (value != null) {
                    if (!value.isEmpty) {

                        val documents = value?.documents
                        if (documents != null) {
                            for (doc in documents) {
                                Log.d(TAG, "chat id: ${doc.id}")

                                val chatMessage = doc.toObject(ChatMessage::class.java)
                                if (chatMessage != null) {
                                    Log.d(TAG, "chat messages : ${chatMessage.text}")
                                    latestMessagesMap[doc.id] = chatMessage

                                    adapter.setOnItemClickListener { item, view ->
                                        Log.d(TAG, "123")
                                        val intent = Intent(applicationContext,ChatLogActivity::class.java)
                                        val row = item as LatestMessagesAdapter

                                       intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartnerUser)
                                        startActivity(intent)

                                    }
                                    refreshRecyclerViewMessages()
                                }

                            }
                        }

                    }
                }

            }

        }

    }
}

