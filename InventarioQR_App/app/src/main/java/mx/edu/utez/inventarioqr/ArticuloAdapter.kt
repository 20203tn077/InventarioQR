package mx.edu.utez.inventarioqr

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.edu.utez.inventarioqr.model.Articulo

class ArticuloAdapter(private val contexto: Context, private val layout: Int, private val datos: List<Articulo>) : RecyclerView.Adapter<ArticuloAdapter.ViewHolder>() {
    var onItemClick: ((Articulo) -> Unit)? = null
    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup, layout: Int) :
        RecyclerView.ViewHolder(inflater.inflate(layout, parent, false)) {
        var txtCodigo: TextView
        var txtNombre: TextView
        var txtDescripcion: TextView
        var txtCantidad: TextView
        var txtCategoria: TextView

        init {
            txtCodigo = itemView.findViewById(R.id.txtCodigo)
            txtNombre = itemView.findViewById(R.id.txtNombre)
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion)
            txtCantidad = itemView.findViewById(R.id.txtCantidad)
            txtCategoria = itemView.findViewById(R.id.txtCategoria)
            itemView.setOnClickListener {
                onItemClick?.invoke(datos[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent, layout)
    }

    override fun onBindViewHolder(holder: ArticuloAdapter.ViewHolder, position: Int) {
        holder.txtCodigo.text = datos[position].codigo.toString()
        holder.txtNombre.text = datos[position].nombre
        holder.txtDescripcion.text = datos[position].descripcion
        holder.txtCantidad.text = datos[position].cantidad.toString() + " disponibles"
        holder.txtCategoria.text = "En: " + datos[position].categoriaId.nombre
    }

    override fun getItemCount(): Int {
        return datos.size;
    }
}