package br.edu.infnet.innotes.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.service.AnotacaoDao


class EditFragment : Fragment() {

    private val anotacaoDao = AnotacaoDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_edit, container, false)

        var id = arguments?.getString("id")

        val etNovoTitulo = view.findViewById<EditText>(R.id.etNovoTitulo)
        val etNovoTexto = view.findViewById<EditText>(R.id.etNovoTexto)

        val ibSalvar = view.findViewById<ImageView>(R.id.ibSalvar)


        ibSalvar.setOnClickListener {
            anotacaoDao.editar(id!!, etNovoTitulo.text.toString(), etNovoTexto.text.toString())
                ?.addOnSuccessListener {
                    Toast.makeText(activity, "Item editado", Toast.LENGTH_LONG).show()

                    findNavController().navigate(R.id.action_editFragment_to_listagemFragment)
                }?.addOnFailureListener {
                    Toast.makeText(activity, "${it.message}", Toast.LENGTH_LONG).show()
                }
        }
        return view
    }


}