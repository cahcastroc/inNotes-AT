package br.edu.infnet.innotes.ui.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.apiLivros.BuscaItem
import com.bumptech.glide.Glide


class LivrosAdapter(listener: RecyclerViewItemListener): RecyclerView.Adapter<LivrosAdapter.ViewHolder>() {


    private var listener = listener
    var listaLivros = listOf<BuscaItem>()
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(livro :BuscaItem, itemListener: RecyclerViewItemListener, position: Int){

            val imageView = itemView.findViewById<ImageView>(R.id.imageView)

            if(livro.volumeInfo.imageLinks != null && livro.volumeInfo.imageLinks!!.smallThumbnail != null){
                val url = livro.volumeInfo.imageLinks!!.smallThumbnail.toString()
                Glide.with(itemView).asBitmap().load(url).into(imageView)

            }

            val tvTitulo = itemView.findViewById<TextView>(R.id.tvTitulo)
            tvTitulo.text = livro.volumeInfo.title
            val tvAutores = itemView.findViewById<TextView>(R.id.tvAutores)
            tvAutores.text = livro.volumeInfo.authors.toString()


            itemView.setOnClickListener {
                itemListener.itemClicked(it, livro.id!!)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linha_list_livro, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listaLivros[position], listener,position)
    }

    override fun getItemCount(): Int {
        return listaLivros.size
    }
}