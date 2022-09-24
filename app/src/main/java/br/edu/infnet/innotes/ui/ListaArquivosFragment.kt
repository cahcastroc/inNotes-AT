package br.edu.infnet.innotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import br.edu.infnet.innotes.R
import java.io.File


class ListaArquivosFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view =  inflater.inflate(R.layout.fragment_lista_arquivos, container, false)

//
        val arquivos: Array<out File>? =
            requireContext().filesDir?.listFiles()

        var nomesArquivos = ArrayList<String>()

        if (arquivos != null) {
            for (i in arquivos.indices) {
                nomesArquivos.add(arquivos[i].name.toString())
            }
        }

        val listArquivos = view.findViewById<ListView>(R.id.listArquivos)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, nomesArquivos)
        listArquivos.adapter = adapter
        listArquivos.choiceMode = ListView.CHOICE_MODE_SINGLE




        return view
    }




}