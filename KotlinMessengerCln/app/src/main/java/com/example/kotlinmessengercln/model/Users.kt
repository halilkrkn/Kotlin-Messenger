package com.example.kotlinmessengercln.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Users(private val usernameArray: String, private val profileImageUrl: String):Parcelable{
    constructor() : this("","")
}