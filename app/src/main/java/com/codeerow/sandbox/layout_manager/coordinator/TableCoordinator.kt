package com.codeerow.sandbox.layout_manager.coordinator

import android.graphics.Point
import com.codeerow.sandbox.layout_manager.utils.angle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


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
    override val startPosition: Point
) : Coordinator {

    override val endPosition = Point(startPosition).apply { x -= width }


    private val radius: Int = width / 2


    private val rightSideToCirclePoint = Point(startPosition).apply {
        y = y - height + radius
    }

    private val leftSideToCirclePoint = Point(rightSideToCirclePoint).apply {
        x -= width
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

    private fun isOnLeftSide(pathLength: Double): Boolean = pathLength <= pathLengthTillArc
    private fun isOnRightSide(pathLength: Double): Boolean = pathLength >= pathLengthTillRightSide

    private fun Point.isOnLeftSide(): Boolean = this.x == leftSideToCirclePoint.x
    private fun Point.isOnRightSide(): Boolean = this.x == rightSideToCirclePoint.x


    override fun Point.shiftPosition(delta: Int): Point {
        val currentPathLength = calculateCurrentPath() - delta
        return when {
            isOnLeftSide(currentPathLength) -> shiftAlongLeftSide(currentPathLength.toInt())
            isOnRightSide(currentPathLength) -> shiftAlongRightSide(currentPathLength.toInt() - oneSidePathLength - arcPathLength)
            else -> shiftAlongCircle(currentPathLength - oneSidePathLength + arcPathLength)
        }
    }

    private fun shiftAlongLeftSide(delta: Int) = Point(startPosition).apply {
        y -= delta
        x -= width
    }

    private fun shiftAlongRightSide(delta: Int) = Point(rightSideToCirclePoint).apply {
        y += delta
    }

    private fun shiftAlongCircle(delta: Double): Point {
        val newDelta = (delta / radius)

        val newX = circleCenterPoint.x + radius * cos(newDelta)
        val newY = circleCenterPoint.y + radius * sin(newDelta)
        return Point(newX.toInt(), newY.toInt())
    }


    private fun Point.calculateCurrentPath(): Double {
        return when {
            // we on the left side our path consist of initial y - current y
            isOnLeftSide() -> startPosition.y.toDouble() - y
            // we on the right side our path consist of
            // one side + arc path + initial y - current y (because initial y = end y)
            isOnRightSide() -> oneSidePathLength.toDouble() + arcPathLength + (oneSidePathLength - (startPosition.y - y))
            // we on the arc, out path is left side length + arc for curr point
            else -> oneSidePathLength + calculateArcLength(angle(this, circleCenterPoint)) -
                    arcPathLength
        }
    }


    fun calculateRotation(point: Point): Double = with(point) {
        return when {
            isOnLeftSide() -> -90.0
            isOnRightSide() -> 90.0
            else -> angle(this, circleCenterPoint) - 270.0
        }
    }


    /**
     * L= πrα / 180°, where a - angle in degrees
     * */
    private fun calculateArcLength(angle: Double) = (PI * radius * (angle / 180))


    /** Bounds is never reached because item disappears only when reach screen bounds */
    override fun Point.isBoundsReached(delta: Int): Boolean {
        return false
    }
}