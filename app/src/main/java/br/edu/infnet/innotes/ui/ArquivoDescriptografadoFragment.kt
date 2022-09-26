package br.edu.infnet.innotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.edu.infnet.innotes.R


class ArquivoDescriptografadoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view =  inflater.inflate(R.layout.fragment_arquivo_descriptografado, container, false)

        val ivTeste = view.findViewById<ImageView>(R.id.ivTeste)
        ivTeste.visibility = View.INVISIBLE

        val tvConteudo = view.findViewById<TextView>(R.id.tvConteudo)


        val conteudo = arguments?.getString("conteudo")
        tvConteudo.text = conteudo


        return view
    }


}