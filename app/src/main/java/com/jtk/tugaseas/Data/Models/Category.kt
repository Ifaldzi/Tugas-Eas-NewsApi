package com.jtk.tugaseas.Data.Models

import com.jtk.tugaseas.R

data class Category(val name: String, val image: Int?) {

    companion object {
        fun getNewsApiCategory() : List<Category> {
            val categories = ArrayList<Category>()

            var category = Category("Technology", R.drawable.category_technology)
            categories.add(category)
            category = Category("Business", R.drawable.category_business)
            categories.add(category)
            category = Category("Entertainment", R.drawable.category_entertainment)
            categories.add(category)
            category = Category("General", R.drawable.category_general)
            categories.add(category)
            category = Category("Health", R.drawable.category_health)
            categories.add(category)
            category = Category("Science", R.drawable.category_science)
            categories.add(category)
            category = Category("Sports", R.drawable.category_sports)
            categories.add(category)

            return categories
        }
    }
}