package com.example.kotlinmessengercln.messages

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.kotlinmessengercln.registerLogin.LoginActivity
import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class LatestMessagesActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    var selectedImageUri : Uri? = null

    companion object{
       var currentUser = Users("","","","")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater.inflate(R.menu.nav_menu,menu)
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
        // When = switch Case gibi
        when (item.itemId){
            R.id.menu_new_messages -> {
                // new Message Activity Intent
                val intent = Intent(applicationContext,
                    NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out ->{
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

     //   fetchCurrentUser()

    }


    private fun fetchCurrentUser(){
        val uid = mAuth.uid
        val ref = database.collection("Users")
        ref.addSnapshotListener { value, error ->
            if (error != null){
                error.printStackTrace()
            }
            if (value != null && !value.isEmpty){

            }
        }

    }
}

