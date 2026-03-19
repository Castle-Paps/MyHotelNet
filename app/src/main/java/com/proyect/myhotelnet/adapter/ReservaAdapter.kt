package com.proyect.myhotelnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyect.myhotelnet.R

class ReservaAdapter(
    private var lista: MutableList<Map<String, String>>,
    private val onClick: (Map<String, String>) -> Unit
) : RecyclerView.Adapter<ReservaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId:     TextView = view.findViewById(R.id.tvIdReserva)
        val tvHotel:  TextView = view.findViewById(R.id.tvHotelReserva)
        val tvFechas: TextView = view.findViewById(R.id.tvFechasReserva)
        val tvTotal:  TextView = view.findViewById(R.id.tvTotalReserva)
        val tvEstado: TextView = view.findViewById(R.id.tvEstadoReserva)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = lista[position]
        holder.tvId.text     = "Reserva #${String.format("%03d", r["id"]?.toIntOrNull() ?: 0)}"
        holder.tvHotel.text  = "${r["hotel"]} — ${r["ciudad"]}"
        holder.tvFechas.text = "Check-in: ${r["checkin"]}  |  Check-out: ${r["checkout"]}"
        holder.tvTotal.text  = "Total: S/ ${r["total"]}"
        holder.tvEstado.text = r["estado"]?.replaceFirstChar { it.uppercase() } ?: ""
        holder.itemView.setOnClickListener { onClick(r) }
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Map<String, String>>) {
        lista.clear(); lista.addAll(nueva); notifyDataSetChanged()
    }
}
