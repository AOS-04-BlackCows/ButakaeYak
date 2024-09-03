package com.blackcows.butakaeyak.ui.take.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewFormBinding
import com.blackcows.butakaeyak.ui.take.data.FormItem

class FormAdapter(val mItems:MutableList<FormItem>,val context: Context):RecyclerView.Adapter<FormAdapter.FormHolder>() {

    private var itemClickListner : ItemClickListener? = null

    private var mSelectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormAdapter.FormHolder {
        val binding = ItemRecyclerviewFormBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FormHolder(binding)
    }

    override fun onBindViewHolder(holder: FormAdapter.FormHolder, position: Int) {
        Glide.with(holder.image.context)
            .load(mItems[position].aImage)
            .into(holder.image)
        holder.name.text = mItems[position].aName
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class FormHolder(val binding : ItemRecyclerviewFormBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivMedicineForm
        val name = binding.tvMedicineFormName
        val checkBox = binding.cbMedicineForm

        fun bind(position: Int) {

            checkBox.isChecked = position == mSelectedItem

            checkBox.setOnClickListener {
                mSelectedItem = position
                itemClickListner?.onClick(it)
                notifyItemRangeChanged(0, mItems.size)
            }
        }
    }

    interface ItemClickListener {
        fun onClick(view: View)
    }
    fun setItemClickListener(itemClickListener: FormAdapter.ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}