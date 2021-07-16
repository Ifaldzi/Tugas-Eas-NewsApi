package com.jtk.tugaseas

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.jtk.tugaseas.Data.Models.Article
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class NewsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val article = intent.getSerializableExtra("article") as Article
        loadContent(article)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadContent(article: Article) {
        val title = findViewById<TextView>(R.id.news_title_detail)
        val publisher = findViewById<TextView>(R.id.news_publisher_detail)
        val author = findViewById<TextView>(R.id.news_author)
        val date = findViewById<TextView>(R.id.news_date_detail)
        val image = findViewById<ImageView>(R.id.news_image_detail)
        val description = findViewById<TextView>(R.id.news_description)
        val urlSource = findViewById<TextView>(R.id.news_url_source)

        title.text = article.title
        publisher.text = article.source.name
        author.text = getString(R.string.author, article.author)
        date.text = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            .format(article.publishedAt)
        if (TextUtils.isEmpty(article.image))
            image.setImageResource(R.drawable.image_not_avail)
        else
            Picasso.get().load(article.image).into(image)
        description.text = article.description
        urlSource.text = getString(R.string.url_source, article.url)

        urlSource.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.setData(Uri.parse(article.url))
            startActivity(browserIntent)
        }
    }
}