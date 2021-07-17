package com.jtk.tugaseas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jtk.tugaseas.Data.Models.Category

class CategoryAdapter(val categories: List<Category>, val clickItemListener: ClickItemListener)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.category_container, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)

        holder.itemView.setOnClickListener {
            clickItemListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText = itemView.findViewById<TextView>(R.id.category_name)
        val categoryImage = itemView.findViewById<ImageView>(R.id.category_image)

        fun bind(category: Category) {
            categoryText.text = category.name
            if (category.image != null)
                categoryImage.setImageResource(category.image)
            else
                categoryImage.setImageResource(R.drawable.image_not_avail)
        }
    }

    interface ClickItemListener {
        fun onItemClick(position: Int)
    }

}