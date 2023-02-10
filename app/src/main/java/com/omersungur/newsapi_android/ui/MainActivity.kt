package com.omersungur.newsapi_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.omersungur.newsapi_android.R
import com.omersungur.newsapi_android.databinding.ActivityMainBinding
import com.omersungur.newsapi_android.db.ArticleDatabase
import com.omersungur.newsapi_android.repository.NewsRepository
import com.omersungur.newsapi_android.viewmodel.NewsViewModel


class MainActivity : AppCompatActivity() {

    lateinit var viewModel : NewsViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]

        //binding.bottomNavMenu.setupWithNavController(fragmentContainerView.findNavController())

        //val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavMenu)
        val bottomNavigationView = binding.bottomNavMenu
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }

}