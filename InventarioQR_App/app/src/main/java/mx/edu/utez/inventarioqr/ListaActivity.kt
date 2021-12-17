package mx.edu.utez.inventarioqr

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista.*
import mx.edu.utez.inventarioqr.model.Articulo
import mx.edu.utez.inventarioqr.model.Respuesta
import mx.edu.utez.inventarioqr.model.RespuestaArticulo
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
            .baseUrl("http://192.168.0.10:8080/InventarioQR_Backend_war/InventarioQR/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<Servicio>(Servicio::class.java)

        var listener: ((Articulo) -> Unit)? = { articulo:Articulo ->
            var builder = AlertDialog.Builder(this@ListaActivity)
            builder.apply {
                setTitle("Eliminar artículo")
                setMessage("¿Deseas eliminar el artículo: ${articulo.nombre}?")
                setPositiveButton("No", {dialog, button -> dialog.dismiss()})
                setNegativeButton("Sí", {dialog, button -> run{
                    dialog.dismiss()
                    service.deleteArticulo(articulo.codigo).enqueue(object: Callback<Respuesta> {
                        override fun onResponse(call: Call<Respuesta>, response: Response<Respuesta>) {
                            Toast.makeText(this@ListaActivity, "Se eliminó el artículo" , Toast.LENGTH_SHORT).show()
                            recreate()
                        }

                        override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                            Log.e("ERROR", t.message.toString())
                            Toast.makeText(this@ListaActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                        }

                    })
                }})
                show()
            }
        }

        var codigo = intent.getLongExtra("codigo", -1L)
        var categoria = intent.getIntExtra("categoria", -1)

        if (codigo != -1L) {
            service.getArticulo(codigo).enqueue(object : Callback<RespuestaArticulo> {
                override fun onResponse(
                    call: Call<RespuestaArticulo>,
                    response: Response<RespuestaArticulo>
                ) {
                    val datos = response?.body()
                    if (datos != null) {
                        if (datos.exitoso) {
                            val articulo = datos.respuesta
                            var articulos = mutableListOf<Articulo>()
                            articulos.add(articulo)

                            val adaptador = ArticuloAdapter(this@ListaActivity, R.layout.layout_articulo, articulos)
                            adaptador.onItemClick = listener
                            rvArticulos.apply {
                                layoutManager = LinearLayoutManager(this@ListaActivity, LinearLayoutManager.VERTICAL, false)
                                setHasFixedSize(false)
                                adapter = adaptador
                            }
                        } else {
                            Toast.makeText(this@ListaActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ListaActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<RespuestaArticulo>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    Toast.makeText(this@ListaActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ListaActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                }

            })
        } else if (categoria != -1) {
            service.getArticulosByCategoria(categoria).enqueue(object : Callback<RespuestaArticulos> {
                override fun onResponse(
                    call: Call<RespuestaArticulos>,
                    response: Response<RespuestaArticulos>
                ) {
                    val datos = response?.body()
                    if (datos != null) {
                        if (datos.exitoso) {
                            val articulos = datos.respuesta
                            val adaptador = ArticuloAdapter(this@ListaActivity, R.layout.layout_articulo, articulos)
                            adaptador.onItemClick = listener
                            rvArticulos.apply {
                                layoutManager = LinearLayoutManager(this@ListaActivity, LinearLayoutManager.VERTICAL, false)
                                setHasFixedSize(false)
                                adapter = adaptador
                            }
                        } else {
                            Toast.makeText(this@ListaActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ListaActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<RespuestaArticulos>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    Toast.makeText(this@ListaActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ListaActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

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
                            val adaptador = ArticuloAdapter(this@ListaActivity, R.layout.layout_articulo, articulos)
                            adaptador.onItemClick = listener
                            rvArticulos.apply {
                                layoutManager = LinearLayoutManager(this@ListaActivity, LinearLayoutManager.VERTICAL, false)
                                setHasFixedSize(false)
                                adapter = adaptador
                            }
                        } else {
                            Toast.makeText(this@ListaActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ListaActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<RespuestaArticulos>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    Toast.makeText(this@ListaActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ListaActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                }

            })
        }
    }
}