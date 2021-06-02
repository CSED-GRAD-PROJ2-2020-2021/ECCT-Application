package com.desireProj.demo.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.LoggerData

class LoggerAdapter(private val logDataList: MutableList<LoggerData>?) : RecyclerView.Adapter<LoggerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val logDataTextView : TextView = itemView.findViewById(R.id.log_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.logger_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = logDataList!![position]

        holder.logDataTextView.text = currentItem.logData
    }

    override fun getItemCount() = logDataList!!.size
}