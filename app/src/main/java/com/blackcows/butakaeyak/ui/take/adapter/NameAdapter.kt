package com.blackcows.butakaeyak.ui.take.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewNameBinding
import com.blackcows.butakaeyak.ui.take.FormSelectDialog
import com.blackcows.butakaeyak.ui.take.data.AlarmItem
import com.blackcows.butakaeyak.ui.take.data.NameAdapterItem
import com.blackcows.butakaeyak.ui.take.data.NameItem
import com.bumptech.glide.Glide

class NameAdapter(val mItems:MutableList<NameItem>, val context: Context): RecyclerView.Adapter<NameAdapter.NameHolder>() {

//    private val items = mutableListOf<NameAdapterItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameHolder {
        val binding = ItemRecyclerviewNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NameHolder(binding)
    }

    override fun onBindViewHolder(holder: NameHolder, position: Int) {

        Glide.with(holder.image.context)
            .load(mItems[position].aImage)
            .into(holder.image)

        holder.image.setOnClickListener {
            FormSelectDialog(context,object :
                FormSelectDialog.OnFormSelectListener {
                override fun onFormSelected(image: Drawable) {
                    Glide.with(holder.image.context)
                        .load(image)
                        .into(holder.image)
                }
            }, holder.image.background).show()
        }

        holder.editText.setText(mItems[position].aText)
        holder.delete.setOnClickListener {
            removeAt(position)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun addItem(nameItem : NameItem) {
        mItems.add(nameItem)
        notifyItemInserted(mItems.size - 1)
    }

    fun removeAt(position: Int) {
        if (position < mItems.size) {
            mItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mItems.size)
        }
    }

    inner class NameHolder(val binding : ItemRecyclerviewNameBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.btnMedicineForm
        val editText = binding.etMedicineName
        val delete = binding.btnMinus
    }
}