package com.example.lemacdatalab.data.model

data class Solicitud(
    val id: String,
    val codigoMuestra: String,
    val solicitante: String,
    val tipoEnsayo: String,
    val cantidadMuestras: Int,
    val esUrgente: Boolean,
    val observaciones: String,
    val fechaRegistro: String
)

object TiposEnsayo {
    val opciones = listOf(
        "Hormigón / Cemento",
        "Suelos / Geotecnia",
        "Asfalto / Mezclas Bituminosas",
        "Acero / Estructuras"
    )
}
