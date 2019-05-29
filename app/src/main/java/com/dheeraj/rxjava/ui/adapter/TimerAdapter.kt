package com.dheeraj.rxjava.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dheeraj.rxjava.R
import com.dheeraj.rxjava.ui.viewholder.TimerViewHolder

class TimerAdapter(private val list: ArrayList<Int>) : RecyclerView.Adapter<TimerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timer,parent,false)
        return TimerViewHolder(view,parent.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(list[position])
    }

}