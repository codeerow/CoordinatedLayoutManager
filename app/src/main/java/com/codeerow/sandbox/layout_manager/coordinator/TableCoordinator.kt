package com.codeerow.sandbox.layout_manager.coordinator

import android.graphics.Point
import android.util.Log
import com.codeerow.sandbox.layout_manager.utils.angle
import kotlin.math.*


/**
 *  This is coordinator for such view:
 *
 *          ***
 *        **   **
 *        **   **
 *        **   **
 *        i     e
 *
 * The movement is directed from i (initial point) to e (end point).
 * */
class TableCoordinator(
    private val width: Int,
    private val height: Int,
    override val initialPosition: Point
) : Coordinator {

    private val radius: Int = width / 2


    private val leftSideToCirclePoint = Point(initialPosition).apply {
        y = y - height + radius
    }

    private val rightSideToCirclePoint = Point(leftSideToCirclePoint).apply {
        x += width
    }

    private val circleCenterPoint = Point(rightSideToCirclePoint).apply {
        x -= width / 2
    }


    private val oneSidePathLength = height - radius
    private val arcPathLength = (PI * radius).toInt()

    /**
     * Represent length of the movement trajectory for each of the sides.
     *
     * (circle arc length) C = 2* PI * r, where R - radius
     * We need half of it: C/2 = PI * R
     */
    private val pathLengthTillArc = oneSidePathLength
    private val pathLengthTillRightSide = oneSidePathLength + arcPathLength

    private fun isOnLeftSide(pathLength: Int): Boolean = pathLength <= pathLengthTillArc
    private fun isOnRightSide(pathLength: Int): Boolean = pathLength >= pathLengthTillRightSide


    override fun Point.shiftPosition(delta: Int): Point {
        val currentPathLength = calculateCurrentPath(delta)
        Log.d("TEST1", "currentPathLength: $currentPathLength")
        return when {
            isOnLeftSide(currentPathLength) -> shiftAlongLeftSide(currentPathLength)
            isOnRightSide(currentPathLength) -> {
                shiftAlongRightSide(currentPathLength - oneSidePathLength - arcPathLength)
            }
            else -> shiftAlongCircle(currentPathLength - oneSidePathLength + arcPathLength)
        }
    }

    private fun shiftAlongLeftSide(delta: Int) = Point(initialPosition).apply {
        y -= delta
    }

    private fun shiftAlongRightSide(delta: Int) = Point(rightSideToCirclePoint).apply {
        Log.d("TEST1", "delta: $delta")
        y += delta
    }

    private fun shiftAlongCircle(delta: Int): Point {
        val newDelta = (delta.toDouble() / radius)

        val newX = circleCenterPoint.x + radius * cos(newDelta)
        val newY = circleCenterPoint.y + radius * sin(newDelta)
        return Point(newX.toInt(), newY.toInt())
    }


    private fun Point.calculateCurrentPath(delta: Int): Int {
        val currentPointPath = when (this.x) {
            // we on the left side our path consist of initial y - current y
            leftSideToCirclePoint.x -> initialPosition.y - y
            // we on the right side our path consist of
            // one side + arc path + initial y - current y (because initial y = end y)
            rightSideToCirclePoint.x -> oneSidePathLength + arcPathLength + (oneSidePathLength - (initialPosition.y - y))
            // we on the arc, out path is left side length + arc for curr point
            else -> oneSidePathLength + calculateArcLength(this.angle(centerPoint = circleCenterPoint)) - arcPathLength
        }
        return currentPointPath - delta
    }


    /**
     * L= πrα / 180°, where a - angle in degrees
     * */
    private fun calculateArcLength(angle: Int) = (PI * radius * (angle / 180.0)).toInt()


    override fun Point.isBoundsReached(delta: Int): Boolean {
        // TODO: implement
        return false
    }

}