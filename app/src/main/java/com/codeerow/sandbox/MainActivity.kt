package com.codeerow.sandbox

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeerow.sandbox.circle_list.CircleAdapter
import com.codeerow.sandbox.layout_manager.CoordinatedLayoutManager
import com.codeerow.sandbox.layout_manager.coordinator.PointByPointCoordinator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_list_item.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.apply {
            val coordinator = PointByPointCoordinator(
                listOf(
                    Point(50, 50),
                    Point(150, 100),
                    Point(250, 150),
                    Point(350, 200),
                    Point(450, 200),
                    Point(550, 150),
                    Point(650, 100),
                    Point(750, 50),
                    Point(750, 150),
                    Point(750, 250),
                    Point(750, 350),
                    Point(750, 450),
                    Point(750, 550)
                )
            )
            layoutManager = CoordinatedLayoutManager(coordinator)
            adapter = CircleAdapter(List(100) { 1 })
        }
    }
}
