package com.omersungur.newsapi_android.repository

import com.omersungur.newsapi_android.db.ArticleDatabase
import com.omersungur.newsapi_android.model.Article
import com.omersungur.newsapi_android.service.RetrofitInstance

class NewsRepository (val db: ArticleDatabase)  {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery : String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticles(article: Article) = db.getArticleDao().deleteArticles(article)

}