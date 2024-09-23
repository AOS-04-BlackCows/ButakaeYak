package com.blackcows.butakaeyak.ui.schedule.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

object ProfileRvDecoration {
    fun getLinearDecoSimpleItem() = object : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(0,0,14,0)
        }
    }
}