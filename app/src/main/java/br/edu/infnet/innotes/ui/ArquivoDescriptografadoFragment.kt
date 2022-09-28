package br.edu.infnet.innotes.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import br.edu.infnet.innotes.R
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException


class ArquivoDescriptografadoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_arquivo_descriptografado, container, false)

        val ivTeste = view.findViewById<ImageView>(R.id.ivImg)

        val tvConteudo = view.findViewById<TextView>(R.id.tvConteudo)

        val nomeArquivo = arguments?.getString("nome")

        try{
            val masterKey = MasterKey.Builder(requireContext())
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            //-----Descriptografia do arquivo de texto
            val arquivo = File(requireActivity().filesDir, "$nomeArquivo")

            val arquivoCriptografado = EncryptedFile.Builder(
                requireContext(),
                arquivo,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            val fis = arquivoCriptografado.openFileInput()
            val bytes = fis.readBytes()

            tvConteudo.text = bytes.decodeToString()

            // "skia: --- Failed to create image decoder with message 'unimplemented'
            val img = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ivTeste.setImageBitmap(img)
        } catch( ex: InvocationTargetException){
            Toast.makeText(activity,"${ex.message}",Toast.LENGTH_LONG).show()

        } catch (ex: IOException){
            Toast.makeText(activity,"${ex.message}",Toast.LENGTH_LONG).show()
        }


        return view
    }


}