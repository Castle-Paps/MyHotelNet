package com.proyect.myhotelnet.entity

data class Hotel(
    val id: Int = 0,
    val nombre: String,
    val ciudad: String,
    val direccion: String = "",
    val descripcion: String = "",
    val precioNoche: Int,
    val estrellas: Int
)
