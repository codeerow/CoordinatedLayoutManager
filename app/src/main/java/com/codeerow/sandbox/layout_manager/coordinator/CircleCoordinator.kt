//package com.codeerow.sandbox.layout_manager.coordinator
//
//import android.graphics.Point
//import android.util.Log
//import kotlin.math.*
//
//
//class CircleCoordinator(
//    private val initialDelta: Double,
//    private val endDelta: Double,
//    private val centerPosition: Point,
//    private val radius: Double
//) : Coordinator {
//
//    /**
//     * This is starting point for our coordinator.
//     * For default is bottom of the circle.
//     * */
//    private val startingPoint = calculatePoint(0.0)
//
//
//    /**
//     * Init position for ours items.
//     * */
//    override val initialPosition: Point = shiftPosition(startingPoint, initialDelta)
//
//
//    /**
//     * Parametric circle function presentation:
//     * x = a + r cos(t)
//     * y = b + r sin(t), where a - center x;
//     *                         b - center y;
//     *                         r - radius;
//     *                         t - parameter;
//     *
//     *
//     *  У нас есть текущая позиция, из текущей позиции вычисляем чему равен
//     *  текущий T (начало для интеграла).
//     *
//     *  delta - результат интегрирования функции от начального T до искомого T.
//     *  После нахождения искомого T, подставим его в уравнения выше для нахождения
//     *  новых x, y.
//     * */
//    override fun shiftPosition(currentPosition: Point, delta: Double): Point {
//        val currentDelta = calculateDelta(currentPosition)
//        Log.d("TEST", "$currentDelta")
//        /**
//         * 1) delta = integral(T1, T2) ( sqrt( (x'^2 + y'^2)^(1/2) ) ) dt
//         * x't = -r*sin(t)
//         * y't = r*cos(t)
//         * 2) delta = integral(T1, T2) ( sqrt( r^2 * sin(t)^2 + r^2 * cos(t)^2 ) )
//         * 3) integral = r * t
//         * 4) delta = r * (T2 - T1)
//         * 5) T2 = (delta / r) + T1
//         * */
//        val newDelta = (delta / radius) + currentDelta
//        return calculatePoint(newDelta)
//    }
//
//    /**
//     * Вычисление зависит от того в какой четверти окружности мы находимся.
//     * */
//    private fun calculateDelta(position: Point): Double {
//        /**
//         * y = b + r sin(t)
//         * t = asin((y-b) / r)
//         * */
//        return if (position.y > centerPosition.y) acos((position.x - centerPosition.x) / radius)
//        else 2 * PI - acos((position.x - centerPosition.x) / radius)
//    }
//
//    private fun calculatePoint(delta: Double) : Point {
//        val newX = centerPosition.x + radius * cos(delta)
//        val newY = centerPosition.y + radius * sin(delta)
//        return Point(newX.toInt(), newY.toInt())
//    }
//
//
//    /**
//     * Check bounds.
//     * */
//    override fun isBoundsReached(point: Point, delta: Double): Boolean {
//        return if (delta > 0) isStartReached(point)
//        else isEndReached(point)
//    }
//
//    private fun isStartReached(point: Point): Boolean {
//        val currentDelta = calculateDelta(point)
//        return abs(0.0 - currentDelta) < abs(0.0 - initialDelta)
//    }
//
//    private fun isEndReached(point: Point): Boolean {
////        val currentDelta = calculateDelta(point)
////        val startingDelta = calculateDelta(startingPoint)
////        return abs(startingDelta - endDelta) > abs(startingDelta - currentDelta)
//        return false
//    }
//}