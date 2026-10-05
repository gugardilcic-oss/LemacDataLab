package com.example.lemacdatalab.data.model

object Ambitos {
    const val NEURODESARROLLO = "Neurodesarrollo"
    const val HABILIDADES_DBT = "Habilidades DBT"
    const val ESTADO_ANIMO = "Estado de Ánimo"
    const val ADICCIONES = "Adicciones"

    val listaAmbitos = listOf(
        NEURODESARROLLO,
        HABILIDADES_DBT,
        ESTADO_ANIMO,
        ADICCIONES
    )

    fun getActividadesSugeridas(ambito: String): List<String> {
        return when (ambito) {
            NEURODESARROLLO -> listOf(
                "Rutina de organización de tareas",
                "Pausa de autorregulación",
                "Estrategia sensorial aplicada",
                "Manejo de sobrecarga / descanso"
            )
            HABILIDADES_DBT -> listOf(
                "Aceptación Radical",
                "Mente Sabia",
                "Habilidad STOP / Pausa reflexiva",
                "TIPP (Cambio fisiológico)",
                "Eficacia Interpersonal"
            )
            ESTADO_ANIMO -> listOf(
                "Check-in: Calma / Tranquilidad",
                "Check-in: Ansiedad / Tensión",
                "Check-in: Tristeza / Desánimo",
                "Check-in: Entusiasmo / Motivación"
            )
            ADICCIONES -> listOf(
                "Control de Craving / Impulso",
                "Identificación de Desencadenante",
                "Acción Protectora Activada",
                "Cumplimiento de Meta de Autocuidado"
            )
            else -> listOf("Registro General de Autocuidado")
        }
    }
}
