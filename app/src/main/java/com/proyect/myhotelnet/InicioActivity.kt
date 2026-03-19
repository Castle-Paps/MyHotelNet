package com.proyect.myhotelnet

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.proyect.myhotelnet.data.SessionManager
import com.proyect.myhotelnet.ui.CamaraFragment
import com.proyect.myhotelnet.ui.FeedFragment
import com.proyect.myhotelnet.ui.HotelesFragment
import com.proyect.myhotelnet.ui.NoticiasFragment
import com.proyect.myhotelnet.ui.PerfilFragment

class InicioActivity : AppCompatActivity() {

    lateinit var session: SessionManager
    private var navSeleccionada = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        session = SessionManager(this)

        val navNoticias = findViewById<LinearLayout>(R.id.navNoticias)
        val navFeed     = findViewById<LinearLayout>(R.id.navFeed)
        val navHoteles  = findViewById<LinearLayout>(R.id.navHoteles)
        val navPerfil   = findViewById<LinearLayout>(R.id.navPerfil)
        val fabCrear    = findViewById<FrameLayout>(R.id.fabCrear)

        navNoticias.setOnClickListener { seleccionarNav(0); reemplazarFragment(NoticiasFragment()) }
        navFeed.setOnClickListener     { seleccionarNav(1); reemplazarFragment(FeedFragment()) }
        navHoteles.setOnClickListener  { seleccionarNav(3); reemplazarFragment(HotelesFragment()) }
        navPerfil.setOnClickListener   { seleccionarNav(4); reemplazarFragment(PerfilFragment()) }
        fabCrear.setOnClickListener    { reemplazarFragment(CamaraFragment()) }

        seleccionarNav(0)
        reemplazarFragment(NoticiasFragment())
    }

    private fun seleccionarNav(index: Int) {
        navSeleccionada = index
        val teal  = ContextCompat.getColor(this, R.color.staynest_primary)
        val gris  = ContextCompat.getColor(this, R.color.plomo)

        val navs = listOf(
            Triple(R.id.icNoticias, R.id.tvNavNoticias, 0),
            Triple(R.id.icFeed,     R.id.tvNavFeed,     1),
            Triple(R.id.icHoteles,  R.id.tvNavHoteles,  3),
            Triple(R.id.icPerfil,   R.id.tvNavPerfil,   4)
        )
        navs.forEach { (icId, tvId, navIndex) ->
            val color = if (navIndex == index) teal else gris
            findViewById<ImageView>(icId).setColorFilter(color)
            findViewById<TextView>(tvId).setTextColor(color)
        }
    }

    fun reemplazarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flayContenedor, fragment)
            .commit()
    }
}
