package com.codeerow.sandbox.circle_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeerow.sandbox.R

class CircleAdapter(private val items: List<Int>) : RecyclerView.Adapter<CircleViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_list_item,
            parent,
            false
        )
        return CircleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CircleViewHolder, position: Int) {
        holder.bind(position)
    }
}