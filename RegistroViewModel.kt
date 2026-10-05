package com.example.lemacdatalab.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.lemacdatalab.data.local.LocalPreferencesStorage
import com.example.lemacdatalab.data.model.Ambitos
import com.example.lemacdatalab.data.model.RegistroAutoReporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class RegistroViewModel(private val context: Context? = null) : ViewModel() {

    private val _formUiState = MutableStateFlow(RegistroFormUiState())
    val formUiState: StateFlow<RegistroFormUiState> = _formUiState.asStateFlow()

    private val _filtroAmbito = MutableStateFlow("Todos")
    val filtroAmbito: StateFlow<String> = _filtroAmbito.asStateFlow()

    // Carga persistente o datos iniciales para el MVP Lemac DataLab
    private val _registros = MutableStateFlow<List<RegistroAutoReporte>>(
        LocalPreferencesStorage.cargarRegistros(context)
    )
    val registros: StateFlow<List<RegistroAutoReporte>> = _registros.asStateFlow()

    // Manejadores de cambios en el formulario
    fun onAmbitoChanged(nuevoAmbito: String) {
        _formUiState.update {
            it.copy(
                ambitoSeleccionado = nuevoAmbito,
                tipoActividad = "", // Limpiar tipo al cambiar ámbito
                tipoActividadError = null,
                desencadenanteError = null
            )
        }
    }

    fun onTipoActividadChanged(tipo: String) {
        _formUiState.update { it.copy(tipoActividad = tipo, tipoActividadError = null) }
    }

    fun onNivelIntensidadChanged(intensidad: Float) {
        _formUiState.update { it.copy(nivelIntensidad = intensidad) }
    }

    fun onDesencadenanteChanged(desencadenante: String) {
        _formUiState.update { it.copy(desencadenanteEstrategia = desencadenante, desencadenanteError = null) }
    }

    fun onObservacionesChanged(obs: String) {
        _formUiState.update { it.copy(observacionesContexto = obs) }
    }

    fun setFiltroAmbito(filtro: String) {
        _filtroAmbito.value = filtro
    }

    // Validación de campos y reglas de negocio del MVP
    fun validarYGuardar(): Boolean {
        val currentState = _formUiState.value
        var hasError = false
        var tipoErr: String? = null
        var desencadenanteErr: String? = null

        // Meta 3: Validaciones del formulario
        // Validación 1: Tipo de actividad es obligatorio
        if (currentState.tipoActividad.isBlank()) {
            tipoErr = "Debe seleccionar o ingresar el tipo de actividad u habilidad"
            hasError = true
        }

        // Validación 2: Regla de dominio (Desencadenante/Estrategia requerida en DBT y Adicciones)
        if ((currentState.ambitoSeleccionado == Ambitos.HABILIDADES_DBT || currentState.ambitoSeleccionado == Ambitos.ADICCIONES) &&
            currentState.desencadenanteEstrategia.isBlank()
        ) {
            desencadenanteErr = "Para ${currentState.ambitoSeleccionado} es obligatorio registrar la situación, desencadenante o acción protectora"
            hasError = true
        }

        if (hasError) {
            _formUiState.update {
                it.copy(
                    tipoActividadError = tipoErr,
                    desencadenanteError = desencadenanteErr
                )
            }
            return false
        }

        // Crear nuevo registro de autorreporte sintético
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val nuevoRegistro = RegistroAutoReporte(
            id = UUID.randomUUID().toString(),
            aliasUsuario = currentState.aliasUsuario,
            ambito = currentState.ambitoSeleccionado,
            tipoActividad = currentState.tipoActividad.trim(),
            nivelIntensidad = currentState.nivelIntensidad.toInt(),
            desencadenanteEstrategia = currentState.desencadenanteEstrategia.trim(),
            observacionesContexto = currentState.observacionesContexto.trim(),
            fechaHora = fechaActual
        )

        val nuevaLista = listOf(nuevoRegistro) + _registros.value
        _registros.value = nuevaLista
        LocalPreferencesStorage.guardarRegistros(context, nuevaLista)

        _formUiState.update {
            RegistroFormUiState(
                ambitoSeleccionado = currentState.ambitoSeleccionado,
                mensajeExito = "¡Registro en '${nuevoRegistro.ambito}' guardado exitosamente!",
                registroCreadoId = nuevoRegistro.id
            )
        }

        return true
    }

    fun eliminarRegistro(id: String) {
        val nuevaLista = _registros.value.filterNot { it.id == id }
        _registros.value = nuevaLista
        LocalPreferencesStorage.guardarRegistros(context, nuevaLista)
    }

    fun limpiarMensajeExito() {
        _formUiState.update { it.copy(mensajeExito = null, registroCreadoId = null) }
    }

    fun getRegistroById(id: String): RegistroAutoReporte? {
        return _registros.value.find { it.id == id }
    }
}
