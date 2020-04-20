package com.codeerow.sandbox

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
            layoutManager = TableLayoutManager(itemMargin = 167)
//            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CircleAdapter(List(1) { 1 })
        }
    }
}
