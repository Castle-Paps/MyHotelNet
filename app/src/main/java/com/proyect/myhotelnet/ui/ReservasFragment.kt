package com.proyect.myhotelnet.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyect.myhotelnet.InicioActivity
import com.proyect.myhotelnet.R
import com.proyect.myhotelnet.adapter.ReservaAdapter
import com.proyect.myhotelnet.data.AppDatabaseHelper

class ReservasFragment : Fragment(R.layout.fragment_reservas) {

    private lateinit var rvReservas: RecyclerView
    private lateinit var adapter: ReservaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = (activity as InicioActivity).session
        val db      = AppDatabaseHelper(requireContext())

        rvReservas = view.findViewById(R.id.rvReservas)
        rvReservas.layoutManager = LinearLayoutManager(requireContext())

        adapter = ReservaAdapter(mutableListOf()) { reserva ->
            AlertDialog.Builder(requireContext())
                .setTitle("Reserva #${reserva["id"]}")
                .setMessage(
                    "Hotel: ${reserva["hotel"]}\n" +
                    "Ciudad: ${reserva["ciudad"]}\n" +
                    "Check-in: ${reserva["checkin"]}\n" +
                    "Check-out: ${reserva["checkout"]}\n" +
                    "Personas: ${reserva["personas"]}\n" +
                    "Total: S/ ${reserva["total"]}\n" +
                    "Estado: ${reserva["estado"]}"
                )
                .setPositiveButton("Cerrar", null)
                .show()
        }

        rvReservas.adapter = adapter
        adapter.actualizar(db.getReservasPorUsuario(session.getId()))
    }
}
