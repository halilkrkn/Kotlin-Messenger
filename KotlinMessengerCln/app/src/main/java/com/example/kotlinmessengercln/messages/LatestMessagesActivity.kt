package com.example.kotlinmessengercln.messages

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.kotlinmessengercln.registerLogin.LoginActivity
import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.model.Users
import com.example.kotlinmessengercln.registerLogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class LatestMessagesActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    var selectedImageUri : Uri? = null

    companion object{
       var currentUser = Users()
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

        fetchCurrentUser()
        //currentUser()
        verifyUserIsLoggedIn()

    }
    //Kullanıcı ilk girişini yaptıktan sonra otomatik girişi yapılacak.
    fun currentUser(){
        // Eğer kayıtlı ve ilk girşini yaptıysa artık ondan sonra güncel olarak girişi yapılacak
        val currentUser = mAuth.currentUser
        if (currentUser != null){
            // Feed Activity gönderir
            val intent = Intent(applicationContext,LatestMessagesActivity::class.java)
            startActivity(intent)
             finish()
        }
    }

    private fun verifyUserIsLoggedIn(){
        val uid = mAuth.uid
        if (uid == null){
            val intent = Intent(applicationContext,RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    private fun fetchCurrentUser(){
        //val userId = database.collection("Users").document().id
        val ref = database.collection("Users")
        ref.addSnapshotListener { value, error ->
            if (error != null){
                error.printStackTrace()
            }
            if (value != null){
                val documents = value.documents
                for (document in documents){
                    val getImageUrl = document.get("downloadUrl") as String

                    currentUser = Users(getImageUrl)
                   // Log.d("LatestMessages","Current User : ${currentUser.downloadUrl}")


                }

            }
        }

    }
}

