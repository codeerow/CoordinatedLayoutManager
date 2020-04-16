package com.codeerow.sandbox

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codeerow.sandbox.circle_list.CircleAdapter
import com.codeerow.sandbox.layout_manager.CoordinatedLayoutManager
import com.codeerow.sandbox.layout_manager.coordinator.TableCoordinator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.PI


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.apply {
            //            val pointByPointCoordinator = PointByPointCoordinator(
//                listOf(
//                    Point(50, 50),
//                    Point(150, 100),
//                    Point(250, 150),
//                    Point(350, 200),
//                    Point(450, 200),
//                    Point(550, 150),
//                    Point(650, 100),
//                    Point(750, 50),
//                    Point(750, 150),
//                    Point(750, 250),
//                    Point(750, 350),
//                    Point(750, 450),
//                    Point(750, 550)
//                )
//            )
            val height = 1920
            val circleCoordinator = TableCoordinator(
                width = 700,
                height = 1000,
                initialPosition = Point(200, height)
            )
            layoutManager = CoordinatedLayoutManager(circleCoordinator, itemMargin = 167)
            adapter = CircleAdapter(List(1) { 1 })
            setOnTouchListener { view, motionEvent ->
                false
            }
        }
    }
}
