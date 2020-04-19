package com.codeerow.sandbox.layout_manager

import android.graphics.Point
import android.view.View
import com.codeerow.sandbox.layout_manager.coordinator.TableCoordinator


class TableLayoutManager(
    private val coordinator: TableCoordinator,
    itemMargin: Int
) : CoordinatedLayoutManager(coordinator, itemMargin) {


    override fun layoutView(view: View, currentPoint: Point) = with(coordinator) {
        super.layoutView(view, currentPoint)
        view.rotation = calculateRotation(currentPoint)
    }
}