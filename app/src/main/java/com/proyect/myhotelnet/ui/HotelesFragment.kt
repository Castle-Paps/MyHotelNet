package com.proyect.myhotelnet.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.proyect.myhotelnet.InicioActivity
import com.proyect.myhotelnet.R
import com.proyect.myhotelnet.adapter.HotelAdapter
import com.proyect.myhotelnet.data.AppDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HotelesFragment : Fragment(R.layout.fragment_hoteles) {

    private lateinit var db: AppDatabaseHelper
    private lateinit var adapter: HotelAdapter
    private lateinit var llHoteles: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db        = AppDatabaseHelper(requireContext())
        llHoteles = view.findViewById(R.id.llHoteles)

        adapter = HotelAdapter(mutableListOf()) { hotel -> mostrarDialogReserva(hotel) }

        val tietBuscar = view.findViewById<TextInputEditText>(R.id.tietBuscar)
        val ivBuscar   = view.findViewById<ImageView>(R.id.ivBuscar)

        ivBuscar.setOnClickListener {
            cargarHoteles(tietBuscar.text.toString().trim())
        }

        tietBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { cargarHoteles(s.toString().trim()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        view.findViewById<TextView>(R.id.chipTodos).setOnClickListener {
            tietBuscar.text?.clear()
            cargarHoteles("")
        }

        cargarHoteles("")
    }

    private fun cargarHoteles(filtro: String) {
        llHoteles.removeAllViews()
        db.getHoteles(filtro).forEach { hotel ->
            val card = layoutInflater.inflate(R.layout.item_hotel, llHoteles, false)
            card.findViewById<TextView>(R.id.tvNombreHotel).text   = hotel["nombre"]
            card.findViewById<TextView>(R.id.tvEstrellasHotel).text= "★".repeat(hotel["estrellas"]?.toIntOrNull() ?: 0)
            card.findViewById<TextView>(R.id.tvCiudadHotel).text   = hotel["ciudad"]
            card.findViewById<TextView>(R.id.tvDescHotel).text     = hotel["descripcion"]
            card.findViewById<TextView>(R.id.tvPrecioHotel).text   = "S/ ${hotel["precio"]} / noche"
            card.findViewById<Button>(R.id.ivReservar).setOnClickListener { mostrarDialogReserva(hotel) }
            card.setOnClickListener { mostrarDialogReserva(hotel) }
            llHoteles.addView(card)
        }
    }

    private fun mostrarDialogReserva(hotel: Map<String, String>) {
        val session    = (activity as InicioActivity).session
        val dialogView = layoutInflater.inflate(R.layout.dialog_reserva, null)

        dialogView.findViewById<TextView>(R.id.tvTituloReserva).text = hotel["nombre"]

        val tietCheckin  = dialogView.findViewById<TextInputEditText>(R.id.tietCheckin)
        val tietCheckout = dialogView.findViewById<TextInputEditText>(R.id.tietCheckout)
        val tietPersonas = dialogView.findViewById<TextInputEditText>(R.id.tietPersonas)
        val tvTotal      = dialogView.findViewById<TextView>(R.id.tvTotalReserva)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)
        val btnCancelar  = dialogView.findViewById<Button>(R.id.btnCancelarReserva)
        val precio       = hotel["precio"]?.toIntOrNull() ?: 0

        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        tietPersonas.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val p = s.toString().toIntOrNull() ?: 0
                tvTotal.text = "Total estimado: S/ ${precio * p} (por noche)"
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })

        btnConfirmar.setOnClickListener {
            val checkin  = tietCheckin.text.toString().trim()
            val checkout = tietCheckout.text.toString().trim()
            val personas = tietPersonas.text.toString().toIntOrNull() ?: 0

            if (checkin.isEmpty() || checkout.isEmpty() || personas == 0) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = precio * personas
            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale("es", "PE")).format(Date())
            val id    = db.insertarReserva(session.getId(), hotel["id"]!!.toInt(),
                checkin, checkout, personas, total, fecha)

            if (id != -1L) {
                Toast.makeText(requireContext(), "¡Reserva #$id confirmada!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Error al reservar", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}
