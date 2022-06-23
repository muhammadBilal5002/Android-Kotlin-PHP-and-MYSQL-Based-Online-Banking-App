package com.example.hbms.View_Holder

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hbms.LocalDataProvider
import com.example.hbms.R
import com.example.hbms.adapter.adapter2

class PendingRequestViewHolder(itemView: View,myaction: adapter2.RespondAction): RecyclerView.ViewHolder(itemView){
    var P_Requester_Email = itemView.findViewById<TextView?>(R.id.P_Requester_Email);
    var P_Request_Amount = itemView.findViewById<TextView>(R.id.P_Request_Amount);
    var P_Request_Message = itemView.findViewById<TextView>(R.id.P_Request_Message);
    var P_Request_Date = itemView.findViewById<TextView>(R.id.P_Request_Date);
    var P_Accept = itemView.findViewById<Button>(R.id.P_Accept);

    init {
        P_Accept.setOnClickListener {
            myaction.action(adapterPosition)
        }
    }
}