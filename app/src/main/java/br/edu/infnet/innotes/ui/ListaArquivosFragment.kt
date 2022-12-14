package br.edu.infnet.innotes.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import br.edu.infnet.innotes.R
import java.io.File


class ListaArquivosFragment : Fragment(),AdapterView.OnItemClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view =  inflater.inflate(R.layout.fragment_lista_arquivos, container, false)

//
        val arquivos: Array<out File>? =
            requireContext().filesDir?.listFiles()

        val nomesArquivos = ArrayList<String>()

        if (arquivos != null) {
            for (i in arquivos.indices) {
                nomesArquivos.add(arquivos[i].name.toString())
            }
        }

        val listArquivos = view.findViewById<ListView>(R.id.listArquivos)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, nomesArquivos)
        listArquivos.adapter = adapter
        listArquivos.choiceMode = ListView.CHOICE_MODE_SINGLE
        listArquivos.onItemClickListener = this


        return view
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val itemClicado :String = p0?.getItemAtPosition(p2) as String

        val bundle = bundleOf("nome" to itemClicado)
        findNavController().navigate(R.id.action_listaArquivosFragment_to_arquivoDescriptografadoFragment,bundle)



    }




}