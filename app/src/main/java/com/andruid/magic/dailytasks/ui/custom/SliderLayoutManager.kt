package com.andruid.magic.dailytasks.ui.custom

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class SliderLayoutManager(
    context: Context,
    private val recyclerView: RecyclerView,
    private val callback: OnItemSelectedListener?
) : LinearLayoutManager(context, HORIZONTAL, true) {
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        // When scroll stops we notify on the selected item
        if (state == RecyclerView.SCROLL_STATE_IDLE) {

            // Find the closest child to the recyclerView center --> this is the selected item.
            val recyclerViewCenterX = getRecyclerViewCenterX()
            var minDistance = recyclerView.width
            var position = -1
            var view: View? = null
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                val childCenterX =
                    getDecoratedLeft(child) + (getDecoratedRight(child) - getDecoratedLeft(child)) / 2
                val childDistanceFromCenter = abs(childCenterX - recyclerViewCenterX)
                if (childDistanceFromCenter < minDistance) {
                    minDistance = childDistanceFromCenter
                    position = recyclerView.getChildLayoutPosition(child)
                    view = child
                }
            }

            // Notify on the selected item
            callback?.onItemSelected(position, view)
        }
    }

    private fun getRecyclerViewCenterX(): Int {
        return (recyclerView.right - recyclerView.left) / 2 + recyclerView.left
    }

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int, view: View?)
    }
}