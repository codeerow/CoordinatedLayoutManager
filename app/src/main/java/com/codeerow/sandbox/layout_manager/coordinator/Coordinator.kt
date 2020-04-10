package com.codeerow.sandbox.layout_manager.coordinator

import android.graphics.Point


interface Coordinator {

    val startPosition: Point
    val lastPosition: Point

    fun calculatePosition(currentPosition: Point, delta: Int): Point?

    fun nextPosition(currentPosition: Point?): Point?
    fun prevPosition(currentPosition: Point): Point?

    fun isFirstPosition(point: Point?): Boolean
    fun isLastPosition(point: Point?): Boolean
}

