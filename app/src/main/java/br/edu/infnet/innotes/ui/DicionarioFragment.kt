package br.edu.infnet.innotes.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.apiDicionario.Dicionario
import br.edu.infnet.innotes.service.apiDicionario.DicionarioService
import br.edu.infnet.innotes.service.apiDicionario.DicionarioServiceListener


class DicionarioFragment : Fragment(), DicionarioServiceListener {

    private val dicionarioService = DicionarioService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view =  inflater.inflate(R.layout.fragment_dicionario, container, false)

        val ok = view.findViewById<Button>(R.id.ok)
        val etPalavra = view.findViewById<EditText>(R.id.etPalavra)

        dicionarioService.setDicionarioServiceListener(this)

        ok.setOnClickListener { dicionarioService.consultaPalavra(etPalavra.text.toString())
        }




        return view

    }

    override fun onResponse(dicionario: ArrayList<Dicionario>?) {
        if (dicionario != null) {
            val resultadoPalavra = dicionario



            Log.i("DR3", "part ${resultadoPalavra[0]}")
        }
    }

    override fun onFailure(message: String?) {
        Log.i("DR3", "ERRO ${message}")
    }


}