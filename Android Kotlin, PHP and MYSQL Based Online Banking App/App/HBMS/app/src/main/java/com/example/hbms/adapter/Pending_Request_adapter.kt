package com.example.hbms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hbms.LocalDataProvider
import com.example.hbms.R
import com.example.hbms.View_Holder.PendingRequestViewHolder
import com.example.hbms.utilities.Request

class adapter2(private var mlist: List<Request>,var con:Context): RecyclerView.Adapter<PendingRequestViewHolder>() {
    lateinit var myaction:RespondAction
    interface RespondAction {
        fun action(position: Int)
    }

    fun setItemClickListner(listner:RespondAction){
        myaction = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingRequestViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.pending_request_item_view,parent,false)
        return PendingRequestViewHolder(view,myaction)
    }
    override fun onBindViewHolder(holder: PendingRequestViewHolder, position: Int) {
        var cur = mlist[position]
        if(LocalDataProvider(con).getData("email")==cur.requesterEmail){
                holder.P_Accept.isEnabled = false
        }
        holder.P_Requester_Email.text = "Requester Email: "+cur.requesterEmail .toString()
        holder.P_Request_Amount.text = "Request Amount: "+cur.requestAmount.toString()
        holder.P_Request_Message.text = "Request Message:  "+cur.requestMessage.toString()
        holder.P_Request_Date.text = "Date: "+cur.requestDate.toString()

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