package com.proyect.myhotelnet.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.proyect.myhotelnet.InicioActivity
import com.proyect.myhotelnet.R
import com.proyect.myhotelnet.data.AppDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedFragment : Fragment(R.layout.fragment_feed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = (activity as InicioActivity).session
        val db      = AppDatabaseHelper(requireContext())

        cargarStories(view, db)
        cargarPublicaciones(view, db)

        view.findViewById<ImageView>(R.id.ivPublicar).setOnClickListener {
            val tiet  = view.findViewById<TextInputEditText>(R.id.tietPublicacion)
            val texto = tiet.text.toString().trim()
            if (texto.isEmpty()) {
                Toast.makeText(requireContext(), "Escribe algo antes de publicar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE")).format(Date())
            db.insertarPublicacion(texto, "", fecha, session.getId())
            tiet.text?.clear()
            cargarPublicaciones(view, db)
            Toast.makeText(requireContext(), "¡Publicado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarStories(view: View, db: AppDatabaseHelper) {
        val llStories = view.findViewById<LinearLayout>(R.id.llStories)
        llStories.removeAllViews()
        val colores = listOf("#1A7A6E", "#7180BC", "#E67E22", "#E74C3C", "#8E44AD")
        val pubs = db.getPublicaciones()
        val autores = pubs.distinctBy { it["nombres"] }.take(5)
        autores.forEachIndexed { i, p ->
            val item = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.CENTER
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.marginEnd = 12.dpToPx()
                layoutParams = lp
            }
            val card = CardView(requireContext()).apply {
                radius = 26f.dpToPx()
                setCardBackgroundColor(android.graphics.Color.parseColor(colores[i % colores.size]))
                cardElevation = 0f
                val lp = LinearLayout.LayoutParams(52.dpToPx(), 52.dpToPx())
                lp.bottomMargin = 4.dpToPx()
                layoutParams = lp
            }
            val tv = TextView(requireContext()).apply {
                val inicial = "${p["nombres"]?.firstOrNull() ?: ""}${p["apellido"]?.firstOrNull() ?: ""}".uppercase()
                text = inicial
                textSize = 14f
                setTextColor(android.graphics.Color.WHITE)
                gravity = android.view.Gravity.CENTER
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
            card.addView(tv)
            val tvNombre = TextView(requireContext()).apply {
                val nombre = p["nombres"] ?: ""
                text = if (nombre.length > 6) nombre.take(6) + "." else nombre
                textSize = 9f
                setTextColor(android.graphics.Color.parseColor("#888888"))
            }
            item.addView(card)
            item.addView(tvNombre)
            llStories.addView(item)
        }
    }

    private fun cargarPublicaciones(view: View, db: AppDatabaseHelper) {
        val llPosts = view.findViewById<LinearLayout>(R.id.llPosts)
        llPosts.removeAllViews()
        db.getPublicaciones().forEach { p ->
            val card = layoutInflater.inflate(R.layout.item_publicacion, llPosts, false)
            val inicial = "${p["nombres"]?.firstOrNull() ?: ""}${p["apellido"]?.firstOrNull() ?: ""}".uppercase()
            card.findViewById<TextView>(R.id.tvIniciales).text   = inicial
            card.findViewById<TextView>(R.id.tvAutor).text       = "${p["nombres"]} ${p["apellido"]}"
            card.findViewById<TextView>(R.id.tvUbicacion).text   = p["ubicacion"] ?: ""
            card.findViewById<TextView>(R.id.tvFecha).text       = p["fecha"] ?: ""
            card.findViewById<TextView>(R.id.tvDescripcion).text = p["descripcion"] ?: ""
            card.findViewById<TextView>(R.id.tvRolBadge).text    = if (p["rol"] == "hotelero") "Hotelero" else "Viajero"
            llPosts.addView(card)
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
    private fun Float.dpToPx(): Float = this * resources.displayMetrics.density
}
