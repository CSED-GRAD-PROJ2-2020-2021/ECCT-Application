package com.desireProj.demo.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.database.ETLItem

class ETLDatabaseAdapter (private val etlList : List<ETLItem>) : RecyclerView.Adapter<ETLDatabaseAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val logDataTextView : TextView = itemView.findViewById(R.id.log_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.logger_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = etlList!![position]

        holder.logDataTextView.text = "PET:" + currentItem.pet + "\n" +
                "Day:" + currentItem.day + "\n" +
                "Duration:" + currentItem.duration + "\n" +
                "RSSI:" + currentItem.rssi
    }

    override fun getItemCount() = etlList!!.size
}