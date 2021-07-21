package com.jtk.tugaseas.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.jtk.tugaseas.*
import com.jtk.tugaseas.Data.Models.Category
import com.jtk.tugaseas.Data.Models.Response
import com.jtk.tugaseas.Data.NewsApi
import com.jtk.tugaseas.Data.NewsApiClient
import retrofit2.Call
import retrofit2.Callback

class SearchFragment : Fragment() {

    private var newsApi: NewsApi? = null
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    lateinit var searchResultLabel: TextView
    private lateinit var newsSearchRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var categoryRecyclerView: RecyclerView

    companion object {
        fun getInstance() : Fragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        activity?.title = "News Discovery"

        newsApi = NewsApiClient.getApiClient()?.create(NewsApi::class.java)

        initiateSearchLayoutComponent(view)

        return view
    }

    private fun initiateSearchLayoutComponent(view: View) {
        searchResultLabel = view.findViewById<TextView>(R.id.search_result_label)
        newsSearchRecyclerView = view.findViewById<RecyclerView>(R.id.news_search_results)
        progressBar = view.findViewById(R.id.news_progress_bar)
        searchView = view.findViewById<SearchView>(R.id.search_news_view)
        categoryRecyclerView = view.findViewById<RecyclerView>(R.id.category_list)

        val newsAdapter = NewsAdapter()
        newsSearchRecyclerView.adapter = newsAdapter

        newsViewModel.news.observe(viewLifecycleOwner){news ->
            news.let { newsAdapter.submitList(it) }
        }

        val categories = Category.getNewsApiCategory()
        val categoryAdapter = CategoryAdapter(categories, object : CategoryAdapter.ClickItemListener {
            override fun onItemClick(position: Int) {
                val category = categories[position]
                getNewsByCategory(category.name)
                changeSearchResultLabel(category.name)
            }
        })

        categoryRecyclerView.adapter = categoryAdapter

        configureSearchView()
    }

    private fun configureSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews(query)
                Log.d("Search V", "Searching..")
                if (query != null) {
                    changeSearchResultLabel(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun changeSearchResultLabel(label: String) {
        searchResultLabel.text = getString(R.string.search_result_label, label);
        searchResultLabel.visibility = View.VISIBLE
    }

    private fun searchNews(query: String?) {
        val call = query?.let { newsApi?.searchNews(it) }

        progressBar.visibility = View.VISIBLE

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
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    t.message?.let { it -> Log.e("API ERR", it) }
                    progressBar.visibility = View.GONE
                }

            })
        }
    }

    private fun getNewsByCategory(category: String) {
        val call =  newsApi?.getNewsByCategory(category)

        progressBar.visibility = View.VISIBLE

        call.let {
            it?.enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.articles?.let { newsViewModel.updateNews(it) }

                        Log.d("API CALL", data?.articles?.size.toString())
                    }
                    Log.d("API CALL", response.code().toString())
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    t.message?.let { it -> Log.e("API ERR", it) }

                    progressBar.visibility = View.GONE
                }

            })
        }
    }
}