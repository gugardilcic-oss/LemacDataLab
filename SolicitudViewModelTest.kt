package com.example.lemacdatalab

import com.example.lemacdatalab.ui.viewmodel.SolicitudViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SolicitudViewModelTest {

    private lateinit var viewModel: SolicitudViewModel

    @Before
    fun setUp() {
        viewModel = SolicitudViewModel()
    }

    @Test
    fun `validarYGuardar con campos vacios retorna false y muestra errores`() {
        viewModel.onCodigoChanged("")
        viewModel.onSolicitanteChanged("")
        viewModel.onTipoEnsayoChanged("")
        viewModel.onCantidadChanged("0")

        val result = viewModel.validarYGuardar()

        assertFalse(result)
        val uiState = viewModel.formUiState.value
        assertNotNull(uiState.codigoError)
        assertNotNull(uiState.solicitanteError)
        assertNotNull(uiState.tipoEnsayoError)
        assertNotNull(uiState.cantidadError)
    }

    @Test
    fun `validarYGuardar con datos validos guarda la solicitud`() {
        val countBefore = viewModel.solicitudes.value.size

        viewModel.onCodigoChanged("MUESTRA-PRUEBA-01")
        viewModel.onSolicitanteChanged("Empresa Test")
        viewModel.onTipoEnsayoChanged("Hormigón / Cemento")
        viewModel.onCantidadChanged("5")
        viewModel.onUrgenteChanged(true)
        viewModel.onObservacionesChanged("Observación de prueba")

        val result = viewModel.validarYGuardar()

        assertTrue(result)
        val countAfter = viewModel.solicitudes.value.size
        assertEquals(countBefore + 1, countAfter)

        val uiState = viewModel.formUiState.value
        assertNull(uiState.codigoError)
        assertNotNull(uiState.mensajeExito)

        val creada = viewModel.getSolicitudById(uiState.solicitudCreadaId!!)
        assertNotNull(creada)
        assertEquals("MUESTRA-PRUEBA-01", creada?.codigoMuestra)
    }
}
