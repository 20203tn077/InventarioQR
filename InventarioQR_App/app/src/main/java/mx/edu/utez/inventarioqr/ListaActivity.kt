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
import kotlinx.android.synthetic.main.activity_lista.*
import mx.edu.utez.inventarioqr.model.Articulo
import mx.edu.utez.inventarioqr.model.RespuestaArticulos
import mx.edu.utez.inventarioqr.service.Servicio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.42:8080/Integradora/InventarioQR/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<Servicio>(Servicio::class.java)

        var categoria = intent.getIntExtra("categoria", -1)
        val origen = intent.getIntExtra("origen", -1)

        var listener: ((Articulo) -> Unit)? = { articulo:Articulo ->
            val intent = Intent(this@ListaActivity, ArticuloActivity::class.java)
            intent.apply {
                putExtra("origen", 1)
                putExtra("codigo", articulo.codigo)
            }

            if (categoria != -1) intent.putExtra("categoria", categoria)

            startActivity(intent)
        }

        if (categoria != -1) {
            service.getArticulosByCategoria(categoria).enqueue(object : Callback<RespuestaArticulos> {
                override fun onResponse(
                    call: Call<RespuestaArticulos>,
                    response: Response<RespuestaArticulos>
                ) {
                    val datos = response?.body()
                    if (datos != null) {
                        if (datos.exitoso) {
                            val articulos = datos.respuesta
                            if (articulos.size > 0) {
                                val adaptador = ArticuloAdapter(this@ListaActivity, R.layout.layout_articulo, articulos)
                                adaptador.onItemClick = listener
                                rvArticulos.apply {
                                    layoutManager = LinearLayoutManager(this@ListaActivity, LinearLayoutManager.VERTICAL, false)
                                    setHasFixedSize(false)
                                    adapter = adaptador
                                }
                            } else {
                                val adaptador = TextAdapter(this@ListaActivity, R.layout.layout_categoria,listOf("No se encontraron artículos"))
                                rvArticulos.apply {
                                    layoutManager = LinearLayoutManager(this@ListaActivity, LinearLayoutManager.VERTICAL, false)
                                    setHasFixedSize(false)
                                    adapter = adaptador
                                }
                            }
                        } else {
                            Toast.makeText(this@ListaActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ListaActivity, if (origen == 1) MainActivity::class.java else CategoriasActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<RespuestaArticulos>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    Toast.makeText(this@ListaActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ListaActivity, if (origen == 1) MainActivity::class.java else CategoriasActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            })
        } else {
            service.getArticulos().enqueue(object : Callback<RespuestaArticulos> {
                override fun onResponse(call: Call<RespuestaArticulos>, response: Response<RespuestaArticulos>) {
                    val datos = response?.body()
                    if (datos != null) {
                        if (datos.exitoso) {
                            val articulos = datos.respuesta
                            if (articulos.size > 0) {
                                val adaptador = ArticuloAdapter(this@ListaActivity, R.layout.layout_articulo, articulos)
                                adaptador.onItemClick = listener
                                rvArticulos.apply {
                                    layoutManager = LinearLayoutManager(this@ListaActivity, LinearLayoutManager.VERTICAL, false)
                                    setHasFixedSize(false)
                                    adapter = adaptador
                                }
                            } else {
                                val adaptador = TextAdapter(this@ListaActivity, R.layout.layout_categoria,listOf("No se encontraron artículos"))
                                rvArticulos.apply {
                                    layoutManager = LinearLayoutManager(this@ListaActivity, LinearLayoutManager.VERTICAL, false)
                                    setHasFixedSize(false)
                                    adapter = adaptador
                                }
                            }
                        } else {
                            Toast.makeText(this@ListaActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ListaActivity, if (origen == 1) MainActivity::class.java else CategoriasActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<RespuestaArticulos>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    Toast.makeText(this@ListaActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ListaActivity, if (origen == 1) MainActivity::class.java else CategoriasActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_comun, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuHome -> {
                val intent = Intent(this@ListaActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}