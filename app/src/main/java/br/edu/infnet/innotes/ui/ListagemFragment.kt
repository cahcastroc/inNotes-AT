package br.edu.infnet.innotes.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import br.edu.infnet.innotes.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*


//listagem das anotações, apresentando data e título em cada linha.
//deve mostrar os dados do usuário logado e permitir o logout - Botão
//O Banner será exibido na tela de listagem, junto a um botão para desbloqueio da versão Premium.
//Listagem -

class ListagemFragment : Fragment() {

    private lateinit var appAuth: FirebaseAuth
    private var appUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_listagem, container, false)



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


        return view

    }


}