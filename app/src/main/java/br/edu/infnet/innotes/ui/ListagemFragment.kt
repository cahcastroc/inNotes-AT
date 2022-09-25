package br.edu.infnet.innotes.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.Anotacao
import br.edu.infnet.innotes.service.AnotacaoDao
import br.edu.infnet.innotes.service.billing.Loja
import br.edu.infnet.innotes.ui.recyclerView.AnotacoesAdapter
import br.edu.infnet.innotes.ui.recyclerView.RecyclerViewItemListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.concurrent.thread


//listagem das anotações, apresentando data e título em cada linha.
//deve mostrar os dados do usuário logado e permitir o logout - Botão
//O Banner será exibido na tela de listagem, junto a um botão para desbloqueio da versão Premium.
//Listagem -

class ListagemFragment : Fragment(), RecyclerViewItemListener {

    private lateinit var appAuth: FirebaseAuth
    private var appUser: FirebaseUser? = null
    private val anotacaoDao = AnotacaoDao()
    private lateinit var adapter: AnotacoesAdapter
    private lateinit var loja : Loja


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_listagem, container, false)

        //----Billing
        loja = Loja(requireContext() as AppCompatActivity)

        //--------Logout
        val btLogout = view.findViewById<Button>(R.id.btLogout)

        btLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        //-------adMob


            MobileAds.initialize(context){}

            val adView = view.findViewById<AdView>(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)



        //--------Dados do usuário

        appAuth = FirebaseAuth.getInstance()
        appUser = appAuth.currentUser
        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val tvUltimoLogin = view.findViewById<TextView>(R.id.tvUltimoLogin)
        tvUserEmail.text = appUser?.email.toString()
        tvUltimoLogin.text = appUser?.metadata?.let { Date(it.lastSignInTimestamp).toString() }

      //--------------------------

        atualizar()

        val btComprar = view.findViewById<Button>(R.id.btComprar)

        btComprar.setOnClickListener {

            thread {
                val produto = loja.produtos[0]
                loja.efetuarCompra(produto)
            }
            val adContainer = view.findViewById<LinearLayout>(R.id.adContainer)
            adContainer.visibility = View.INVISIBLE

        }




        return view

    }


    private fun atualizar(){

        thread {
            if (appUser != null) {
                anotacaoDao.anotacoesUsuario(appUser!!.email!!).addOnSuccessListener {
                    val anotacoesUsuario = ArrayList<Anotacao>()

                    for (documento in it) {
                        var anotacao = documento.toObject(Anotacao::class.java)//
                        anotacoesUsuario.add(anotacao)
                    }

                    val rvAnotacoesUsuario = view?.findViewById<RecyclerView>(R.id.rvLista)
                    rvAnotacoesUsuario?.layoutManager = LinearLayoutManager(context)
                    adapter = AnotacoesAdapter(this)
                    adapter.listaAnotacao = anotacoesUsuario
                    rvAnotacoesUsuario?.adapter = adapter


                }.addOnFailureListener {
                    Toast.makeText(activity, "Falha", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    override fun itemClicked(view: View, id: String) {

    }


    override fun itemDeletar(view: View, id: String) {
        anotacaoDao.deletar(id).addOnSuccessListener {
            atualizar()
            Toast.makeText(requireContext(), "Item deletado", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Erro ao deletar", Toast.LENGTH_LONG).show()
        }

    }

    override fun itemEditar(view: View, id: String) {
        val bundle = bundleOf("id" to id )
        findNavController().navigate(R.id.action_listagemFragment_to_editFragment,bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        loja.fecharLoja()
    }

}