package com.artspb.moviesearchapp.ui.poster

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.artspb.moviesearchapp.R
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.LayerType
import com.bumptech.glide.Glide

class PosterActivity : AppCompatActivity() {

    private lateinit var viewModel: PosterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)

        val btnBack: Button = findViewById(R.id.btnBack)
        val posterImage: ImageView = findViewById(R.id.posterImage)
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvTypeBadge: TextView = findViewById(R.id.tvTypeBadge)
        val tvYear: TextView = findViewById(R.id.tvYear)

        val title = intent.getStringExtra("EXTRA_TITLE") ?: ""
        val image = intent.getStringExtra("EXTRA_IMAGE") ?: ""
        val type = intent.getStringExtra("EXTRA_TYPE") ?: ""
        val year = intent.getStringExtra("EXTRA_YEAR") ?: ""

        tvTitle.text = title
        tvTypeBadge.text = type.uppercase()
        tvYear.text = year

        viewModel = ViewModelProvider(this, PosterViewModel.getFactory(image))[PosterViewModel::class.java]

        viewModel.observeUrl().observe(this) { url ->
            ArchitectureFlowMonitor.logStep(
                layer = LayerType.UI,
                title = "PosterActivity: отображение постера",
                details = "Получен URL из PosterViewModel (LiveData)",
                payloadPreview = "url = $url"
            )

            Glide.with(this)
                .load(url)
                .centerCrop()
                .into(posterImage)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
