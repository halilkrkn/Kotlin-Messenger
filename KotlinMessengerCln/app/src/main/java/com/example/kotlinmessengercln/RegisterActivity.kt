package com.example.kotlinmessengercln

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    var selectedImageUri : Uri? = null
  //  private lateinit var selectedImageUri : Uri
    private lateinit var database : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

    }

    // Kullanıcı KAydı İşlemleri
     fun register(view: View){
        val email = emailRegisterEdittext.text.toString()
        val password = passwordRegisterEdittext.text.toString()

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){

                val intent = Intent(applicationContext,LoginActivity::class.java)
                startActivity(intent)
                uploadProfilPhoto()  // Profil fotosunu Firebase de stroge(depoya) ve database ekleme
                finish()
            }
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }

    fun uploadProfilPhoto(){
        //UUID : image name

        val uuıd = UUID.randomUUID()
        val imageName = "$uuıd.jpg"

        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)

        if (selectedImageUri != null){
            imagesReference.putFile(selectedImageUri!!).addOnSuccessListener { taskSnapshot ->

                //Database - Firestore Kaydedilecek
                val uploadedPictureRefence = FirebaseStorage.getInstance()
                    .reference.child("images")
                    .child(imageName)

                uploadedPictureRefence.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
/*
                    /// Database Firestore Kısmı
                    val postMap = hashMapOf<String,Any>()
                    postMap.put("downloadUrl", downloadUrl)
                    postMap.put("userEmail", mAuth.currentUser!!.email.toString())
                    postMap.put("username", usernameRegisterEdittext.text.toString())
                    postMap.put("date",com.google.firebase.Timestamp.now())

                    database.collection("Posts").add(postMap).addOnCompleteListener { task ->
                        if (task.isComplete && task.isSuccessful){
                            //Back
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                    }

 */

                }

            }
        }
    }

    // Kullanıcı Profil Fotosu için izinler ve SDK sürümlerine göre kullanımı
    fun uploadPhotoRegister (view: View){

        //Profil Fotosunu almak için kullanıcıdan izinler istenecek.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataUri: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && dataUri != null ){

            selectedImageUri = dataUri.data
            try {
                if (selectedImageUri != null){

                    if (Build.VERSION.SDK_INT >=28){

                        val source = ImageDecoder.createSource(contentResolver, selectedImageUri!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        profileImageview.setImageBitmap(bitmap)

                    }else{
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImageUri)
                        profileImageview.setImageBitmap(bitmap)
                    }
                }
            } catch (exception: Exception){
                exception.printStackTrace()
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, dataUri)
    }



    // Eğer Kayıtlı ise Login ekranına yönlendirme
    fun alreadyHaveAccount(view: View){

        val intent = Intent(applicationContext,LoginActivity::class.java)
        startActivity(intent)
        finish()

    }
}