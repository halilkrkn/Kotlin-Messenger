package com.example.kotlinmessengercln.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessengercln.adapter.NewMessageGroupAdapter
import com.example.kotlinmessengercln.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

        val ref = database.collection("Users")
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
                            adapter.add(NewMessageGroupAdapter(username,downloadUrl))

                            recyclerView_newMessage.adapter = adapter

                        }

 */
                        // Yukardaki For Loop içerisindeki ile Aynı yöntem
                        documents.forEach {
                            val username = it.get("username") as String
                            val profileImageUrl = it.get("downloadUrl") as String

                            adapter.add(
                                NewMessageGroupAdapter(
                                    username,
                                    profileImageUrl
                                )
                            )

                            adapter.setOnItemClickListener { item, view ->

                                val intent = Intent(view.context,ChatLogActivity::class.java)
                                intent.putExtra(USER_KEY,username)
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



