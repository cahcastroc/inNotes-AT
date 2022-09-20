package br.edu.infnet.innotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.apiBook.QueryResult
import br.edu.infnet.innotes.service.ServiceListener
import br.edu.infnet.innotes.ui.recyclerView.RecyclerViewItemListener


class LivrosFragment : Fragment(), RecyclerViewItemListener, ServiceListener {


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









        return view

    }

    override fun onResponse(queryResult: QueryResult?) {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String?) {
        TODO("Not yet implemented")
    }

    override fun itemClicked(view: View, id: String) {
        TODO("Not yet implemented")
    }

    override fun itemLongClicked(view: View, id: String) {
        TODO("Not yet implemented")
    }


}