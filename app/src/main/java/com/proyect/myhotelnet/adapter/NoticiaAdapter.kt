package com.proyect.myhotelnet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyect.myhotelnet.R

class NoticiaAdapter(private var lista: MutableList<Map<String, String>>)
    : RecyclerView.Adapter<NoticiaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoriaNoticia)
        val tvFecha:     TextView = view.findViewById(R.id.tvFechaNoticia)
        val tvTitulo:    TextView = view.findViewById(R.id.tvTituloNoticia)
        val tvContenido: TextView = view.findViewById(R.id.tvContenidoNoticia)
        val tvHotel:     TextView = view.findViewById(R.id.tvHotelNoticia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noticia, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val n = lista[position]
        holder.tvCategoria.text = n["categoria"] ?: ""
        holder.tvFecha.text     = n["fecha"] ?: ""
        holder.tvTitulo.text    = n["titulo"] ?: ""
        holder.tvContenido.text = n["contenido"] ?: ""
        holder.tvHotel.text     = "● ${n["hotel"]}"
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Map<String, String>>) {
        lista.clear(); lista.addAll(nueva); notifyDataSetChanged()
    }
}
