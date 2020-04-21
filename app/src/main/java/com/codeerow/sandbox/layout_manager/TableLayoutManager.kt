package com.codeerow.sandbox.layout_manager

import android.graphics.Point
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.codeerow.sandbox.layout_manager.coordinator.TableCoordinator


class TableLayoutManager(
    itemMargin: Int
) : CoordinatedLayoutManager(itemMargin) {

    override lateinit var coordinator: TableCoordinator

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val view = recycler.getViewForPosition(0)
        val (itemWidth, itemHeight) = view.measure()
        coordinator = TableCoordinator(
            width = width - itemWidth,
            height = height - itemHeight,
            startPosition = Point(width - itemWidth / 2, height)
        )
        super.onLayoutChildren(recycler, state)
    }

    override fun layoutView(view: View, currentPoint: Point) = with(coordinator) {
        super.layoutView(view, currentPoint)
        view.rotation = calculateRotation(currentPoint)
    }

    override fun canScroll(): Boolean {
//        val firstView = getChildAt(0) ?: return false
//        val lastView = getChildAt(childCount - 1) ?: return false
//        if(firstView.bottom > height) return false
        return super.canScroll()
    }
}