package com.codeerow.sandbox.layout_manager.coordinator

import android.graphics.Point


interface Coordinator {

    val initialPosition: Point

    fun Point.shiftPosition(delta: Int): Point

    fun Point.isBoundsReached(delta: Int): Boolean
}

