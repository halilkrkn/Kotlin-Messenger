package com.example.kotlinmessengercln.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
@Parcelize
data class Users(

    val downloadUrl : String? = null,
    val useremail : String? = null,
    val username : String? = null,
    val userId : String? = null,
    val date: com.google.firebase.Timestamp? = null
):Parcelable{
    constructor(): this("","","","")
}