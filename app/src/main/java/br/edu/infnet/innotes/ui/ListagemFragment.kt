package br.edu.infnet.innotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.infnet.innotes.R


//listagem das anotações, apresentando data e título em cada linha.
//deve mostrar os dados do usuário logado e permitir o logout - Botão
//O Banner será exibido na tela de listagem, junto a um botão para desbloqueio da versão Premium.
//Listagem -

class ListagemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_listagem, container, false)

        return view

    }


}