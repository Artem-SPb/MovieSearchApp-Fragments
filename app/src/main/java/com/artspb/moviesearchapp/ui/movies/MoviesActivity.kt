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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artspb.moviesearchapp.R
import com.artspb.moviesearchapp.domain.models.Movie
import com.artspb.moviesearchapp.ui.inspector.ArchitectureBottomSheetDialog
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.FlowStep
import com.artspb.moviesearchapp.ui.inspector.LayerType
import com.artspb.moviesearchapp.ui.poster.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

// В этом классе я переписал логику создания ViewModel, 
// теперь Koin делает всю работу за нас (by viewModel())!
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

    private val viewModel: MoviesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Поиск фильмов (MVVM)"

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
                title = "Клик по элементу списка",
                details = "Открываем DetailsActivity",
                payloadPreview = "Movie(title='${movie.title}', id='${movie.id}')"
            )

            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("poster", movie.image)
                putExtra("id", movie.id)
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
                    viewModel.searchDebounce(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        
        searchButton.visibility = View.GONE

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
            btnOpenFlowLog.text = "Лог потока (${ArchitectureFlowMonitor.getHistory().size})"

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
            btnOpenFlowLog.text = "Лог потока (0)"
            tvHudStatus.text = "Ожидание ввода..."
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
