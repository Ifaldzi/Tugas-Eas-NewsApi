package com.jtk.tugaseas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jtk.tugaseas.Data.Models.Article
import com.jtk.tugaseas.Data.Models.Response
import com.jtk.tugaseas.Data.NewsApi
import com.jtk.tugaseas.Data.NewsApiClient
import com.jtk.tugaseas.UI.HeadlinesFragment
import com.jtk.tugaseas.UI.SearchFragment
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    var newsApi: NewsApi? = null

    val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsApi = NewsApiClient.getApiClient()?.create(NewsApi::class.java)
        
        loadFragment(HeadlinesFragment())
        
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigation.setOnItemSelectedListener{
            var fragment: Fragment? = null

            when(it.itemId) {
                R.id.headline_menu -> {
                    fragment = HeadlinesFragment()
                }
                R.id.search_menu -> {
                    fragment = SearchFragment.getInstance()
                }
            }

            loadFragment(fragment)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu, menu)
//
//        val searchViewItem = menu?.findItem(R.id.app_bar_search)
//        val searchView = searchViewItem?.actionView as SearchView
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchNews(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//        })
//
//        return super.onCreateOptionsMenu(menu)
//    }
    
    private fun loadFragment(fragment: Fragment?) : Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
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