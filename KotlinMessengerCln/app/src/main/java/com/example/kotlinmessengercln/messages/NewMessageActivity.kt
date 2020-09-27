package com.example.kotlinmessengercln.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessengercln.adapter.NewMessageGroupAdapter
import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        supportActionBar?.title = "Select User"

        fetchUsers()
    }

    companion object{
        val USER_KEY= "USER_KEY"
    }
    private fun fetchUsers(){

        val ref = database.collection("Users").orderBy("date", Query.Direction.DESCENDING)
        ref.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty){



                        val documents = snapshot.documents
                        val adapter = GroupAdapter<ViewHolder>()

/*
                        for (document in documents) {

                            val username = document.get("username") as String
                            val downloadUrl = document.get("downloadUrl") as String
                             adapter.add(NewMessageGroupAdapter(Users(profileImageUrl,"",username,"")))

                            recyclerView_newMessage.adapter = adapter

                        }

 */
                        // Yukardaki For Loop içerisindeki ile Aynı yöntem
                        // Kullanıcı seçerken ki sayfaya üye olan kişilerin bilgilerini databaseden çektik ve gösterdik.
                        documents.forEach {
                            val username = it.get("username") as String
                            val profileImageUrl = it.get("downloadUrl") as String
                            val userId  = it.get("userId") as String

                            adapter.add(NewMessageGroupAdapter(Users(profileImageUrl,"",username,userId)))

                        // Oluşturulan row lara tıklama işlemleri yapıldı
                            adapter.setOnItemClickListener { item, view ->

                                val userItem = item as NewMessageGroupAdapter
                                val intent = Intent(view.context, ChatLogActivity::class.java)
                                //intent.putExtra(USER_KEY,userItem.user.userId)
                                intent.putExtra(USER_KEY,userItem.user)
                                startActivity(intent)
                                finish() // LatestNewMesage Activitye yönlendiriyor.


                            }
                            recyclerView_newMessage.adapter = adapter

                        }

                    }
                }
            }
        }
    }
}



