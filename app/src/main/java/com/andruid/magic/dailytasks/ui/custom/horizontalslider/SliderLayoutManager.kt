package com.andruid.magic.dailytasks.ui.custom.horizontalslider

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.dailytasks.util.ScreenUtils
import kotlin.math.abs
import kotlin.math.sqrt

class SliderLayoutManager(
    context: Context,
    private val recyclerView: RecyclerView,
    private val callback: OnItemSelectedListener?
) : LinearLayoutManager(context, HORIZONTAL, true) {
    init {
        val padding = ScreenUtils.getScreenWidth(context) / 2 - ScreenUtils.dpToPx(context, 32)
        recyclerView.setPadding(padding, 0, padding, 0)

        LinearSnapHelper().attachToRecyclerView(recyclerView)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        scaleDownView()
    }

    override fun scrollHorizontallyBy(
        dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?
    ): Int {
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            scaleDownView()
            scrolled
        } else {
            0
        }
    }

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

    private fun scaleDownView() {
        val mid = width / 2.0f

        for (i in 0 until childCount) {

            // Calculating the distance of the child from the center
            val child = getChildAt(i) ?: continue
            val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f
            val distanceFromCenter = abs(mid - childMid)

            val viewHolder = recyclerView.getChildViewHolder(child)
            if (viewHolder !is ISliderViewHolder?)
                throw ClassCastException("Expected viewHolder of type ${ISliderViewHolder::class.simpleName}")

            if (distanceFromCenter < child.width / 2)
                viewHolder?.select()
            else
                viewHolder?.deselect()

            // The scaling formula
            val scale = 1 - sqrt((distanceFromCenter / width).toDouble()).toFloat() * 0.66f

            // Set scale to view
            child.scaleX = scale
            child.scaleY = scale
        }
    }

    private fun getRecyclerViewCenterX(): Int {
        return (recyclerView.right - recyclerView.left) / 2 + recyclerView.left
    }

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int, view: View?)
    }
}