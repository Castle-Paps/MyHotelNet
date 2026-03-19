package com.proyect.myhotelnet

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.proyect.myhotelnet.data.AppDatabaseHelper

class Register : AppCompatActivity() {

    private var rolSeleccionado = "viajero"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        val tilUsuario  = findViewById<TextInputLayout>(R.id.tilUsuario)
        val tilApPat    = findViewById<TextInputLayout>(R.id.tilApellidoPaterno)
        val tilNombres  = findViewById<TextInputLayout>(R.id.tilNombres)
        val tilClave    = findViewById<TextInputLayout>(R.id.tilClave)
        val tietUsuario = findViewById<TextInputEditText>(R.id.tietUsuario)
        val tietApPat   = findViewById<TextInputEditText>(R.id.tietApellidoPaterno)
        val tietNombres = findViewById<TextInputEditText>(R.id.tietNombres)
        val tietClave   = findViewById<TextInputEditText>(R.id.tietClave)
        val cardViajero = findViewById<LinearLayout>(R.id.cardViajero)
        val cardHotelero= findViewById<LinearLayout>(R.id.cardHotelero)
        val chkTyc      = findViewById<MaterialCheckBox>(R.id.chkTyc)
        val btnGuardar  = findViewById<Button>(R.id.btnGuardar)
        val tvIrLogin   = findViewById<TextView>(R.id.tvIrLogin)
        val db          = AppDatabaseHelper(this)

        cardViajero.setOnClickListener {
            rolSeleccionado = "viajero"
            cardViajero.setBackgroundResource(R.drawable.bg_rol_card_active)
            cardHotelero.setBackgroundResource(R.drawable.bg_rol_card_inactive)
        }
        cardHotelero.setOnClickListener {
            rolSeleccionado = "hotelero"
            cardHotelero.setBackgroundResource(R.drawable.bg_rol_card_active)
            cardViajero.setBackgroundResource(R.drawable.bg_rol_card_inactive)
        }

        btnGuardar.setOnClickListener {
            val usuario  = tietUsuario.text.toString().trim()
            val apellido = tietApPat.text.toString().trim()
            val nombres  = tietNombres.text.toString().trim()
            val clave    = tietClave.text.toString().trim()
            var error    = false

            if (usuario.isEmpty())         { tilUsuario.error = "Requerido"; error = true } else tilUsuario.error = ""
            if (usuario.contains(" "))     { tilUsuario.error = "Sin espacios"; error = true }
            if (apellido.isEmpty())        { tilApPat.error   = "Requerido"; error = true } else tilApPat.error   = ""
            if (nombres.isEmpty())         { tilNombres.error = "Requerido"; error = true } else tilNombres.error = ""
            if (clave.length < 4)          { tilClave.error   = "Mínimo 4 caracteres"; error = true } else tilClave.error = ""
            if (!chkTyc.isChecked)         { Toast.makeText(this, "Acepta los términos", Toast.LENGTH_SHORT).show(); error = true }
            if (db.usuarioExiste(usuario)) { tilUsuario.error = "Ese usuario ya existe"; error = true }

            if (error) return@setOnClickListener

            val exito = db.registrarUsuario(usuario, clave, nombres, apellido, "", "", "N", rolSeleccionado)
            if (exito) {
                Toast.makeText(this, "¡Cuenta creada! Inicia sesión con: $usuario", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Error al crear cuenta", Toast.LENGTH_SHORT).show()
            }
        }

        tvIrLogin.setOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, maxOf(sys.bottom, ime.bottom))
            insets
        }
    }
}
