package com.example.hbms.View_Holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hbms.R

class RespondedRequestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var Requester_Email = itemView.findViewById<TextView?>(R.id.Requester_Email);
    var Request_Amount = itemView.findViewById<TextView>(R.id.Request_Amount);
    var Request_Message = itemView.findViewById<TextView>(R.id.Request_Message);
    var Request_Date = itemView.findViewById<TextView>(R.id.Request_Date);
    var Responded_Amount = itemView.findViewById<TextView>(R.id.Responded_Amount);
    var Responder_Message = itemView.findViewById<TextView>(R.id.Responder_Message);
    var Responded_Date = itemView.findViewById<TextView>(R.id.Responded_Date);
}