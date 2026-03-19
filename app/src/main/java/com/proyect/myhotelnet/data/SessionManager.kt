package com.proyect.myhotelnet.data

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("hotelnet_session", Context.MODE_PRIVATE)

    fun guardar(id: Int, usuario: String, nombres: String, apellido: String, rol: String) {
        prefs.edit()
            .putInt("id", id).putString("usuario", usuario)
            .putString("nombres", nombres).putString("apellido", apellido)
            .putString("rol", rol).putBoolean("logueado", true)
            .apply()
    }

    fun cerrar() = prefs.edit().clear().apply()
    fun logueado()   = prefs.getBoolean("logueado", false)
    fun getId()      = prefs.getInt("id", 0)
    fun getUsuario() = prefs.getString("usuario", "") ?: ""
    fun getNombres() = prefs.getString("nombres", "") ?: ""
    fun getApellido()= prefs.getString("apellido", "") ?: ""
    fun getRol()     = prefs.getString("rol", "viajero") ?: "viajero"
    fun esHotelero() = getRol() == "hotelero"
    fun getNombreCompleto() = "${getNombres()} ${getApellido()}"
    fun getIniciales(): String {
        val n = getNombres(); val a = getApellido()
        return "${n.firstOrNull() ?: ""}${a.firstOrNull() ?: ""}".uppercase()
    }
}
