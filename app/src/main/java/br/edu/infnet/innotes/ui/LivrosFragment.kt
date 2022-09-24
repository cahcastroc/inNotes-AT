package br.edu.infnet.innotes.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.apiBook.QueryItem
import br.edu.infnet.innotes.domain.apiBook.QueryResult
import br.edu.infnet.innotes.service.apiBook.bookServiceListener
import br.edu.infnet.innotes.service.apiBook.LivroService
import br.edu.infnet.innotes.ui.recyclerView.LivrosAdapter
import br.edu.infnet.innotes.ui.recyclerView.RecyclerViewItemListener
import java.lang.NullPointerException


class LivrosFragment : Fragment(), RecyclerViewItemListener, bookServiceListener {

    private val livroService = LivroService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_livros, container, false)


        //-----------Spinner
        val opcoesSpinner= arrayOf("Titulo", "Autor")
        val spOpcoes = view.findViewById<Spinner>(R.id.spOpcoes)
        spOpcoes.adapter =
            context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1,opcoesSpinner) }
        spOpcoes.setSelection(0)

        //------------RecyclerView em branco

        val rvResultadoLivro = view.findViewById<RecyclerView>(R.id.rvResultadoLivros)
        rvResultadoLivro.layoutManager = GridLayoutManager(context,3)
        val adapter = LivrosAdapter(this)
        rvResultadoLivro.adapter = adapter

        //-------Service da Api

        val btPesquisar = view.findViewById<Button>(R.id.btPesquisar)
        livroService.setBookServiceListener(this)

        btPesquisar.setOnClickListener{
            var opcaoPesquisa = spOpcoes.selectedItem.toString()

            val etConsulta = view.findViewById<EditText>(R.id.etConsulta)

            if(etConsulta.text.toString().isNotEmpty() || etConsulta.text.toString().isNotBlank() ){
                livroService.queryBooks(opcaoPesquisa,etConsulta.text.toString())
            }

        }


        return view

    }

    override fun onResponse(queryResult: QueryResult?) {
        try {
            if (queryResult != null) {
                val itensPesquisa = queryResult.items
                val listaLivros = ArrayList<QueryItem>()
                Log.i("DR3", "Resultado com ${itensPesquisa.size} itens")

                for (queryItem in itensPesquisa) {
                    listaLivros.add(queryItem)
                }

                // Recycler view com o Resultado
                val rvResultadoLivros = view?.findViewById<RecyclerView>(R.id.rvResultadoLivros)
                val adapter = LivrosAdapter(this)
                adapter.listaLivros = listaLivros
                if (rvResultadoLivros != null) {
                    rvResultadoLivros.adapter = adapter
                }
            }
        } catch (ex: NullPointerException){
            Toast.makeText(activity,"Erro! Não localizado", Toast.LENGTH_LONG).show()
        }
    }

    override fun onFailure(message: String?) {

    }

    override fun itemClicked(view: View, id: String) {

    }

    override fun itemLongClicked(view: View, id: String) {

    }

    override fun itemDeletar(view: View, id: String) {

    }


}