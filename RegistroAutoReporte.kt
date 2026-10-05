package com.example.lemacdatalab.data.model

data class RegistroAutoReporte(
    val id: String,
    val aliasUsuario: String,
    val ambito: String,
    val tipoActividad: String,
    val nivelIntensidad: Int, // Escala de 1 a 10
    val desencadenanteEstrategia: String,
    val observacionesContexto: String,
    val fechaHora: String
)
