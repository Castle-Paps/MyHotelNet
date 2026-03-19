package com.proyect.myhotelnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyect.myhotelnet.R

class PublicacionAdapter(private var lista: MutableList<Map<String, String>>)
    : RecyclerView.Adapter<PublicacionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIniciales:   TextView = view.findViewById(R.id.tvIniciales)
        val tvAutor:       TextView = view.findViewById(R.id.tvAutor)
        val tvUbicacion:   TextView = view.findViewById(R.id.tvUbicacion)
        val tvFecha:       TextView = view.findViewById(R.id.tvFecha)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvRolBadge:    TextView = view.findViewById(R.id.tvRolBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publicacion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        val nombre = "${p["nombres"]} ${p["apellido"]}"
        holder.tvIniciales.text   = "${p["nombres"]?.firstOrNull() ?: ""}${p["apellido"]?.firstOrNull() ?: ""}".uppercase()
        holder.tvAutor.text       = nombre
        holder.tvUbicacion.text   = p["ubicacion"] ?: ""
        holder.tvFecha.text       = p["fecha"] ?: ""
        holder.tvDescripcion.text = p["descripcion"] ?: ""
        holder.tvRolBadge.text    = if (p["rol"] == "hotelero") "Hotelero" else "Viajero"
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Map<String, String>>) {
        lista.clear(); lista.addAll(nueva); notifyDataSetChanged()
    }
}
