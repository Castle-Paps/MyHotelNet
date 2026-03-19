package com.proyect.myhotelnet.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "hotelnet.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuario (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                usuario TEXT UNIQUE NOT NULL,
                clave TEXT NOT NULL,
                nombres TEXT NOT NULL,
                apellido_paterno TEXT NOT NULL,
                apellido_materno TEXT NOT NULL,
                celular TEXT,
                sexo TEXT DEFAULT 'N',
                rol TEXT DEFAULT 'viajero'
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE hotel (
                id_hotel INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL,
                ciudad TEXT NOT NULL,
                direccion TEXT,
                descripcion TEXT,
                precio_noche INTEGER NOT NULL,
                estrellas INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE reserva (
                id_reserva INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_usuario INTEGER NOT NULL,
                id_hotel INTEGER NOT NULL,
                fecha_checkin TEXT NOT NULL,
                fecha_checkout TEXT NOT NULL,
                num_personas INTEGER NOT NULL,
                total INTEGER NOT NULL,
                estado TEXT DEFAULT 'pendiente',
                fecha_reserva TEXT NOT NULL,
                FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
                FOREIGN KEY (id_hotel) REFERENCES hotel (id_hotel)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE noticia (
                id_noticia INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                titulo TEXT NOT NULL,
                contenido TEXT NOT NULL,
                fecha TEXT NOT NULL,
                categoria TEXT NOT NULL,
                id_hotel INTEGER,
                FOREIGN KEY (id_hotel) REFERENCES hotel (id_hotel)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE publicacion (
                id_publicacion INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                descripcion TEXT NOT NULL,
                ubicacion TEXT,
                fecha TEXT NOT NULL,
                id_usuario INTEGER NOT NULL,
                FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
            )
        """.trimIndent())

        insertarDatosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS publicacion")
        db.execSQL("DROP TABLE IF EXISTS noticia")
        db.execSQL("DROP TABLE IF EXISTS reserva")
        db.execSQL("DROP TABLE IF EXISTS hotel")
        db.execSQL("DROP TABLE IF EXISTS usuario")
        onCreate(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        val usuarios = listOf(
            arrayOf("ana123",   "1234", "Ana",        "García",    "López",    "+51987654321", "F", "viajero"),
            arrayOf("carlos",   "1234", "Carlos",     "Mendoza",   "Ríos",     "+51912345678", "M", "viajero"),
            arrayOf("grandpalace", "hotel1", "Grand Palace", "Hotel",  "SAC",  "+51014567890", "N", "hotelero"),
            arrayOf("paraiso",  "hotel2", "Paraíso",   "Beach",     "Resort",   "+51015678901", "N", "hotelero")
        )
        usuarios.forEach { u ->
            val cv = ContentValues().apply {
                put("usuario", u[0]); put("clave", u[1]); put("nombres", u[2])
                put("apellido_paterno", u[3]); put("apellido_materno", u[4])
                put("celular", u[5]); put("sexo", u[6]); put("rol", u[7])
            }
            db.insert("usuario", null, cv)
        }

        val hoteles = listOf(
            arrayOf("Grand Palace Hotel",    "Miraflores, Lima",  "Av. Larco 345",         "Hotel de lujo con rooftop bar y vista al mar",          "280", "5"),
            arrayOf("Paraíso Beach Resort",  "Barranco, Lima",    "Jr. Grau 120",           "Resort de playa con piscina infinita y spa",            "195", "4"),
            arrayOf("Heimen Nordisk",        "Cusco, Perú",       "Plaza Mayor 10",         "Hotel boutique con vista a la Plaza Mayor del Cusco",   "220", "4"),
            arrayOf("La Rioja Suites",       "Arequipa, Perú",    "Calle Mercaderes 55",    "Hotel céntrico con desayuno incluido",                  "160", "3"),
            arrayOf("Selva Verde Lodge",     "Iquitos, Perú",     "Río Amazonas km 5",      "Lodge en la selva amazónica con tours de naturaleza",   "310", "5")
        )
        hoteles.forEach { h ->
            val cv = ContentValues().apply {
                put("nombre", h[0]); put("ciudad", h[1]); put("direccion", h[2])
                put("descripcion", h[3]); put("precio_noche", h[4].toInt()); put("estrellas", h[5].toInt())
            }
            db.insert("hotel", null, cv)
        }

        val noticias = listOf(
            arrayOf("Nuevo rooftop bar con vista al mar",        "Inauguramos nuestro esperado rooftop bar, abierto de 6pm a 2am con cócteles de autor.", "hace 2h",   "Apertura", "1"),
            arrayOf("Paquetes familiares con 30% de descuento",  "Esta temporada ofrecemos increíbles paquetes para familias con desayuno buffet incluido.", "hace 5h",   "Oferta",   "2"),
            arrayOf("Renovación de suites premium completada",   "Nuestras suites del piso 8 han sido completamente renovadas con jacuzzi privado.",       "ayer",      "Novedad",  "1"),
            arrayOf("Festival gastronómico este fin de semana",  "Los chefs del restaurante preparan un festival de sabores del mar. Entrada gratis.",      "hace 2 días","Evento",  "2")
        )
        noticias.forEach { n ->
            val cv = ContentValues().apply {
                put("titulo", n[0]); put("contenido", n[1]); put("fecha", n[2])
                put("categoria", n[3]); put("id_hotel", n[4].toInt())
            }
            db.insert("noticia", null, cv)
        }

        val publicaciones = listOf(
            arrayOf("Fin de semana increíble en el Grand Palace. La vista desde el rooftop al atardecer es espectacular!", "Miraflores, Lima",  "hace 3h",    "1"),
            arrayOf("Primera vez en Paraíso Beach Resort. La piscina infinita con vista al mar es lo más hermoso que vi.", "Barranco, Lima",    "hace 6h",    "2"),
            arrayOf("De Lima a Cusco! Heimen Nordisk tiene una vista increíble a la Plaza Mayor. Definitivamente volvería.", "Cusco, Perú",    "ayer",       "1"),
            arrayOf("Recomiendo totalmente La Rioja Suites. Excelente relación precio-calidad y el desayuno era delicioso.", "Arequipa, Perú", "hace 2 días", "2")
        )
        publicaciones.forEach { p ->
            val cv = ContentValues().apply {
                put("descripcion", p[0]); put("ubicacion", p[1])
                put("fecha", p[2]); put("id_usuario", p[3].toInt())
            }
            db.insert("publicacion", null, cv)
        }
    }

    fun login(usuario: String, clave: String): Map<String, String>? {
        val db = readableDatabase
        val c = db.rawQuery(
            "SELECT id_usuario, nombres, apellido_paterno, rol FROM usuario WHERE usuario=? AND clave=?",
            arrayOf(usuario, clave)
        )
        return if (c.moveToFirst()) {
            val r = mapOf(
                "id"      to c.getInt(0).toString(),
                "nombres" to c.getString(1),
                "apellido" to c.getString(2),
                "rol"     to c.getString(3)
            )
            c.close(); r
        } else { c.close(); null }
    }

    fun usuarioExiste(usuario: String): Boolean {
        val c = readableDatabase.rawQuery("SELECT id_usuario FROM usuario WHERE usuario=?", arrayOf(usuario))
        val e = c.moveToFirst(); c.close(); return e
    }

    fun registrarUsuario(usuario: String, clave: String, nombres: String,
                         apellidoPaterno: String, apellidoMaterno: String,
                         celular: String, sexo: String, rol: String): Boolean {
        return try {
            val cv = ContentValues().apply {
                put("usuario", usuario); put("clave", clave); put("nombres", nombres)
                put("apellido_paterno", apellidoPaterno); put("apellido_materno", apellidoMaterno)
                put("celular", celular); put("sexo", sexo); put("rol", rol)
            }
            writableDatabase.insert("usuario", null, cv) != -1L
        } catch (e: Exception) { false }
    }

    fun getHoteles(filtro: String = ""): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val query = if (filtro.isEmpty())
            "SELECT * FROM hotel ORDER BY estrellas DESC"
        else
            "SELECT * FROM hotel WHERE nombre LIKE '%$filtro%' OR ciudad LIKE '%$filtro%'"
        val c = readableDatabase.rawQuery(query, null)
        while (c.moveToNext()) {
            lista.add(mapOf(
                "id"          to c.getInt(c.getColumnIndexOrThrow("id_hotel")).toString(),
                "nombre"      to c.getString(c.getColumnIndexOrThrow("nombre")),
                "ciudad"      to c.getString(c.getColumnIndexOrThrow("ciudad")),
                "direccion"   to c.getString(c.getColumnIndexOrThrow("direccion")),
                "descripcion" to c.getString(c.getColumnIndexOrThrow("descripcion")),
                "precio"      to c.getInt(c.getColumnIndexOrThrow("precio_noche")).toString(),
                "estrellas"   to c.getInt(c.getColumnIndexOrThrow("estrellas")).toString()
            ))
        }
        c.close(); return lista
    }

    fun insertarReserva(idUsuario: Int, idHotel: Int, checkin: String,
                        checkout: String, personas: Int, total: Int, fecha: String): Long {
        val cv = ContentValues().apply {
            put("id_usuario", idUsuario); put("id_hotel", idHotel)
            put("fecha_checkin", checkin); put("fecha_checkout", checkout)
            put("num_personas", personas); put("total", total)
            put("estado", "pendiente"); put("fecha_reserva", fecha)
        }
        return writableDatabase.insert("reserva", null, cv)
    }

    fun getReservasPorUsuario(idUsuario: Int): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val c = readableDatabase.rawQuery("""
            SELECT r.id_reserva, h.nombre, h.ciudad, r.fecha_checkin,
                   r.fecha_checkout, r.num_personas, r.total, r.estado, r.fecha_reserva
            FROM reserva r
            INNER JOIN hotel h ON r.id_hotel = h.id_hotel
            WHERE r.id_usuario = ?
            ORDER BY r.id_reserva DESC
        """.trimIndent(), arrayOf(idUsuario.toString()))
        while (c.moveToNext()) {
            lista.add(mapOf(
                "id"        to c.getInt(0).toString(),
                "hotel"     to c.getString(1),
                "ciudad"    to c.getString(2),
                "checkin"   to c.getString(3),
                "checkout"  to c.getString(4),
                "personas"  to c.getInt(5).toString(),
                "total"     to c.getInt(6).toString(),
                "estado"    to c.getString(7),
                "fecha"     to c.getString(8)
            ))
        }
        c.close(); return lista
    }

    fun getNoticias(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val c = readableDatabase.rawQuery("""
            SELECT n.id_noticia, n.titulo, n.contenido, n.fecha, n.categoria, h.nombre
            FROM noticia n
            INNER JOIN hotel h ON n.id_hotel = h.id_hotel
            ORDER BY n.id_noticia DESC
        """.trimIndent(), null)
        while (c.moveToNext()) {
            lista.add(mapOf(
                "id"        to c.getInt(0).toString(),
                "titulo"    to c.getString(1),
                "contenido" to c.getString(2),
                "fecha"     to c.getString(3),
                "categoria" to c.getString(4),
                "hotel"     to c.getString(5)
            ))
        }
        c.close(); return lista
    }

    fun getPublicaciones(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val c = readableDatabase.rawQuery("""
            SELECT p.id_publicacion, p.descripcion, p.ubicacion, p.fecha,
                   u.nombres, u.apellido_paterno, u.rol
            FROM publicacion p
            INNER JOIN usuario u ON p.id_usuario = u.id_usuario
            ORDER BY p.id_publicacion DESC
        """.trimIndent(), null)
        while (c.moveToNext()) {
            lista.add(mapOf(
                "id"          to c.getInt(0).toString(),
                "descripcion" to c.getString(1),
                "ubicacion"   to c.getString(2),
                "fecha"       to c.getString(3),
                "nombres"     to c.getString(4),
                "apellido"    to c.getString(5),
                "rol"         to c.getString(6)
            ))
        }
        c.close(); return lista
    }

    fun insertarPublicacion(descripcion: String, ubicacion: String, fecha: String, idUsuario: Int): Long {
        val cv = ContentValues().apply {
            put("descripcion", descripcion); put("ubicacion", ubicacion)
            put("fecha", fecha); put("id_usuario", idUsuario)
        }
        return writableDatabase.insert("publicacion", null, cv)
    }
}
