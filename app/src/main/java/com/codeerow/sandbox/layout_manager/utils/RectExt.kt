package com.codeerow.sandbox.layout_manager.utils

import android.graphics.Rect


fun Rect.perimeter(): Int {
    return (right - left) * 2 + (bottom - top) * 2
}