package com.codeerow.sandbox.layout_manager

import android.graphics.Point
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


abstract class AbstractLayoutManager : RecyclerView.LayoutManager() {

    var firstVisiblePosition: Int = 0
    var lastVisiblePosition: Int = 0


    override fun canScrollVertically() = true
    override fun canScrollHorizontally() = false

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }


    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        return if (childCount == 0) { // we cannot scroll if we don't have views
            0
        } else scrollBy(dy, recycler)
    }


    abstract fun scrollBy(delta: Int, recycler: Recycler): Int
    abstract fun layoutNextView(view: View, currentPoint: Point?): Point?
    abstract fun layoutPrevView(view: View, currentPoint: Point?): Point?

//    override fun scrollVerticallyBy(
//        dy: Int,
//        recycler: RecyclerView.Recycler?,
//        state: RecyclerView.State?
//    ): Int {
//        for (i in 0 until childCount) {
//            val view = getChildAt(i) ?: return dy
////            if (i % 2 == 0) view.offsetTopAndBottom(dy)
////            else view.offsetTopAndBottom(-dy)
//            if (dy > 0) view.scrollUp(dy)
//            else view.scrollDown(dy)
//        }
//        return dy
//    }
//
//
//    private fun View.scrollDown(dy: Int) {
//        val leftBound = 0
//        val bottomBound = this@CustomLayoutManager.height
//        when {
//            left > leftBound -> offsetLeftAndRight(dy)
//            bottom > bottomBound -> offsetLeftAndRight(-dy)
//            else -> offsetTopAndBottom(-dy)
//        }
//    }
//
//    private fun View.scrollUp(dy: Int) {
//        val upperBound = 0
//        val rightBound = this@CustomLayoutManager.width
//        when {
//            right > rightBound -> offsetTopAndBottom(dy)
//            top > upperBound -> offsetTopAndBottom(-dy)
//            else -> offsetLeftAndRight(dy)
//        }
//    }
}