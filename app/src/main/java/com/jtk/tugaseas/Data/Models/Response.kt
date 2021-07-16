package com.jtk.tugaseas.Data.Models

data class Response(val status: String,
                    val totalResults: Int,
                    val articles: List<Article>)