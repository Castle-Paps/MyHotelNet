package com.proyect.myhotelnet.entity

data class Usuario(
    val id: Int = 0,
    val usuario: String,
    val clave: String,
    val nombres: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val celular: String = "",
    val sexo: String = "N",
    val rol: String = "viajero"
)
