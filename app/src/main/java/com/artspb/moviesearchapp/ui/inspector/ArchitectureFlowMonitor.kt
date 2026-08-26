package com.artspb.moviesearchapp.ui.inspector

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.CopyOnWriteArrayList

// Мой синглтон-монитор для визуальной демонстрации работы слоев Clean Architecture (Вау-эффект для портфолио).
// Все слои (UI, Domain/Interactor, Data/Repository/Network) отправляют сюда свои события при выполнении запроса,
// а главная Activity и шторка с логами отображают их в реальном времени.
object ArchitectureFlowMonitor {

    private val steps = CopyOnWriteArrayList<FlowStep>()
    private val listeners = CopyOnWriteArrayList<FlowStepListener>()
    private var stepCounter = 0L

    interface FlowStepListener {
        fun onStepAdded(step: FlowStep)
        fun onFlowCleared()
    }

    fun addListener(listener: FlowStepListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: FlowStepListener) {
        listeners.remove(listener)
    }

    fun clearHistory() {
        steps.clear()
        stepCounter = 0L
        listeners.forEach { it.onFlowCleared() }
    }

    fun logStep(
        layer: LayerType,
        title: String,
        details: String = "",
        payloadPreview: String = ""
    ) {
        stepCounter++
        val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val step = FlowStep(
            id = stepCounter,
            timestamp = timeFormat,
            layer = layer,
            threadName = Thread.currentThread().name,
            title = title,
            details = details,
            payloadPreview = payloadPreview
        )
        steps.add(step)
        listeners.forEach { it.onStepAdded(step) }
    }

    fun getHistory(): List<FlowStep> = steps.toList()
}
