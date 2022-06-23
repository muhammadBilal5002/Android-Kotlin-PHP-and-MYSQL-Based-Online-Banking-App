package com.example.hbms.View_Holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hbms.R


class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var t_id = itemView.findViewById<TextView?>(R.id.t_id);
    var sender_email = itemView.findViewById<TextView>(R.id.sender_email);
    var reciver_email = itemView.findViewById<TextView>(R.id.reciver_email);
    var Transaction_Amount = itemView.findViewById<TextView>(R.id.Transaction_Amount);
    var Transaction_Purpose = itemView.findViewById<TextView>(R.id.Transaction_Purpose);
    var Transaction_Date = itemView.findViewById<TextView>(R.id.Transaction_Date);



}