package com.example.hbms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hbms.R
import com.example.hbms.View_Holder.MyViewHolder
import com.example.hbms.utilities.Transaction

class adapter1(private var mlist: List<Transaction>): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var cur = mlist[position]
        holder.t_id.text = "Transaction Id: "+cur.transactionId.toString()
        holder.sender_email.text = "Sender Email: "+cur.sender
        holder.reciver_email.text = "Receiver Email: "+cur.receiver
        holder.Transaction_Amount.text = "Transaction Amount: "+cur.transactionAmount.toString()
        holder.Transaction_Purpose.text = "Transaction Purpose: "+cur.transactionPurpose
        holder.Transaction_Date.text = "Transaction Date: "+cur.transactionDate
    }
    override fun getItemCount(): Int {//size of array
        return mlist.size
    }
    fun setlist(llist:List<Transaction>){
        mlist = llist
    }
    fun getlist(Pos:Int): Transaction {
        return mlist[Pos]
    }
}