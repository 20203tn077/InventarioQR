package mx.edu.utez.inventarioqr

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_articulo.*
import mx.edu.utez.inventarioqr.model.Articulo
import mx.edu.utez.inventarioqr.model.Respuesta
import mx.edu.utez.inventarioqr.model.RespuestaArticulo
import mx.edu.utez.inventarioqr.service.Servicio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticuloActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articulo)
        
        val codigo = intent.getLongExtra("codigo", -1L)
        val origen = intent.getIntExtra("origen", -1)
        var categoria = intent.getIntExtra("categoria", -1)
        lateinit var articulo: Articulo

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.42:8080/Integradora/InventarioQR/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<Servicio>(Servicio::class.java)
        
        if (codigo != -1L) {
            service.getArticulo(codigo).enqueue(object : Callback<RespuestaArticulo> {
                override fun onResponse(
                    call: Call<RespuestaArticulo>,
                    response: Response<RespuestaArticulo>
                ) {
                    val datos = response?.body()
                    if (datos != null) {
                        if (datos.exitoso) {
                            articulo = datos.respuesta
                            txtCodigo.text = articulo.codigo.toString()
                            txtNombre.text = articulo.nombre
                            txtDescripcion.text = articulo.descripcion
                            txtCategoria.text = articulo.categoriaId.nombre
                            txtCantidad.text = articulo.cantidad.toString()
                        } else {
                            Toast.makeText(this@ArticuloActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ArticuloActivity, if (origen == 1) MainActivity::class.java else ListaActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            if (categoria != -1) intent.putExtra("categoria", categoria)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<RespuestaArticulo>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    Toast.makeText(this@ArticuloActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ArticuloActivity, if (origen == 1) ListaActivity::class.java else MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

            })
        }

        btnEliminar.setOnClickListener {
            var builder = AlertDialog.Builder(this@ArticuloActivity)
            builder.apply {
                setTitle("Eliminar artículo")
                setMessage("¿Deseas eliminar el artículo: ${articulo.nombre}?")
                setPositiveButton("No") { dialog, button -> dialog.dismiss() }
                setNegativeButton("Sí") { dialog, button ->
                    run {
                        dialog.dismiss()
                        service.deleteArticulo(articulo.codigo)
                            .enqueue(object : Callback<Respuesta> {
                                override fun onResponse(
                                    call: Call<Respuesta>,
                                    response: Response<Respuesta>
                                ) {
                                    Toast.makeText(
                                        this@ArticuloActivity,
                                        "Se eliminó el artículo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@ArticuloActivity, if (origen == 1) ListaActivity::class.java else MainActivity::class.java)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                }

                                override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                                    Log.e("ERROR", t.message.toString())
                                    Toast.makeText(
                                        this@ArticuloActivity,
                                        "Fallo al llamar al servidor",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                }
                show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_comun, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuHome -> {
                val intent = Intent(this@ArticuloActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}