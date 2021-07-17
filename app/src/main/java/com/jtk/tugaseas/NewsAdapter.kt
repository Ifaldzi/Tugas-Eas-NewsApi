package com.jtk.tugaseas

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jtk.tugaseas.Data.Models.Article
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter : ListAdapter<Article, NewsAdapter.ViewHolder>(NewsDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headlineTitle = view.findViewById<TextView>(R.id.news_title)
        val newsDate = view.findViewById<TextView>(R.id.news_date)
        val newsPublisher = view.findViewById<TextView>(R.id.news_publisher)
        val newsImage = view.findViewById<ImageView>(R.id.news_image)

        fun bind(article: Article) {
            headlineTitle.text = article.title
            newsDate.text = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(article.publishedAt)
            newsPublisher.text = article.source.name
            if(TextUtils.isEmpty(article.image))
                newsImage.setImageResource(R.drawable.image_not_avail)
            else
                Picasso.get().load(article.image).resize(250, 250).into(newsImage)
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_container, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article: Article = getItem(position)
        holder.bind(article)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, NewsDetailActivity::class.java)
            intent.putExtra("article", article)
            it.context.startActivity(intent)
        }
    }
}