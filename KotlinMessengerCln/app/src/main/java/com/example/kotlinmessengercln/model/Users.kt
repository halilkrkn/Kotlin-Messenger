package com.example.kotlinmessengercln.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

//Firestore Kullanıcı(Users) veritabanı modeli
@Parcelize
data class Users(

    val downloadUrl: String? = null,
    val useremail: String? = null,
    val username: String? = null,
    val userId: String? = null,
    val date: Timestamp? = null
):Parcelable