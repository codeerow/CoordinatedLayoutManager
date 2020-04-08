package com.codeerow.sandbox.layout_manager.coordinator

import android.graphics.Point
import android.util.Log


class PointByPointCoordinator(private val positions: List<Point>) : Coordinator {

    override val startPosition: Point = positions.first()
    override val lastPosition: Point = positions.last()

    override fun nextPosition(currentPosition: Point, delta: Int): Point? {
        var currentIndex = positions.indexOf(currentPosition)
        Log.d("currentIndex", currentIndex.toString())
        return if (delta < 0) {
            if (currentIndex <= 0) null
            else positions[--currentIndex]
        } else {
            if (currentIndex >= positions.size - 1) null
            else positions[++currentIndex]
        }
    }

    override fun nextPosition(currentPosition: Point?): Point? {
        var currentIndex = positions.indexOf(currentPosition)
        return if (currentIndex == -1 || currentIndex >= positions.size - 1) null
        else positions[++currentIndex]
    }

    override fun positionFor(index: Int): Point? {
        return positions.getOrNull(index)
    }

    override fun prevPosition(currentPosition: Point): Point? {
        var currentIndex = positions.indexOf(currentPosition)
        return if (currentIndex <= 0) null
        else positions[--currentIndex]
    }

    override fun isFirstPosition(point: Point?): Boolean = point == startPosition

    override fun isLastPosition(point: Point?): Boolean = point == lastPosition
}