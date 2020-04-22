package com.codeerow.sandbox.layout_manager

import android.graphics.Point
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.codeerow.sandbox.layout_manager.coordinator.Coordinator
import com.codeerow.sandbox.layout_manager.utils.isOutOfParent
import com.codeerow.sandbox.layout_manager.utils.positionInParent


abstract class CoordinatedLayoutManager(
    private val itemMargin: Int
) : RecyclerView.LayoutManager() {

    abstract val coordinator: Coordinator

    private var firstVisiblePosition: Int = 0
        set(value) {
            field = value
        }
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
            var currentPoint: Point = startPosition
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

    protected fun View.measure(): Pair<Int, Int> {
        measureChildWithMargins(this, 0, 0)

        val measuredWidth = getDecoratedMeasuredWidth(this)
        val measuredHeight = getDecoratedMeasuredHeight(this)
        return measuredWidth to measuredHeight
    }

    open fun layoutView(view: View, currentPoint: Point) {
        val (measuredWidth, measuredHeight) = view.measure()
        val left = currentPoint.x - measuredWidth / 2
        val right = currentPoint.x + measuredWidth / 2
        val top = currentPoint.y - measuredHeight / 2
        val bottom = currentPoint.y + measuredHeight / 2

        layoutDecorated(view, left, top, right, bottom)
    }


    /** Scrolling */
    override fun canScrollVertically() = true

    override fun canScrollHorizontally() = false

    open fun canScroll(): Boolean {
        return childCount != 0
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        // TODO: adjust scroll delta
        return if (canScroll()) scrollEachItem(dy, recycler)
        else 0
    }

    private fun scrollEachItem(delta: Int, recycler: Recycler): Int = with(coordinator) {
        for (i in 0 until childCount) {
            getChildAt(i)?.let { view ->
                scrollItem(view, delta)
            }
        }
        with(recycler) {
            val firstView = getChildAt(0) ?: return delta
            val lastView = getChildAt(childCount - 1) ?: return delta

            /** recycling */
            if (delta < 0) {
                if (firstView.isOutOfParent()) {
                    removeView(firstView)
                    recycleView(firstView)
                    ++firstVisiblePosition
                }
            } else {
                if (lastView.isOutOfParent()) {
                    removeView(lastView)
                    recycleView(lastView)
                    --lastVisiblePosition
                }
            }

            /** restoring */
            if (delta < 0) {
                if (lastVisiblePosition < itemCount && lastView.bottom < height) {
                    Log.d("TEST", "(last) restore at: $lastVisiblePosition")
                    val newView: View = getViewForPosition(lastVisiblePosition)
                    val newCenter = lastView.positionInParent().shiftPosition(itemMargin)
                    addView(newView)
                    layoutView(newView, newCenter)
                    ++lastVisiblePosition
                }
            } else {
                if (firstVisiblePosition > 0 && firstView.bottom < height) {
                    Log.d("TEST", "(start) restore at: $firstVisiblePosition")
                    val newView: View = getViewForPosition(firstVisiblePosition - 1)
                    val newCenter = firstView.positionInParent().shiftPosition(-itemMargin)
                    addView(newView, 0)
                    layoutView(newView, newCenter)
                    --firstVisiblePosition
                }
            }
        }
        return delta
    }

    private fun scrollItem(view: View, delta: Int) = with(coordinator) {
        val viewPosition = view.positionInParent()
        val newPosition = viewPosition.shiftPosition(delta)
        layoutView(view, newPosition)
    }
}