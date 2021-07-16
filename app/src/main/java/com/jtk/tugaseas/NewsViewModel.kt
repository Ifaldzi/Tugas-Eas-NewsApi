package com.jtk.tugaseas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jtk.tugaseas.Data.Models.Article
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    public val news = MutableLiveData<List<Article>>()

    fun updateNews(article: List<Article>) {
        viewModelScope.launch {
            news.postValue(article)
        }
    }
}