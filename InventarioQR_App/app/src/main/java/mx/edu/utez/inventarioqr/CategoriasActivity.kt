package mx.edu.utez.inventarioqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_categorias.*
import kotlinx.android.synthetic.main.activity_registro.*
import mx.edu.utez.inventarioqr.model.Categoria
import mx.edu.utez.inventarioqr.model.RespuestaCategorias
import mx.edu.utez.inventarioqr.service.Servicio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        var categorias = emptyList<Categoria>()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.42:8080/Integradora/InventarioQR/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<Servicio>(Servicio::class.java)

        service.getCategorias().enqueue(object : Callback<RespuestaCategorias> {
            override fun onResponse(call: Call<RespuestaCategorias>, response: Response<RespuestaCategorias>) {
                val datos = response?.body()
                if (datos != null) {
                    if (datos.exitoso) {
                        categorias = datos.respuesta
                        val adaptador = CategoriaAdapter(this@CategoriasActivity, R.layout.layout_categoria, categorias)
                        rvCategorias.apply {
                            layoutManager = LinearLayoutManager(this@CategoriasActivity, LinearLayoutManager.VERTICAL, false)
                            setHasFixedSize(false)
                            adapter = adaptador
                        }

                        adaptador.onItemClick = { categoria ->
                            val intent = Intent(this@CategoriasActivity, ListaActivity::class.java)
                                .putExtra("origen", 2)
                                .putExtra("categoria", categoria.id)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@CategoriasActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CategoriasActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<RespuestaCategorias>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                Toast.makeText(this@CategoriasActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                val intent = Intent(this@CategoriasActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
            }

        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_comun, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuHome -> {
                val intent = Intent(this@CategoriasActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}