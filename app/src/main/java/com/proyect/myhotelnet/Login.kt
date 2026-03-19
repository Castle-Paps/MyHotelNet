package com.proyect.myhotelnet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.proyect.myhotelnet.data.AppDatabaseHelper
import com.proyect.myhotelnet.data.SessionManager

class Login : AppCompatActivity() {

    private lateinit var tilUsuario: TextInputLayout
    private lateinit var tilClave: TextInputLayout
    private lateinit var tietUsuario: TextInputEditText
    private lateinit var tietClave: TextInputEditText
    private lateinit var btnIngresar: Button
    private lateinit var tvRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val session = SessionManager(this)
        if (session.logueado()) {
            startActivity(Intent(this, InicioActivity::class.java))
            finish(); return
        }

        setContentView(R.layout.activity_acceso)

        tilUsuario  = findViewById(R.id.tilUsuario)
        tilClave    = findViewById(R.id.tilClave)
        tietUsuario = findViewById(R.id.tietUsuario)
        tietClave   = findViewById(R.id.tietClave)
        btnIngresar = findViewById(R.id.btnIngresar)
        tvRegistro  = findViewById(R.id.tvRegistro)

        val db = AppDatabaseHelper(this)

        btnIngresar.setOnClickListener { validarCampos(db, session) }

        tvRegistro.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, maxOf(sys.bottom, ime.bottom))
            insets
        }
    }

    private fun validarCampos(db: AppDatabaseHelper, session: SessionManager) {
        val usuario = tietUsuario.text.toString().trim()
        val clave   = tietClave.text.toString().trim()
        var error   = false

        if (usuario.isEmpty()) { tilUsuario.error = "Ingresa tu usuario"; error = true }
        else tilUsuario.error = ""

        if (clave.isEmpty()) { tilClave.error = "Ingresa tu contraseña"; error = true }
        else tilClave.error = ""

        if (error) return

        Toast.makeText(this, "Verificando...", Toast.LENGTH_SHORT).show()

        val resultado = db.login(usuario, clave)
        if (resultado != null) {
            session.guardar(
                id       = resultado["id"]!!.toInt(),
                usuario  = usuario,
                nombres  = resultado["nombres"]!!,
                apellido = resultado["apellido"]!!,
                rol      = resultado["rol"]!!
            )
            Toast.makeText(this, "¡Bienvenido, ${resultado["nombres"]}!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, InicioActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
        }
    }
}
