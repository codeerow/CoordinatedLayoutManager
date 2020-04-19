package com.codeerow.sandbox

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codeerow.sandbox.circle_list.CircleAdapter
import com.codeerow.sandbox.layout_manager.CoordinatedLayoutManager
import com.codeerow.sandbox.layout_manager.TableLayoutManager
import com.codeerow.sandbox.layout_manager.coordinator.TableCoordinator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.PI


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.apply {
            val height = 1920
            val circleCoordinator = TableCoordinator(
                width = 700,
                height = 1000,
                initialPosition = Point(200, height)
            )
            layoutManager = TableLayoutManager(circleCoordinator, itemMargin = 167)
            adapter = CircleAdapter(List(1) { 1 })
        }
    }
}
