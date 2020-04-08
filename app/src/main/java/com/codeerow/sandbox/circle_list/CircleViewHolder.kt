package com.codeerow.sandbox.circle_list

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_list_item.view.*

class CircleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(position: Int) = with(view) {
        view.setOnClickListener {
            Toast.makeText(view.context, position.toString(), Toast.LENGTH_SHORT).show()
        }
        view.itemText.text = position.toString()
    }
}