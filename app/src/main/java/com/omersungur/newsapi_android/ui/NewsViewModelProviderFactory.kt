package com.omersungur.newsapi_android.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omersungur.newsapi_android.repository.NewsRepository
import com.omersungur.newsapi_android.viewmodel.NewsViewModel

class NewsViewModelProviderFactory(private val application: Application, private val newsRepository: NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(application,newsRepository) as T
    }
}