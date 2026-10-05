package com.example.lemacdatalab

import com.example.lemacdatalab.data.model.Ambitos
import com.example.lemacdatalab.ui.viewmodel.RegistroViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegistroViewModelTest {

    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setUp() {
        viewModel = RegistroViewModel()
    }

    @Test
    fun `validarYGuardar con tipoActividad vacia retorna false y muestra error`() {
        viewModel.onTipoActividadChanged("")
        val result = viewModel.validarYGuardar()

        assertFalse(result)
        assertNotNull(viewModel.formUiState.value.tipoActividadError)
    }

    @Test
    fun `validarYGuardar en DBT sin desencadenante retorna false`() {
        viewModel.onAmbitoChanged(Ambitos.HABILIDADES_DBT)
        viewModel.onTipoActividadChanged("STOP")
        viewModel.onDesencadenanteChanged("") // Vacío en DBT

        val result = viewModel.validarYGuardar()

        assertFalse(result)
        assertNotNull(viewModel.formUiState.value.desencadenanteError)
    }

    @Test
    fun `validarYGuardar con datos validos agrega registro correctamente`() {
        val countBefore = viewModel.registros.value.size

        viewModel.onAmbitoChanged(Ambitos.NEURODESARROLLO)
        viewModel.onTipoActividadChanged("Rutina de organización")
        viewModel.onNivelIntensidadChanged(8f)
        viewModel.onDesencadenanteChanged("Organización matutina")
        viewModel.onObservacionesChanged("Completado sin distracciones")

        val result = viewModel.validarYGuardar()

        assertTrue(result)
        val countAfter = viewModel.registros.value.size
        assertEquals(countBefore + 1, countAfter)

        val state = viewModel.formUiState.value
        assertNull(state.tipoActividadError)
        assertNotNull(state.mensajeExito)

        val nuevoRegistro = viewModel.getRegistroById(state.registroCreadoId!!)
        assertNotNull(nuevoRegistro)
        assertEquals("Rutina de organización", nuevoRegistro?.tipoActividad)
    }

    @Test
    fun `eliminarRegistro quita el elemento de la lista`() {
        val initialList = viewModel.registros.value
        val itemToDelete = initialList.first()

        viewModel.eliminarRegistro(itemToDelete.id)

        val newList = viewModel.registros.value
        assertEquals(initialList.size - 1, newList.size)
        assertNull(viewModel.getRegistroById(itemToDelete.id))
    }
}
