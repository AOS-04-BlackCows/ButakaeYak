package com.blackcows.butakaeyak.ui.take.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blackcows.butakaeyak.R

object TakeRvDecorator {
    fun getLinearDeco() = object : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(0,16,0,16)
        }
    }

    fun getLinearDecoSimpleItem() = object : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(0,0,0,4)
        }
    }

    fun getLinearDecoWithDivider() = object : ItemDecoration() {
        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
            val widthMargin = 20f   // 좌우 Margin
            val height = 2f         // 사각형의 height
            val context = parent.context

            val left = parent.paddingLeft.toFloat()
            val right = parent.width - parent.paddingRight.toFloat()
            val paint = Paint().apply { color = context.getColor(R.color.any_200) }
            for(i in 0 until parent.childCount) {
                val view = parent.getChildAt(i)
                val top = view.bottom.toFloat() + (view.layoutParams as RecyclerView.LayoutParams).bottomMargin
                val bottom = top + height

                c.drawRect(left + widthMargin, top, right - widthMargin, bottom, paint)
            }
        }
    }
}