package com.example.kotlinmessengercln

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        curruntUser()
    }

    //Kullanıcı ilk girişini yaptıktan sonra otomatik girişi yapılacak.
    fun curruntUser(){
        // Eğer kayıtlı ve ilk girşini yaptıysa artık ondan sonra güncel olarak girişi yapılacak
        val currentUser = mAuth.currentUser
        if (currentUser != null){
            // Feed Activity gönderir
           //val intent = Intent(applicationContext,LatestMessagesActivity::class.java)
         //  startActivity(intent)
          //  finish()
        }
    }

    // Oluşturalan kullanıcı için Login işlemleri
    fun logIn(view: View){

        val email = emailLoginEdittext.text.toString()
        val password = passwordLoginEdittext.text.toString()

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                //Feed Activity Intent
                val intent = Intent(applicationContext,LatestMessagesActivity::class.java)
               startActivity(intent)
               finish()
            }
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    // Register ekranına geri dön
    fun backToRegister(view: View){
        val intent = Intent(applicationContext,RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}