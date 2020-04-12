//package com.codeerow.sandbox.layout_manager.coordinator
//
//import android.graphics.Point
//import android.util.Log
//
//
//class PointByPointCoordinator(private val positions: List<Point>) : Coordinator {
//
//    override val initialPosition: Point = positions.first()
//    private val endPosition: Point = positions.last()
//
//    override fun calculatePosition(currentPosition: Point, delta: Double): Point {
//        var currentIndex = positions.indexOf(currentPosition)
//        Log.d("currentIndex", currentIndex.toString())
//        return if (delta < 0) {
//            if (currentIndex <= 0) null
//            else positions[--currentIndex]
//        } else {
//            if (currentIndex >= positions.size - 1) null
//            else positions[++currentIndex]
//        }
//    }
//
//    override fun nextPosition(currentPosition: Point?): Point? {
//        var currentIndex = positions.indexOf(currentPosition)
//        return if (currentIndex == -1 || currentIndex >= positions.size - 1) null
//        else positions[++currentIndex]
//    }
//
//    override fun prevPosition(currentPosition: Point): Point? {
//        var currentIndex = positions.indexOf(currentPosition)
//        return if (currentIndex <= 0) null
//        else positions[--currentIndex]
//    }
//
//    override fun isStartReached(point: Point?): Boolean = point == initialPosition
//
//    override fun isEndReached(point: Point?): Boolean = point == endPosition
//}