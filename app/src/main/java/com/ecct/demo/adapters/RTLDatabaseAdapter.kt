/**
 * Author: Mohamed Samy
 */
package com.ecct.demo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecct.protocol.R
import com.ecct.protocol.database.RTLItem

class RTLDatabaseAdapter(private val rtlList : List<RTLItem>) : RecyclerView.Adapter<RTLDatabaseAdapter.ViewHolder>() {

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val logDataTextView : TextView = itemView.findViewById(R.id.log_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.logger_item,parent,false)

        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = rtlList[position]

        holder.logDataTextView.text = "PET: " + currentItem.pet + "\n" +
                "Day: " + currentItem.day
    }

    override fun getItemCount() = rtlList.size
}