package com.artspb.moviesearchapp.ui.inspector

data class FlowStep(
    val id: Long,
    val timestamp: String,
    val layer: LayerType,
    val threadName: String,
    val title: String,
    val details: String,
    val payloadPreview: String
)

enum class LayerType(val displayName: String, val colorHex: String) {
    UI("Presentation / UI Layer", "#4CAF50"),       // Зеленый
    VIEW_MODEL("Presentation / ViewModel", "#E91E63"), // Розовый
    DOMAIN("Domain Layer (Interactor)", "#FF9800"),  // Оранжевый
    DATA("Data Layer (Network/DTO)", "#2196F3"),     // Синий
    MAPPING("Mapping (DTO ➔ Domain)", "#9C27B0")     // Фиолетовый
}
