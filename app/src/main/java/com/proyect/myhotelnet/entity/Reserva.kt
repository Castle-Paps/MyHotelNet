package com.proyect.myhotelnet.entity

data class Reserva(
    val id: Int = 0,
    val hotel: String,
    val ciudad: String,
    val checkin: String,
    val checkout: String,
    val personas: Int,
    val total: Int,
    val estado: String,
    val fecha: String
)
