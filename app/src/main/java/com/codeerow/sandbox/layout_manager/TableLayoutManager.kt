package com.codeerow.sandbox.layout_manager

import android.graphics.Point
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.codeerow.sandbox.layout_manager.coordinator.TableCoordinator
import com.codeerow.sandbox.layout_manager.utils.isOutOfParent
import com.codeerow.sandbox.layout_manager.utils.positionInParent
import kotlin.math.max
import kotlin.math.min


class TableLayoutManager(private val itemMargin: Int) : CoordinatedLayoutManager(itemMargin) {

    override lateinit var coordinator: TableCoordinator

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val view = recycler.getViewForPosition(0)
        val (itemWidth, itemHeight) = view.measure()
        coordinator = TableCoordinator(
            width = width - itemWidth,
            height = height - itemHeight,
            startPosition = Point(width - itemWidth / 2, height - itemHeight / 2)
        )
        super.onLayoutChildren(recycler, state)
    }

    override fun layoutView(view: View, currentPoint: Point) = with(coordinator) {
        super.layoutView(view, currentPoint)
        view.rotation = calculateRotation(currentPoint)
    }


    override fun calculateAvailableScroll(dy: Int): Int {
        val firstView = getChildAt(0) ?: return dy
        val lastView = getChildAt(childCount - 1) ?: return dy

        return if (dy < 0) {
            if (lastVisiblePosition == itemCount) {
                val offset = height - lastView.bottom
                max(offset, dy)
            } else {
                dy
            }

        } else {
            if (firstVisiblePosition == 0) {
                val offset = firstView.bottom - height
                min(offset, dy)
            } else {
                dy
            }
        }
    }

    override fun RecyclerView.Recycler.recycleItems(firstView: View, lastView: View, delta: Int) {
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
    }

    override fun RecyclerView.Recycler.restoreItems(firstView: View, lastView: View, delta: Int) =
        with(coordinator) {
            if (delta < 0) {
                if (lastVisiblePosition < itemCount && lastView.bottom < height) {
                    val newView: View = getViewForPosition(lastVisiblePosition)
                    val newCenter = lastView.positionInParent().shiftPosition(itemMargin)
                    addView(newView)
                    layoutView(newView, newCenter)
                    ++lastVisiblePosition
                }
            } else {
                if (firstVisiblePosition > 0 && firstView.bottom < height) {
                    val newView: View = getViewForPosition(firstVisiblePosition - 1)
                    val newCenter = firstView.positionInParent().shiftPosition(-itemMargin)
                    addView(newView, 0)
                    layoutView(newView, newCenter)
                    --firstVisiblePosition
                }
            }
        }
}