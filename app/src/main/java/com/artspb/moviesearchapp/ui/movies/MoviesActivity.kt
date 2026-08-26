package com.artspb.moviesearchapp.ui.movies

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artspb.moviesearchapp.R
import com.artspb.moviesearchapp.domain.models.Movie
import com.artspb.moviesearchapp.ui.inspector.ArchitectureBottomSheetDialog
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.FlowStep
import com.artspb.moviesearchapp.ui.inspector.LayerType
import com.artspb.moviesearchapp.ui.poster.PosterActivity

class MoviesActivity : AppCompatActivity(), ArchitectureFlowMonitor.FlowStepListener {

    private lateinit var searchButton: Button
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView

    private lateinit var hudFlowCard: View
    private lateinit var btnOpenFlowLog: Button
    private lateinit var tvHudStatus: TextView
    private lateinit var pillUi: TextView
    private lateinit var pillViewModel: TextView
    private lateinit var pillDomain: TextView
    private lateinit var pillData: TextView
    private lateinit var pillMapping: TextView

    private val movies = ArrayList<Movie>()
    private val adapter = MoviesAdapter()

    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Поиск фильмов (MVVM)"

        viewModel = ViewModelProvider(this, MoviesViewModel.getFactory())[MoviesViewModel::class.java]

        placeholderMessage = findViewById(R.id.placeholderMessage)
        searchButton = findViewById(R.id.searchButton)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.moviesRecyclerView)

        hudFlowCard = findViewById(R.id.hudFlowCard)
        btnOpenFlowLog = findViewById(R.id.btnOpenFlowLog)
        tvHudStatus = findViewById(R.id.tvHudStatus)
        pillUi = findViewById(R.id.pillUi)
        pillViewModel = findViewById(R.id.pillViewModel)
        pillDomain = findViewById(R.id.pillDomain)
        pillData = findViewById(R.id.pillData)
        pillMapping = findViewById(R.id.pillMapping)

        adapter.movies = movies
        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        adapter.onMovieClick = { movie ->
            ArchitectureFlowMonitor.logStep(
                layer = LayerType.UI,
                title = "Клик по карточке фильма",
                details = "Передача чистой модели Domain в PosterActivity через Intent",
                payloadPreview = "Movie(title='${movie.title}', id='${movie.id}')"
            )

            val intent = Intent(this, PosterActivity::class.java).apply {
                putExtra("EXTRA_TITLE", movie.title)
                putExtra("EXTRA_IMAGE", movie.image)
                putExtra("EXTRA_TYPE", movie.resultType)
                putExtra("EXTRA_YEAR", movie.description)
            }
            startActivity(intent)
        }

        val openLogAction = View.OnClickListener {
            ArchitectureBottomSheetDialog().show(supportFragmentManager, ArchitectureBottomSheetDialog.TAG)
        }
        btnOpenFlowLog.setOnClickListener(openLogAction)
        hudFlowCard.setOnClickListener(openLogAction)

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.toString().isNotEmpty()) {
                    ArchitectureFlowMonitor.clearHistory()
                    resetHudPills()
                    
                    ArchitectureFlowMonitor.logStep(
                        layer = LayerType.UI,
                        title = "Ввод текста в EditText",
                        details = "UI-слой передает строку запроса во ViewModel для debounce",
                        payloadPreview = "query = '${p0}'"
                    )
                    
                    viewModel.searchDebounce(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        
        searchButton.visibility = View.GONE // Кнопка больше не нужна, так как поиск автоматический (debounce)

        viewModel.observeState().observe(this) { state ->
            render(state)
        }

        viewModel.observeShowToast().observe(this) { toastMessage ->
            if (toastMessage != null) {
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
            }
        }

        ArchitectureFlowMonitor.addListener(this)
    }

    private fun render(state: MoviesState) {
        ArchitectureFlowMonitor.logStep(
            layer = LayerType.UI,
            title = "MoviesActivity.render()",
            details = "UI-слой получил новое состояние экрана от LiveData",
            payloadPreview = "State: ${state::class.java.simpleName}"
        )
        
        when (state) {
            is MoviesState.Loading -> {
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.text = "Загрузка..."
                movies.clear()
                adapter.notifyDataSetChanged()
            }
            is MoviesState.Content -> {
                placeholderMessage.visibility = View.GONE
                movies.clear()
                movies.addAll(state.movies)
                adapter.notifyDataSetChanged()
            }
            is MoviesState.Error -> {
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.text = state.errorMessage
                movies.clear()
                adapter.notifyDataSetChanged()
            }
            is MoviesState.Empty -> {
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.text = state.message
                movies.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ArchitectureFlowMonitor.removeListener(this)
    }

    override fun onStepAdded(step: FlowStep) {
        runOnUiThread {
            tvHudStatus.text = "${step.layer.displayName}: ${step.title}"
            btnOpenFlowLog.text = "📋 Лог (${ArchitectureFlowMonitor.getHistory().size})"

            when (step.layer) {
                LayerType.UI -> pillUi.setBackgroundColor(Color.parseColor(LayerType.UI.colorHex))
                LayerType.VIEW_MODEL -> pillViewModel.setBackgroundColor(Color.parseColor(LayerType.VIEW_MODEL.colorHex))
                LayerType.DOMAIN -> pillDomain.setBackgroundColor(Color.parseColor(LayerType.DOMAIN.colorHex))
                LayerType.DATA -> pillData.setBackgroundColor(Color.parseColor(LayerType.DATA.colorHex))
                LayerType.MAPPING -> pillMapping.setBackgroundColor(Color.parseColor(LayerType.MAPPING.colorHex))
            }
        }
    }

    override fun onFlowCleared() {
        runOnUiThread {
            btnOpenFlowLog.text = "📋 Лог (0)"
            tvHudStatus.text = "Готово к поиску..."
            resetHudPills()
        }
    }

    private fun resetHudPills() {
        val dimColor = Color.parseColor("#313244")
        pillUi.setBackgroundColor(dimColor)
        pillViewModel.setBackgroundColor(dimColor)
        pillDomain.setBackgroundColor(dimColor)
        pillData.setBackgroundColor(dimColor)
        pillMapping.setBackgroundColor(dimColor)
    }
}
