package com.example.lemacdatalab.data.local

import android.content.Context
import com.example.lemacdatalab.data.model.Ambitos
import com.example.lemacdatalab.data.model.RegistroAutoReporte
import com.example.lemacdatalab.data.model.Solicitud
import org.json.JSONArray
import org.json.JSONObject

object LocalPreferencesStorage {

    private const val PREFS_NAME = "lemac_datalab_prefs"
    private const val KEY_REGISTROS = "key_registros"
    private const val KEY_SOLICITUDES = "key_solicitudes"

    // Datos iniciales por defecto para el MVP
    val registrosIniciales = listOf(
        RegistroAutoReporte(
            id = "reg-1",
            aliasUsuario = "Usuario_Prueba_01",
            ambito = Ambitos.NEURODESARROLLO,
            tipoActividad = "Pausa de autorregulación",
            nivelIntensidad = 7,
            desencadenanteEstrategia = "Estrategia de respiración diafragmática de 5 minutos",
            observacionesContexto = "Lugar ruidoso en la tarde. Ayudó a reducir sobrecarga.",
            fechaHora = "28/09/2026 10:30"
        ),
        RegistroAutoReporte(
            id = "reg-2",
            aliasUsuario = "Usuario_Prueba_01",
            ambito = Ambitos.HABILIDADES_DBT,
            tipoActividad = "Aceptación Radical",
            nivelIntensidad = 8,
            desencadenanteEstrategia = "Frustración por retraso en proyecto personal",
            observacionesContexto = "Practicado ejercicio de mente sabia. Utilidad alta.",
            fechaHora = "27/09/2026 18:15"
        ),
        RegistroAutoReporte(
            id = "reg-3",
            aliasUsuario = "Usuario_Prueba_01",
            ambito = Ambitos.ESTADO_ANIMO,
            tipoActividad = "Check-in: Calma / Tranquilidad",
            nivelIntensidad = 6,
            desencadenanteEstrategia = "Caminata al aire libre",
            observacionesContexto = "Sensación de bienestar en nivel medio-alto.",
            fechaHora = "27/09/2026 09:00"
        ),
        RegistroAutoReporte(
            id = "reg-4",
            aliasUsuario = "Usuario_Prueba_01",
            ambito = Ambitos.ADICCIONES,
            tipoActividad = "Control de Craving / Impulso",
            nivelIntensidad = 6,
            desencadenanteEstrategia = "Estrés laboral ➔ Acción protectora: llamar a contacto de apoyo",
            observacionesContexto = "Se evitó el consumo impulsivo exitosamente.",
            fechaHora = "26/09/2026 21:45"
        )
    )

    val solicitudesIniciales = listOf(
        Solicitud(
            id = "sol-1",
            codigoMuestra = "MUESTRA-H-01",
            solicitante = "Constructora Andes Ltda.",
            tipoEnsayo = "Hormigón / Cemento",
            cantidadMuestras = 3,
            esUrgente = true,
            observaciones = "Ensayo de compresión a 28 días",
            fechaRegistro = "28/09/2026"
        ),
        Solicitud(
            id = "sol-2",
            codigoMuestra = "MUESTRA-S-02",
            solicitante = "Inmobiliaria Bío-Bío",
            tipoEnsayo = "Suelos / Geotecnia",
            cantidadMuestras = 5,
            esUrgente = false,
            observaciones = "Análisis granulométrico y proctor modificado",
            fechaRegistro = "27/09/2026"
        )
    )

    fun guardarRegistros(context: Context?, lista: List<RegistroAutoReporte>) {
        if (context == null) return
        val jsonArray = JSONArray()
        for (item in lista) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("aliasUsuario", item.aliasUsuario)
                put("ambito", item.ambito)
                put("tipoActividad", item.tipoActividad)
                put("nivelIntensidad", item.nivelIntensidad)
                put("desencadenanteEstrategia", item.desencadenanteEstrategia)
                put("observacionesContexto", item.observacionesContexto)
                put("fechaHora", item.fechaHora)
            }
            jsonArray.put(obj)
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_REGISTROS, jsonArray.toString()).apply()
    }

    fun cargarRegistros(context: Context?): List<RegistroAutoReporte> {
        if (context == null) return registrosIniciales
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_REGISTROS, null) ?: return registrosIniciales
        return try {
            val jsonArray = JSONArray(jsonString)
            val lista = mutableListOf<RegistroAutoReporte>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                lista.add(
                    RegistroAutoReporte(
                        id = obj.optString("id", ""),
                        aliasUsuario = obj.optString("aliasUsuario", ""),
                        ambito = obj.optString("ambito", ""),
                        tipoActividad = obj.optString("tipoActividad", ""),
                        nivelIntensidad = obj.optInt("nivelIntensidad", 5),
                        desencadenanteEstrategia = obj.optString("desencadenanteEstrategia", ""),
                        observacionesContexto = obj.optString("observacionesContexto", ""),
                        fechaHora = obj.optString("fechaHora", "")
                    )
                )
            }
            if (lista.isEmpty()) registrosIniciales else lista
        } catch (e: Exception) {
            registrosIniciales
        }
    }

    fun guardarSolicitudes(context: Context?, lista: List<Solicitud>) {
        if (context == null) return
        val jsonArray = JSONArray()
        for (item in lista) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("codigoMuestra", item.codigoMuestra)
                put("solicitante", item.solicitante)
                put("tipoEnsayo", item.tipoEnsayo)
                put("cantidadMuestras", item.cantidadMuestras)
                put("esUrgente", item.esUrgente)
                put("observaciones", item.observaciones)
                put("fechaRegistro", item.fechaRegistro)
            }
            jsonArray.put(obj)
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SOLICITUDES, jsonArray.toString()).apply()
    }

    fun cargarSolicitudes(context: Context?): List<Solicitud> {
        if (context == null) return solicitudesIniciales
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_SOLICITUDES, null) ?: return solicitudesIniciales
        return try {
            val jsonArray = JSONArray(jsonString)
            val lista = mutableListOf<Solicitud>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                lista.add(
                    Solicitud(
                        id = obj.optString("id", ""),
                        codigoMuestra = obj.optString("codigoMuestra", ""),
                        solicitante = obj.optString("solicitante", ""),
                        tipoEnsayo = obj.optString("tipoEnsayo", ""),
                        cantidadMuestras = obj.optInt("cantidadMuestras", 1),
                        esUrgente = obj.optBoolean("esUrgente", false),
                        observaciones = obj.optString("observaciones", ""),
                        fechaRegistro = obj.optString("fechaRegistro", "")
                    )
                )
            }
            if (lista.isEmpty()) solicitudesIniciales else lista
        } catch (e: Exception) {
            solicitudesIniciales
        }
    }
}
