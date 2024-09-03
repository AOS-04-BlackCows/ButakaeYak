package com.blackcows.butakaeyak.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.ui.home.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class PillResultFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = HomeRecyclerAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "결과 화면"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            PillResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}