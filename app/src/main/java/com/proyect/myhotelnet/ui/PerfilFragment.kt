package com.proyect.myhotelnet.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.proyect.myhotelnet.InicioActivity
import com.proyect.myhotelnet.Login
import com.proyect.myhotelnet.R
import com.proyect.myhotelnet.data.AppDatabaseHelper

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = (activity as InicioActivity).session
        val db      = AppDatabaseHelper(requireContext())

        view.findViewById<TextView>(R.id.tvAvatarPerfil).text  = session.getIniciales()
        view.findViewById<TextView>(R.id.tvNombrePerfil).text  = session.getNombreCompleto()
        view.findViewById<TextView>(R.id.tvUsuarioPerfil).text = "@${session.getUsuario()}"
        view.findViewById<TextView>(R.id.tvRolPerfil).text     =
            if (session.esHotelero()) "Hotelero" else "Viajero"

        val reservas = db.getReservasPorUsuario(session.getId())
        val badge    = view.findViewById<TextView>(R.id.tvBadgeReservas)
        badge.text   = if (reservas.isEmpty()) "Sin reservas" else "${reservas.size} activas"
        view.findViewById<TextView>(R.id.tvStatReservas).text = reservas.size.toString()

        view.findViewById<View>(R.id.itemEditarPerfil).setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.itemMisReservas).setOnClickListener {
            (activity as InicioActivity).reemplazarFragment(ReservasFragment())
        }
        view.findViewById<View>(R.id.itemNotificaciones).setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.itemCerrarSesion).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro que quieres salir?")
                .setPositiveButton("Salir") { _, _ ->
                    session.cerrar()
                    startActivity(Intent(requireContext(), Login::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
