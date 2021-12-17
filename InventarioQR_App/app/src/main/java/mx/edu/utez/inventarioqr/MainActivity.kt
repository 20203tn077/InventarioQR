package mx.edu.utez.inventarioqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTodos.setOnClickListener(this@MainActivity)
        btnCategoria.setOnClickListener(this@MainActivity)
        btnCodigo.setOnClickListener(this@MainActivity)
        btnRegistrar.setOnClickListener(this@MainActivity)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnTodos -> {
                val intent = Intent(this@MainActivity, ListaActivity::class.java)
                startActivity(intent)
            }
            R.id.btnCategoria -> {
                val intent = Intent(this@MainActivity, CategoriasActivity::class.java)
                startActivity(intent)
            }
            R.id.btnCodigo -> {
                val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    .putExtra("destino", 1)
                startActivity(intent)
            }
            R.id.btnRegistrar -> {
                val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    .putExtra("destino", 2)
                startActivity(intent)
            }
        }
    }
}