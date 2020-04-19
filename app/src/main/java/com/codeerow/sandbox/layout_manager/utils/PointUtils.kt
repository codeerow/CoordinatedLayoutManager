package com.codeerow.sandbox.layout_manager.utils

import android.graphics.Point
import kotlin.math.atan


fun angle(pointA: Point, pointB: Point): Int {
    val angle = Math.toDegrees(atan((pointA.y - pointB.y).toDouble() / (pointA.x - pointB.x)))
    return if (angle > 0) 180 + angle.toInt() else 360 + angle.toInt()
}