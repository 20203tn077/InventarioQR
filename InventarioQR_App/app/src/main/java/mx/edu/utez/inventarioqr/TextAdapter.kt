package mx.edu.utez.inventarioqr

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextAdapter(private val contexto: Context, private val layout: Int, private val datos: List<String>) : RecyclerView.Adapter<TextAdapter.ViewHolder>() {
    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup, layout: Int) :
        RecyclerView.ViewHolder(inflater.inflate(layout, parent, false)) {
        var text: TextView

        init {
            text = itemView.findViewById(R.id.txtCategoria)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent, layout)
    }

    override fun onBindViewHolder(holder: TextAdapter.ViewHolder, position: Int) {
        holder.text.text = datos[position]
    }

    override fun getItemCount(): Int {
        return datos.size;
    }
}