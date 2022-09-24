package br.edu.infnet.innotes.ui.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.Anotacao

class AnotacoesAdapter(listener: RecyclerViewItemListener) :
    RecyclerView.Adapter<AnotacoesAdapter.ViewHolder>() {

    private var listener = listener

    var listaAnotacao = ArrayList<Anotacao>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.linha_anotacoes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listaAnotacao[position], listener, position)
    }

    override fun getItemCount(): Int {
        return listaAnotacao.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(anotacao: Anotacao, itemListener: RecyclerViewItemListener, position: Int) {
            val tvListaData = itemView.findViewById<TextView>(R.id.tvListaData)
            val tvListaTitulo = itemView.findViewById<TextView>(R.id.tvListaTitulo)
            val tvListaTexto = itemView.findViewById<TextView>(R.id.tvListaTexto)

            tvListaData.text = anotacao.data
            tvListaTitulo.text = anotacao.titulo
            tvListaTexto.text = anotacao.texto

            val btDel = itemView.findViewById<ImageButton>(R.id.btDel)

            btDel.setOnClickListener {
                itemListener.itemDeletar(it,anotacao.id!!)
            }

        }
    }
}