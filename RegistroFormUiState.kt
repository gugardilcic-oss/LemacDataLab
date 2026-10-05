package com.example.lemacdatalab.ui.viewmodel

import com.example.lemacdatalab.data.model.Ambitos

data class RegistroFormUiState(
    val aliasUsuario: String = "Usuario_Prueba_01",
    val ambitoSeleccionado: String = Ambitos.NEURODESARROLLO,
    val tipoActividad: String = "",
    val nivelIntensidad: Float = 5f, // Escala 1 a 10
    val desencadenanteEstrategia: String = "",
    val observacionesContexto: String = "",

    // Mensajes de error específicos
    val tipoActividadError: String? = null,
    val desencadenanteError: String? = null,

    // Retroalimentación
    val mensajeExito: String? = null,
    val registroCreadoId: String? = null
)
