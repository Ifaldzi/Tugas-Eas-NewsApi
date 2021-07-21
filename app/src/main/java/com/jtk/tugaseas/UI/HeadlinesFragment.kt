package com.jtk.tugaseas.UI

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jtk.tugaseas.Data.NewsApi
import com.jtk.tugaseas.NewsAdapter
import com.jtk.tugaseas.NewsViewModel
import com.jtk.tugaseas.R
import androidx.fragment.app.viewModels
import com.jtk.tugaseas.Data.Models.Response
import com.jtk.tugaseas.Data.NewsApiClient
import com.jtk.tugaseas.MainActivity
import retrofit2.Call
import retrofit2.Callback

class HeadlinesFragment : Fragment() {

    private var newsApi: NewsApi? = null

    lateinit var newsViewModel: NewsViewModel

    private lateinit var errorText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.headline_fragment, container, false)

        activity?.title = "Headlines"

        newsViewModel = (activity as MainActivity).newsViewModel

        errorText = view.findViewById<TextView>(R.id.error_load_text)

        errorText.setOnClickListener {
            getNews()
        }

        initiateRecyclerView(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsApi = NewsApiClient.getApiClient()?.create(NewsApi::class.java)
        if (savedInstanceState == null)
            getNews()
    }

    private fun initiateRecyclerView(view: View) {
        val newsAdapter = NewsAdapter()
        val dividerItem = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val recyclerView = view.findViewById<RecyclerView>(R.id.news_list)
        recyclerView.adapter = newsAdapter
        recyclerView.addItemDecoration(dividerItem)

        newsViewModel.news.observe(viewLifecycleOwner) {news ->
            news.let { newsAdapter.submitList(it) }
        }
    }

    private fun getNews() {
        val call: Call<Response>? = newsApi?.getHeadlinesFromIndonesia()

        val progressBar = view?.findViewById<ProgressBar>(R.id.news_progress_bar)
        progressBar?.visibility = View.VISIBLE

        errorText.visibility = View.GONE

        call.let {
            it?.enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()

                        //update live data
                        data?.articles?.let { it -> newsViewModel.updateNews(it) }
                        Log.d("API Call", data?.articles?.size.toString())

                        progressBar?.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    t.message?.let { it1 -> Log.e("API Error", it1) }

                    progressBar?.visibility = View.GONE
                    errorText?.visibility = View.VISIBLE
                }

            })
        }
    }
}