package com.codeerow.sandbox.layout_manager.utils

import kotlin.math.PI
import kotlin.math.round


fun Int.toRadians(): Double {
    return this * PI / 180
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}