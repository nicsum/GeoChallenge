package com.example.geochallenge.ui.utils

import android.content.Context
import android.graphics.PointF
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import androidx.recyclerview.widget.RecyclerView

class LinearLayoutManagerWithSmoothScroller(context: Context) :
    LinearLayoutManager(context, VERTICAL, false) {

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {

        val smoothScroller = TopSnappedSmoothScroller(recyclerView!!.context)
        smoothScroller.targetPosition = if (position < 0) 0 else position
        startSmoothScroll(smoothScroller)
    }

    inner class TopSnappedSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@LinearLayoutManagerWithSmoothScroller.computeScrollVectorForPosition(
                targetPosition
            )
        }
    }

    override fun getItemViewType(view: View): Int {
        return SNAP_TO_START
    }

}