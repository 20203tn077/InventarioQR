package mx.edu.utez.inventarioqr

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.edu.utez.inventarioqr.model.Categoria

class CategoriaAdapter(private val contexto: Context, private val layout: Int, private val datos: List<Categoria>) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {
    var onItemClick: ((Categoria) -> Unit)? = null


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup, layout: Int) :
        RecyclerView.ViewHolder(inflater.inflate(layout, parent, false)) {
        var txtCategoria: TextView

        init {
            txtCategoria = itemView.findViewById(R.id.txtCategoria)
            itemView.setOnClickListener {
                onItemClick?.invoke(datos[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent, layout)
    }

    override fun onBindViewHolder(holder: CategoriaAdapter.ViewHolder, position: Int) {
        holder.txtCategoria.text = datos[position].nombre
    }

    override fun getItemCount(): Int {
        return datos.size;
    }
}