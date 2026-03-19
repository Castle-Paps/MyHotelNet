package com.proyect.myhotelnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyect.myhotelnet.R

class HotelAdapter(
    private var lista: MutableList<Map<String, String>>,
    private val onReservar: (Map<String, String>) -> Unit
) : RecyclerView.Adapter<HotelAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre:    TextView  = view.findViewById(R.id.tvNombreHotel)
        val tvEstrellas: TextView  = view.findViewById(R.id.tvEstrellasHotel)
        val tvCiudad:    TextView  = view.findViewById(R.id.tvCiudadHotel)
        val tvDesc:      TextView  = view.findViewById(R.id.tvDescHotel)
        val tvPrecio:    TextView  = view.findViewById(R.id.tvPrecioHotel)
        val ivReservar:  ImageView = view.findViewById(R.id.ivReservar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hotel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val h = lista[position]
        holder.tvNombre.text    = h["nombre"] ?: ""
        holder.tvEstrellas.text = "★".repeat(h["estrellas"]?.toIntOrNull() ?: 0)
        holder.tvCiudad.text    = h["ciudad"] ?: ""
        holder.tvDesc.text      = h["descripcion"] ?: ""
        holder.tvPrecio.text    = "S/ ${h["precio"]} / noche"
        holder.ivReservar.setOnClickListener { onReservar(h) }
        holder.itemView.setOnClickListener   { onReservar(h) }
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Map<String, String>>) {
        lista.clear(); lista.addAll(nueva); notifyDataSetChanged()
    }
}
