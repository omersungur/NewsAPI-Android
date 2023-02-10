package com.omersungur.newsapi_android.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.omersungur.newsapi_android.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Eski verileri değiştirmek ve işleme devam etmek için kullandık.
    suspend fun upsert(article: Article) : Long

    @Query("SELECT * FROM articles")
    fun getAllArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticles(article: Article)
}