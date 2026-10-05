package com.example.lemacdatalab.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.lemacdatalab.data.local.LocalPreferencesStorage
import com.example.lemacdatalab.data.model.Solicitud
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SolicitudViewModel(private val context: Context? = null) : ViewModel() {

    private val _formUiState = MutableStateFlow(SolicitudFormUiState())
    val formUiState: StateFlow<SolicitudFormUiState> = _formUiState.asStateFlow()

    private val _solicitudes = MutableStateFlow<List<Solicitud>>(
        LocalPreferencesStorage.cargarSolicitudes(context)
    )
    val solicitudes: StateFlow<List<Solicitud>> = _solicitudes.asStateFlow()

    // Manejadores de cambios en el formulario
    fun onCodigoChanged(codigo: String) {
        _formUiState.update { it.copy(codigoMuestra = codigo, codigoError = null) }
    }

    fun onSolicitanteChanged(solicitante: String) {
        _formUiState.update { it.copy(solicitante = solicitante, solicitanteError = null) }
    }

    fun onTipoEnsayoChanged(tipo: String) {
        _formUiState.update { it.copy(tipoEnsayo = tipo, tipoEnsayoError = null) }
    }

    fun onCantidadChanged(cantidad: String) {
        _formUiState.update { it.copy(cantidadText = cantidad, cantidadError = null) }
    }

    fun onUrgenteChanged(urgente: Boolean) {
        _formUiState.update { it.copy(esUrgente = urgente) }
    }

    fun onObservacionesChanged(obs: String) {
        _formUiState.update { it.copy(observaciones = obs) }
    }

    // Validación y almacenamiento del formulario
    fun validarYGuardar(): Boolean {
        val currentState = _formUiState.value

        var hasError = false
        var codigoErr: String? = null
        var solicitanteErr: String? = null
        var tipoErr: String? = null
        var cantidadErr: String? = null

        // Meta 3: Validaciones (campo obligatorio + regla propia de negocio)
        if (currentState.codigoMuestra.isBlank()) {
            codigoErr = "El código de muestra es obligatorio"
            hasError = true
        }

        if (currentState.solicitante.isBlank()) {
            solicitanteErr = "El nombre del solicitante es obligatorio"
            hasError = true
        }

        if (currentState.tipoEnsayo.isBlank()) {
            tipoErr = "Debe seleccionar un tipo de ensayo"
            hasError = true
        }

        val cantidadNum = currentState.cantidadText.toIntOrNull()
        if (cantidadNum == null || cantidadNum <= 0) {
            cantidadErr = "La cantidad debe ser un número entero mayor a 0"
            hasError = true
        }

        if (hasError) {
            _formUiState.update {
                it.copy(
                    codigoError = codigoErr,
                    solicitanteError = solicitanteErr,
                    tipoEnsayoError = tipoErr,
                    cantidadError = cantidadErr
                )
            }
            return false
        }

        // Si es válido, se crea y guarda la solicitud
        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val nuevaSolicitud = Solicitud(
            id = UUID.randomUUID().toString(),
            codigoMuestra = currentState.codigoMuestra.trim(),
            solicitante = currentState.solicitante.trim(),
            tipoEnsayo = currentState.tipoEnsayo,
            cantidadMuestras = cantidadNum!!,
            esUrgente = currentState.esUrgente,
            observaciones = currentState.observaciones.trim(),
            fechaRegistro = fechaActual
        )

        val nuevaLista = listOf(nuevaSolicitud) + _solicitudes.value
        _solicitudes.value = nuevaLista
        LocalPreferencesStorage.guardarSolicitudes(context, nuevaLista)

        _formUiState.update {
            SolicitudFormUiState(
                mensajeExito = "¡Solicitud '${nuevaSolicitud.codigoMuestra}' registrada exitosamente!",
                solicitudCreadaId = nuevaSolicitud.id
            )
        }

        return true
    }

    fun limpiarMensajeExito() {
        _formUiState.update { it.copy(mensajeExito = null, solicitudCreadaId = null) }
    }

    fun getSolicitudById(id: String): Solicitud? {
        return _solicitudes.value.find { it.id == id }
    }
}
