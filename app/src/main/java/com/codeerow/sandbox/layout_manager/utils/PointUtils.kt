package com.codeerow.sandbox.layout_manager.utils

import android.graphics.Point
import android.view.View
import kotlin.math.atan


fun angle(pointA: Point, pointB: Point): Int {
    val angle = Math.toDegrees(atan((pointA.y - pointB.y).toDouble() / (pointA.x - pointB.x)))
    return if (angle > 0) 180 + angle.toInt() else 360 + angle.toInt()
}

fun Point.isOutOfParent(childView: View): Boolean = with(childView) {
    val parentView = parent as? View ?: return false
    return when {
        x < 0 -> true
        y < 0 -> true
        x > parentView.width -> true
        y > parentView.height -> true
        else -> false
    }
}