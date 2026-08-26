package com.artspb.moviesearchapp.ui.poster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.artspb.moviesearchapp.R
import com.artspb.moviesearchapp.databinding.ActivityDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {

    // Я решил перенести отображение постера и информации в отдельные фрагменты,
    // а для навигации между ними использую TabLayout и ViewPager2.

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val poster = intent.getStringExtra("poster") ?: ""
        val movieId = intent.getStringExtra("id") ?: ""

        binding.viewPager.adapter = DetailsViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
            posterUrl = poster,
            movieId = movieId,
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.poster)
                1 -> tab.text = getString(R.string.details)
            }
        }
        tabMediator.attach()

        // Добавляем кнопку "Назад" в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}