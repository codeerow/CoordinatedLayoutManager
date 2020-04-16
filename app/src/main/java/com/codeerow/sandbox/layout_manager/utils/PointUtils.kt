package com.codeerow.sandbox.layout_manager.utils

import android.graphics.Point
import kotlin.math.atan


fun Point.angle(centerPoint: Point): Int {
    val angle = Math.toDegrees(atan((y - centerPoint.y).toDouble() / (x - centerPoint.x)))
    return if (angle > 0) 180 + angle.toInt() else 360 + angle.toInt()
}