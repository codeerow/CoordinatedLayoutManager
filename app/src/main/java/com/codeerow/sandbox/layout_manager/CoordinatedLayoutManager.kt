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

    private fun scrollEachItem(delta: Int, recycler: Recycler): Int {
        for (i in 0 until childCount) {
            getChildAt(i)?.let { view ->
                scrollItem(view, delta)
            }
        }
        with(recycler) {
            if (canRecycle(delta)) recycleItem(delta)
            if (canRestore(delta)) restoreItem(delta)
        }
        return delta
    }

    private fun scrollItem(view: View, delta: Int) = with(coordinator) {
        val viewPosition = view.positionInParent()
        val newPosition = viewPosition.shiftPosition(delta)
        layoutView(view, newPosition)
    }


    /** Recycling */
    private fun canRecycle(delta: Int): Boolean {
        return if (delta < 0) canRecycleStart()
        else canRecycleEnd()
    }

    private fun canRecycleEnd(): Boolean {
        return false
//        return getChildAt(lastVisiblePosition)?.isOutOfParent() == true
    }

    private fun canRecycleStart(): Boolean {
        return getChildAt(0)?.isOutOfParent() == true
    }


    private fun Recycler.recycleItem(delta: Int) {
        Log.d("TEST", "recycle at: $firstVisiblePosition")
        val view = getChildAt(0) ?: return
        removeView(view)
        recycleView(view)
        /*  if (delta < 0)*/ ++firstVisiblePosition
//        else ++firstVisiblePosition
    }


    /** Restoring */
    private fun canRestore(delta: Int): Boolean {
        return if (delta < 0) canRestoreAtEnd()
        else canRestoreAtStart()
    }

    private fun canRestoreAtStart(): Boolean {
        return firstVisiblePosition > 0 && getChildAt(0)?.bottom!! < height
    }

    private fun canRestoreAtEnd(): Boolean {
        /* val lastView = getChildAt(lastVisiblePosition - 1) ?:*/ return false
        /* return !lastView.isOutOfParent() && lastVisiblePosition + 1 <= childCount*/
    }


    private fun Recycler.restoreItem(delta: Int) = with(coordinator) {
        val newView: View
        val newCenter: Point
        /* if (delta < 0) {
             val lastView = getChildAt(lastVisiblePosition) ?: return@with
             ++lastVisiblePosition
             newView = getViewForPosition(lastVisiblePosition)
             newCenter = lastView.positionInParent().shiftPosition(itemMargin)
         } else {*/
        val firstView = getChildAt(0) ?: return@with
        firstView.performClick()
        --firstVisiblePosition
        Log.d("TEST", "restore at: $firstVisiblePosition")
        newView = getViewForPosition(firstVisiblePosition)
        newCenter = firstView.positionInParent().shiftPosition(-itemMargin)
//        }
        addView(newView, 0)
        layoutView(newView, newCenter)
    }
}