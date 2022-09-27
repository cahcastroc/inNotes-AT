package br.edu.infnet.innotes.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.apiDicionario.Dicionario
import br.edu.infnet.innotes.service.apiDicionario.DicionarioService
import br.edu.infnet.innotes.service.apiDicionario.DicionarioServiceListener
import java.util.*
import kotlin.collections.ArrayList


class DicionarioFragment : Fragment(), DicionarioServiceListener {

    private val dicionarioService = DicionarioService()
    private lateinit var etPalavra: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dicionario, container, false)


        val btBuscar = view.findViewById<Button>(R.id.btBuscar)
        etPalavra = view.findViewById<EditText>(R.id.etPalavra)

        dicionarioService.setDicionarioServiceListener(this)

        btBuscar.setOnClickListener {
            dicionarioService.consultaPalavra(etPalavra.text.toString())
        }

        return view
    }

    override fun onResponse(dicionario: ArrayList<Dicionario>?) {
        if (dicionario != null) {
            val tvResultadoPalavra = requireView().findViewById<TextView>(R.id.tvResultadoPalavra)
            val tvCabecalho = requireView().findViewById<TextView>(R.id.tvCabecalho)
            tvResultadoPalavra.text = dicionario.toString()
            tvCabecalho.text =
                "Significado da palavra ${etPalavra.text.toString()}:"
            etPalavra.setText("")
        }
    }

    override fun onFailure(message: String?) {
        Log.i("AT-Dicionário", "ERRO ${message}")
    }


}