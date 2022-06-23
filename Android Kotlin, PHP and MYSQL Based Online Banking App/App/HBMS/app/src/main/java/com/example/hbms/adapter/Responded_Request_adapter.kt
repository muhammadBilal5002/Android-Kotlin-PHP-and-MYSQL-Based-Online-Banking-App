package com.example.hbms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hbms.R
import com.example.hbms.View_Holder.PendingRequestViewHolder
import com.example.hbms.View_Holder.RespondedRequestViewHolder
import com.example.hbms.utilities.Request

class adapter3(private var mlist: List<Request>): RecyclerView.Adapter<RespondedRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RespondedRequestViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.responded_request_item_view,parent,false)
        return RespondedRequestViewHolder(view)
    }
    override fun onBindViewHolder(holder: RespondedRequestViewHolder, position: Int) {
        var cur = mlist[position]
        holder.Requester_Email.text = "Requester Email: "+cur.requesterEmail .toString()
        holder.Request_Amount.text = "Request Amount: "+cur.requestAmount.toString()
        holder.Request_Message.text = "Request Message:  "+cur.requestMessage.toString()
        holder.Request_Date.text = "Date: "+cur.requestDate.toString()
        holder.Responded_Amount.text = "Responded Amount: "+cur.respondAmount.toString()
        holder.Responder_Message.text = "Responder Message: " +cur.respondMessage
        holder.Responded_Date.text = "Responded Date: "+cur.requestDate.toString()

    }
    override fun getItemCount(): Int {//size of array
        return mlist.size
    }
    fun setlist(llist:List<Request>){
        mlist = llist
    }
    fun getlist(Pos:Int): Request {
        return mlist[Pos]
    }
}