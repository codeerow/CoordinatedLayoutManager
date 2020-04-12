package com.codeerow.sandbox.layout_manager.coordinator

import android.graphics.Point
import kotlin.math.*


class CircleCoordinator(
    private val startDelta: Double,
    private val endDelta: Double,
    private val centerPosition: Point,
    private val radius: Double
) : Coordinator {

    /**
     * This is starting point for our coordinator.
     * For default is bottom of the circle.
     * */
    private val startingPoint = Point(centerPosition.x, centerPosition.y + radius.toInt())


    /**
     * Init position for ours items.
     * */
    override val initialPosition: Point = shiftPosition(startingPoint, startDelta)


    /**
     * Parametric circle function presentation:
     * x = a + r cos(t)
     * y = b + r sin(t), where a - center x;
     *                         b - center y;
     *                         r - radius;
     *                         t - parameter;
     *
     *
     *  У нас есть текущая позиция, из текущей позиции вычисляем чему равен
     *  текущий T (начало для интеграла).
     *
     *  delta - результат интегрирования функции от начального T до искомого T.
     *  После нахождения искомого T, подставим его в уравнения выше для нахождения
     *  новых x, y.
     * */
    override fun shiftPosition(currentPosition: Point, delta: Double): Point {
        /**
         * x = a + r cos(t)
         * t = acos((x-a) / r)
         * */
        val currentDelta = calculateDelta(currentPosition)

        /**
         * 1) delta = integral(T1, T2) ( sqrt( (x'^2 + y'^2)^(1/2) ) ) dt
         * x't = -r*sin(t)
         * y't = r*cos(t)
         * 2) delta = integral(T1, T2) ( sqrt( r^2 * sin(t)^2 + r^2 * cos(t)^2 ) )
         * 3) integral = r * t
         * 4) delta = r * (T2 - T1)
         * 5) T2 = (delta / r) + T1
         * */

        val newDelta = (delta / radius) + currentDelta
        val newX = centerPosition.x + radius * cos(newDelta)
        val newY = centerPosition.y + radius * sin(newDelta)
        return Point(newX.toInt(), newY.toInt())
    }

    /**
     * Вычисление зависит от того в какой четверти окружности мы находимся.
     * */
    private fun calculateDelta(position: Point): Double {
        return if (position.x > centerPosition.x) asin((position.y - centerPosition.y) / radius)
        else PI - asin((position.y - centerPosition.y) / radius)
    }


    /**
     * Check bounds.
     * */
    override fun isBoundsReached(point: Point, delta: Double): Boolean {
        return /*if (delta > 0) isStartReached(point)
        else isEndReached(point)*/false
    }

    private fun isStartReached(point: Point): Boolean {
        val currentDelta = calculateDelta(point)
        val initialDelta = calculateDelta(initialPosition)
        return currentDelta == initialDelta + startDelta
    }

    private fun isEndReached(point: Point): Boolean {
        val currentDelta = calculateDelta(point)
        return endDelta == currentDelta
    }
}