package com.example.lemacdatalab.ui.viewmodel

data class SolicitudFormUiState(
    val codigoMuestra: String = "",
    val solicitante: String = "",
    val tipoEnsayo: String = "",
    val cantidadText: String = "1",
    val esUrgente: Boolean = false,
    val observaciones: String = "",
    
    // Errores específicos por campo
    val codigoError: String? = null,
    val solicitanteError: String? = null,
    val tipoEnsayoError: String? = null,
    val cantidadError: String? = null,
    
    // Feedback general
    val mensajeExito: String? = null,
    val solicitudCreadaId: String? = null
)
