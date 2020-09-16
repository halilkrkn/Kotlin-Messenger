package com.example.kotlinmessengercln

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_message.view.*

class NewMessageGroupAdapter(private val usernameArray: String, private val profileImageUrl: String): Item<ViewHolder>(){
    constructor() : this("","")

    override fun getLayout(): Int {
        return R.layout.user_row_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.usernametextViewNewMessage.text = usernameArray
        Picasso.get().load(profileImageUrl).into(viewHolder.itemView.usernameImageViewNewMessage)
    }


}
/*
class NewMessageGroupAdapter(private val usernameArray: ArrayList<String>, private val profileImageUrl: ArrayList<String>): Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.user_row_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.usernametextViewNewMessage.text = usernameArray[position]
        Picasso.get().load(profileImageUrl[position]).into(viewHolder.itemView.usernameImageViewNewMessage)
    }
}


 */