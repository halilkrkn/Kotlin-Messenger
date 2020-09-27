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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

class LatestMessagesActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    companion object{
       var currentUser : Users? = null

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
        // When = switch Case  veya if-else gibi
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

            val intent = Intent(applicationContext,LatestMessagesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
// Kullanıcı doğrulaması yapıldı.
    private fun verifyUserIsLoggedIn(){
        val uid = mAuth.uid
        if (uid == null){
            val intent = Intent(applicationContext,RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    // ChatLog Activityde Mesajlaşma esnasında Kullanıcın profil fotosunun gösterilmesi için
    private fun fetchCurrentUser(){
        val userId = FirebaseAuth.getInstance().uid ?: ""


            Log.d("LatestMessage", "girdi")

        val docRef = database.collection("Users").document(userId).get().addOnSuccessListener {

            Log.d("LatestMessage", "onSucces " + it.id )

            currentUser = it.toObject(Users::class.java)
            Log.d("LatestMessages","Current User : ${currentUser?.downloadUrl}")


        }
    }
}


