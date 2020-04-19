package com.codeerow.sandbox.layout_manager

import android.graphics.Point
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.codeerow.sandbox.layout_manager.coordinator.Coordinator
import com.codeerow.sandbox.layout_manager.utils.moveTo
import com.codeerow.sandbox.layout_manager.utils.positionInParent


open class CoordinatedLayoutManager(
    private val coordinator: Coordinator,
    private val itemMargin: Int
) : RecyclerView.LayoutManager() {

    private var firstVisiblePosition: Int = 0
    private var lastVisiblePosition: Int = 0


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }


    /** Layout */
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        removeAllViews()
        if (itemCount == 0) return

        // TODO: These values should not be set to "0". They should be restored from state
        // TODO: These values should not be set to "0". They should be restored from state

        lastVisiblePosition = 0
        firstVisiblePosition = 0

        with(coordinator) {
            var currentPoint: Point = initialPosition
            do {
                val view = recycler.getViewForPosition(lastVisiblePosition)
                addView(view)
                layoutView(view, currentPoint)
                currentPoint = currentPoint.shiftPosition(itemMargin)
                lastVisiblePosition++
            } while (!currentPoint.isBoundsReached(itemMargin)
                && lastVisiblePosition < itemCount
            )
        }
    }

    open fun layoutView(view: View, currentPoint: Point) {
        measureChildWithMargins(view, 0, 0)

        val measuredWidth = getDecoratedMeasuredWidth(view)
        val measuredHeight = getDecoratedMeasuredHeight(view)

        val left = currentPoint.x - measuredWidth / 2
        val right = currentPoint.x + measuredWidth / 2
        val top = currentPoint.y - measuredHeight / 2
        val bottom = currentPoint.y + measuredHeight / 2

        layoutDecorated(view, left, top, right, bottom)
    }


    /** Scroller */
    override fun canScrollVertically() = true

    override fun canScrollHorizontally() = false

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        return if (childCount == 0) { // we cannot scroll if we don't have views
            0
        } else scrollBy(dy, recycler)
    }

    private fun scrollBy(delta: Int, recycler: Recycler): Int = with(coordinator) {
        for (i in 0 until childCount) {
            getChildAt(i)?.let { view ->
                val viewPosition = view.positionInParent()
                if (!viewPosition.isBoundsReached(delta)) {
                    val newPosition = viewPosition.shiftPosition(delta)
                    layoutView(view, newPosition)
                } else {
                    if (firstVisiblePosition != 0) {
                        removeView(view)
                        recycler.recycleView(view)
                    }
//                    if (delta < 0) {
//                        ++firstVisiblePosition
//                    } else {
//                        --lastVisiblePosition
//                    }
                }
            }
        }
        return delta
    }

}