package com.artspb.moviesearchapp.ui.inspector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artspb.moviesearchapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ArchitectureBottomSheetDialog : BottomSheetDialogFragment() {

    private val adapter = ArchitectureFlowAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_architecture_flow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvFlowSteps: RecyclerView = view.findViewById(R.id.rvFlowSteps)
        val btnClearHistory: Button = view.findViewById(R.id.btnClearHistory)

        rvFlowSteps.layoutManager = LinearLayoutManager(context)
        rvFlowSteps.adapter = adapter

        adapter.setSteps(ArchitectureFlowMonitor.getHistory())

        btnClearHistory.setOnClickListener {
            ArchitectureFlowMonitor.clearHistory()
            adapter.setSteps(emptyList())
        }
    }

    companion object {
        const val TAG = "ArchitectureBottomSheetDialog"
    }
}
