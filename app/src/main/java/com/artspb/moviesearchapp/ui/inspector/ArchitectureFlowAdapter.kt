package com.artspb.moviesearchapp.ui.inspector

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artspb.moviesearchapp.R

class ArchitectureFlowAdapter : RecyclerView.Adapter<ArchitectureFlowAdapter.ViewHolder>() {

    private val steps = ArrayList<FlowStep>()

    fun setSteps(newSteps: List<FlowStep>) {
        steps.clear()
        steps.addAll(newSteps)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flow_step, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLayerBadge: TextView = itemView.findViewById(R.id.tvLayerBadge)
        private val tvThreadName: TextView = itemView.findViewById(R.id.tvThreadName)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        private val layoutPayload: View = itemView.findViewById(R.id.layoutPayload)
        private val tvPayload: TextView = itemView.findViewById(R.id.tvPayload)

        fun bind(step: FlowStep) {
            tvLayerBadge.text = step.layer.displayName
            try {
                tvLayerBadge.setBackgroundColor(Color.parseColor(step.layer.colorHex))
            } catch (e: Exception) {
                tvLayerBadge.setBackgroundColor(Color.DKGRAY)
            }

            tvThreadName.text = "thread: ${step.threadName}"
            tvTimestamp.text = step.timestamp
            tvTitle.text = step.title
            tvDetails.text = step.details

            if (step.payloadPreview.isNotEmpty()) {
                layoutPayload.visibility = View.VISIBLE
                tvPayload.text = step.payloadPreview
            } else {
                layoutPayload.visibility = View.GONE
            }
        }
    }
}
