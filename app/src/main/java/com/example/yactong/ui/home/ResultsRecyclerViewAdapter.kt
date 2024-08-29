package com.example.yactong.ui.home

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.example.yactong.R

import com.example.yactong.ui.home.placeholder.PlaceholderContent.PlaceholderItem
import com.example.yactong.databinding.ItemResultsBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ResultsRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemResultsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val imgUri = Uri.parse("android.resource://com.example.project/" + R.drawable.choco)
//        holder.pillPicture.setImageURI(item.pill_pic.toUri())
        holder.pillPicture.setImageURI(imgUri)
        holder.pillName.text = item.pill_name
        holder.pillType.text = item.pill_type
//        holder.pillFavorite.isClickable =
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemResultsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val pillPicture: ImageView = binding.ivPill
        val pillName: TextView = binding.tvPillname
        val pillType: TextView = binding.tvPilltype
        val pillFavorite: ImageButton = binding.btnPillfavoriteadd

        override fun toString(): String {
            return super.toString() + " '" + pillName.text + "'"
        }
    }

}