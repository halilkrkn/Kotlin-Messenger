package com.example.kotlinmessengercln.adapter

import com.example.kotlinmessengercln.R
import com.example.kotlinmessengercln.model.Users
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.user_row_message.view.*

// kişileri seçme bölümü
class NewMessageGroupAdapter(val user: Users): Item<ViewHolder>(){

    // Burda NewMessage Activity için oluşturdupumuz recycler view row unu oluşturduğumuz layoutu tanımlayıp getirdik.
    override fun getLayout(): Int {
        return R.layout.user_row_message
    }

    // Model dosyasındaki Users data modeli içindeki username ve downloadUrl i usernametextViewNewMessage textView ında ve  usernameImageViewNewMessage imageView ında gösterdik
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.usernametextViewNewMessage.text = user.username
        Picasso.get().load(user.downloadUrl).into(viewHolder.itemView.usernameImageViewNewMessage)
    }
}
