package com.jtk.tugaseas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jtk.tugaseas.Data.Models.Article
import com.jtk.tugaseas.Data.Models.Response
import com.jtk.tugaseas.Data.NewsApi
import com.jtk.tugaseas.Data.NewsApiClient
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private var newsApi: NewsApi? = null

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsAdapter = NewsAdapter()
        val dividerItem = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val recyclerView = findViewById<RecyclerView>(R.id.news_list)
        recyclerView.adapter = newsAdapter
        recyclerView.addItemDecoration(dividerItem)

        newsViewModel.news.observe(this){news ->
            news.let { newsAdapter.submitList(it) }
        }

        newsApi = NewsApiClient.getApiClient()?.create(NewsApi::class.java)
        getNews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchViewItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchViewItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun getNews() {
        val call: Call<Response>? = newsApi?.getHeadlinesFromIndonesia()

        call.let {
            it?.enqueue(object : Callback<Response>{
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()

                        //update live data
                        data?.articles?.let { it -> newsViewModel.updateNews(it) }
                        Log.d("API Call", data?.articles?.size.toString())
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    t.message?.let { it1 -> Log.e("API Error", it1) }
                }

            })
        }
    }

    private fun searchNews(query: String?) {
        val call = query?.let { newsApi?.searchNews(it) }

        call.let {
            it?.enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.articles?.let { it -> newsViewModel.updateNews(it) }

                        Log.d("API CALL", data?.articles?.size.toString())
                    }
                    Log.d("API CALL", response.code().toString())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    t.message?.let { it -> Log.e("API ERR", it) }
                }

            })
        }
    }
}