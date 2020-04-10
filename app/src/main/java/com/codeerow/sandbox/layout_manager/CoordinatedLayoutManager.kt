package com.codeerow.sandbox.layout_manager

import android.graphics.Point
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.codeerow.sandbox.layout_manager.coordinator.Coordinator
import com.codeerow.sandbox.layout_manager.utils.moveTo
import com.codeerow.sandbox.layout_manager.utils.positionInParent


class CoordinatedLayoutManager(private val coordinator: Coordinator) :
    RecyclerView.LayoutManager() {

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
        Log.d("TEST", "onLayoutChildren")
        removeAllViews()
        if (itemCount == 0) return

        // TODO: These values should not be set to "0". They should be restored from state
        // TODO: These values should not be set to "0". They should be restored from state

        lastVisiblePosition = 0
        firstVisiblePosition = 0

        var currentPoint: Point? = coordinator.startPosition
        do {
            val view = recycler.getViewForPosition(lastVisiblePosition)
            addView(view)
            layoutView(view, currentPoint)
            currentPoint = coordinator.nextPosition(currentPoint)
            lastVisiblePosition++
        } while (!coordinator.isLastPosition(view.positionInParent()) && lastVisiblePosition < itemCount)
    }

    private fun layoutView(view: View, currentPoint: Point?) {
        if (currentPoint == null) return
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

    private fun scrollBy(delta: Int, recycler: Recycler): Int {
        val firstView = getChildAt(0)
        val lastView = getChildAt(childCount - 1)

        for (i in 0 until childCount) {
            val view = getChildAt(i) ?: return 0
            val newPosition = coordinator.calculatePosition(view.positionInParent(), delta)
            if (newPosition != null) view.moveTo(newPosition)
        }

        performRecycling(delta, firstView, lastView, recycler)
        return delta
    }


    private fun performRecycling(
        delta: Int,
        firstView: View?,
        lastView: View?,
        recycler: Recycler
    ) {
        if (delta < 0) {
            /** Scroll down */
            firstView?.let { recycleTopIfNeeded(firstView, recycler) }
            lastView?.let { addToBottomIfNeeded(lastView, recycler) }
        } else {
            /** Scroll up */
            lastView?.let { recycleBottomIfNeeded(lastView, recycler) }
            firstView?.let { addTopIfNeeded(firstView, recycler) }
        }
    }

    private fun addToBottomIfNeeded(lastView: View, recycler: Recycler) {
        if (!coordinator.isLastPosition(lastView.positionInParent())) {
            if (lastVisiblePosition < itemCount) {
                val newLastView = recycler.getViewForPosition(lastVisiblePosition)
                addView(newLastView)
                layoutView(newLastView, coordinator.nextPosition(lastView.positionInParent()))
                ++lastVisiblePosition
            }
        }
    }

    private fun addTopIfNeeded(firstView: View, recycler: Recycler) {
        if (!coordinator.isFirstPosition(firstView.positionInParent())) {
            if (firstVisiblePosition > 0) {
                val newFirstView = recycler.getViewForPosition(firstVisiblePosition - 1)
                addView(newFirstView, 0)
                layoutView(newFirstView, coordinator.prevPosition(firstView.positionInParent()))
                --firstVisiblePosition
            }
        }
    }

    private fun recycleTopIfNeeded(firstView: View, recycler: Recycler) {
        if (coordinator.isFirstPosition(firstView.positionInParent()) &&
            firstVisiblePosition + 1 < lastVisiblePosition
        ) {
            removeView(firstView)
            ++firstVisiblePosition
            recycler.recycleView(firstView)
        }
    }

    private fun recycleBottomIfNeeded(lastView: View, recycler: Recycler) {
        if (coordinator.isLastPosition(lastView.positionInParent()) &&
            lastVisiblePosition - 1 > firstVisiblePosition
        ) {
            removeView(lastView)
            --lastVisiblePosition
            recycler.recycleView(lastView)
        }
    }
}