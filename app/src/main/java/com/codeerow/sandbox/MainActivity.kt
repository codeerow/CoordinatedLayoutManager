package com.codeerow.sandbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codeerow.sandbox.circle_list.CircleAdapter
import com.codeerow.sandbox.layout_manager.TableLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.apply {
            layoutManager = TableLayoutManager(itemMargin = 300)
//            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CircleAdapter(List(25) { 1 })
        }
    }
}
