package com.proyect.myhotelnet.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.proyect.myhotelnet.InicioActivity
import com.proyect.myhotelnet.R
import com.proyect.myhotelnet.data.AppDatabaseHelper

class NoticiasFragment : Fragment(R.layout.fragment_noticias) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = (activity as InicioActivity).session
        val db      = AppDatabaseHelper(requireContext())
        val noticias = db.getNoticias()

        val btnPublicar = view.findViewById<Button>(R.id.btnPublicarNoticia)
        btnPublicar.visibility = if (session.esHotelero()) View.VISIBLE else View.GONE

        btnPublicar.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }

        if (noticias.isNotEmpty()) {
            view.findViewById<TextView>(R.id.tvTituloDestacado).text   = noticias[0]["titulo"]
            view.findViewById<TextView>(R.id.tvSubtituloDestacado).text =
                "${noticias[0]["hotel"]} · ${noticias[0]["fecha"]}"
        }

        val llNoticias = view.findViewById<LinearLayout>(R.id.llNoticias)
        llNoticias.removeAllViews()
        noticias.forEach { n ->
            val card = layoutInflater.inflate(R.layout.item_noticia, llNoticias, false)
            card.findViewById<TextView>(R.id.tvCategoriaNoticia).text  = n["categoria"] ?: ""
            card.findViewById<TextView>(R.id.tvFechaNoticia).text      = n["fecha"] ?: ""
            card.findViewById<TextView>(R.id.tvTituloNoticia).text     = n["titulo"] ?: ""
            card.findViewById<TextView>(R.id.tvContenidoNoticia).text  = n["contenido"] ?: ""
            card.findViewById<TextView>(R.id.tvHotelNoticia).text      = "● ${n["hotel"]}"
            llNoticias.addView(card)
        }
    }
}
