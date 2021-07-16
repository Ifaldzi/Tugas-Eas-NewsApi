package com.jtk.tugaseas.Data.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Article(val source: Source,
                   val author: String,
                   val title: String,
                   val description: String,
                   val url: String,
                   @SerializedName("urlToImage") val image: String,
                   val publishedAt: Date,
                   val content: String) : Serializable