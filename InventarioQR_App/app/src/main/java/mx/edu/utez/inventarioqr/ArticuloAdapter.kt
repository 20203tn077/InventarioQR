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
        var txtNombre: TextView
        var txtDescripcion: TextView

        init {
            txtNombre = itemView.findViewById(R.id.txtNombre)
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion)
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
        holder.txtNombre.text = datos[position].nombre
        holder.txtDescripcion.text = datos[position].descripcion
    }

    override fun getItemCount(): Int {
        return datos.size;
    }
}