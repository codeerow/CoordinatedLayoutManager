package com.codeerow.sandbox.layout_manager.coordinator

import android.graphics.Point


interface Coordinator {

    val initialPosition: Point
    
    fun shiftPosition(currentPosition: Point, delta: Double): Point

    fun isBoundsReached(point: Point, delta: Double): Boolean
}

