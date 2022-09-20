package br.edu.infnet.innotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import br.edu.infnet.innotes.R


class MenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        val navController = this.findNavController()

        val btInserir = view.findViewById<Button>(R.id.btInserir)
        val btVisualizar = view.findViewById<Button>(R.id.btVisualizar)

        btInserir.setOnClickListener{
            navController.navigate(R.id.action_menuFragment_to_criaAnotacaoFragment)
        }

        btVisualizar.setOnClickListener{
            navController.navigate(R.id.action_menuFragment_to_listagemFragment)
        }




        return view
    }


}