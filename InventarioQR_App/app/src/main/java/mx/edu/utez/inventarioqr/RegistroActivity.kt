package mx.edu.utez.inventarioqr

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registro.*
import mx.edu.utez.inventarioqr.model.Articulo
import mx.edu.utez.inventarioqr.model.Categoria
import mx.edu.utez.inventarioqr.model.Respuesta
import mx.edu.utez.inventarioqr.model.RespuestaCategorias
import mx.edu.utez.inventarioqr.service.Servicio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        var categorias = emptyList<Categoria>()

        txtCodigo.apply {
            setFocusable(false);
            setEnabled(false);
            setCursorVisible(false);
            setKeyListener(null);
            setBackgroundColor(Color.TRANSPARENT);
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.10:8080/InventarioQR_Backend_war/InventarioQR/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<Servicio>(Servicio::class.java)

        service.getCategorias().enqueue(object : Callback<RespuestaCategorias> {
            override fun onResponse(call: Call<RespuestaCategorias>, response: Response<RespuestaCategorias>) {
                val datos = response?.body()
                if (datos != null) {
                    if (datos.exitoso) {
                        categorias = datos.respuesta
                        var data = mutableListOf<String>()
                        for (i:Categoria in categorias) {
                            data.add(i.nombre)
                        }
                        spCategoria.adapter = ArrayAdapter(this@RegistroActivity, android.R.layout.simple_list_item_1, data)
                    } else {
                        Toast.makeText(this@RegistroActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<RespuestaCategorias>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                Toast.makeText(this@RegistroActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
            }

        })

        var codigo = intent.getLongExtra("codigo", -1L)

        if (codigo != -1L) {
            txtCodigo.setText(codigo.toString())
        } else {
            Toast.makeText(this@RegistroActivity, "Fallo al obtener el código" , Toast.LENGTH_SHORT).show()
            val intent = Intent(this@RegistroActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        btnRegistrar.setOnClickListener {
            if (txtCodigo.text.toString().isNotEmpty() && txtNombre.text.toString().isNotEmpty() && txtDescripcion.text.toString().isNotEmpty() && txtCantidad.text.toString().isNotEmpty()) {
                service.postArticulo(Articulo(codigo, txtNombre.text.toString(), txtDescripcion.text.toString(), categorias[spCategoria.selectedItemPosition], txtCantidad.text.toString().toInt())).enqueue(
                    object: Callback<Respuesta> {
                        override fun onResponse(
                            call: Call<Respuesta>,
                            response: Response<Respuesta>
                        ) {
                            val datos = response?.body()
                            if (datos != null) {
                                if (datos.exitoso) {
                                    Toast.makeText(this@RegistroActivity, "Articulo registrado", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this@RegistroActivity, datos.mensaje, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<Respuesta>, t: Throwable) {
                            Log.e("ERROR", t.message.toString())
                            Toast.makeText(this@RegistroActivity, "Fallo al llamar al servidor" , Toast.LENGTH_SHORT).show()
                        }

                    }
                )
            } else {
                Toast.makeText(this@RegistroActivity, "Debes ingresar todos los datos" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}