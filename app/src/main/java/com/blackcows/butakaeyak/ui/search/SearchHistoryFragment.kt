package com.blackcows.butakaeyak.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentSearchHistoryBinding
import com.blackcows.butakaeyak.ui.search.adapter.SearchRecyclerAdapter

private const val TAG = "검색 기록"
class SearchHistoryFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentSearchHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchHistoryAdapter : SearchRecyclerAdapter


    private var columnCount = 2 //컬럼 갯수 = 2 그리드

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
        val view = inflater.inflate(R.layout.fragment_search_history, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
//                adapter = HomeRecyclerAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "검색 기록"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            SearchHistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}