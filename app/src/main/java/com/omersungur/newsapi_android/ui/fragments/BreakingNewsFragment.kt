package com.omersungur.newsapi_android.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omersungur.newsapi_android.R
import com.omersungur.newsapi_android.adapter.NewsAdapter
import com.omersungur.newsapi_android.ui.MainActivity
import com.omersungur.newsapi_android.util.Constants.Companion.QUERY_PAGE_SIZE
import com.omersungur.newsapi_android.util.Resource
import com.omersungur.newsapi_android.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList()) // list differ, mutable list ile çalışamıyor bu yüzden normal listeye çevirdik.
                        val totalPages =
                            newsResponse.totalResults / QUERY_PAGE_SIZE + 2 // int tipinde bölme işlemi olduğu için tam çıkmıyor ve 1 ekliyoruz, son response boş geldiği için 1 daha ekliyoruz.
                        isLastPage =
                            viewModel.breakingNewsPage == totalPages // arttırma sonucunda eşitlenirse son sayfaya geldik demektir.
                        if (isLastPage) {
                            rvBreakingNews.setPadding(0, 0, 0, 0) // paddingleri resetlemeye yarıyor.
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"An error occured! $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false // son sayfa ise paging'i bitirmek için kullanıyoruz.
    var isScrolling = false // scroll edip etmediğimizi kontrol ediyoruz.

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { // Scrolling yapılıyor demektir.
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition =
                layoutManager.findFirstVisibleItemPosition() // görünürdeki ilk article'ın konumunu verir.
            val visibleItemCount = layoutManager.childCount // görünürdeki article sayısını verir
            val totalItemCount = layoutManager.itemCount // toplam article sayısını verir.

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem =
                firstVisibleItemPosition + visibleItemCount >= totalItemCount // ekrandaki ilk article'ın pozisyonu ve ekrandaki toplam görünen article'ın toplamı, toplam article sayısına eşit veya büyükse o son article'dır
            val isNotAtBeginning =
                firstVisibleItemPosition >= 0 // ekranda ilk article'ı görüyorsak false gelecek.(Kaydırma yaptığımızda true olur, yani 2.article artık başta görünürse)
            val isTotalMoreThanVisible =
                totalItemCount >= QUERY_PAGE_SIZE // her pagede 20 item yüklüyoruz, eğer toplam item sayısı 20'den fazla ise true döer.
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("tr")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)

        }
    }

}