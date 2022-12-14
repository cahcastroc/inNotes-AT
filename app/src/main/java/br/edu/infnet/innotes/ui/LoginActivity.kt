package br.edu.infnet.innotes.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.infnet.innotes.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.concurrent.thread


class LoginActivity : AppCompatActivity() {

    private lateinit var appAuth: FirebaseAuth
    private var appUser: FirebaseUser? = null


    //-----------------------Facebook

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    //-----------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        appAuth = FirebaseAuth.getInstance()

        //-------------------------------Facebook

        val providers = arrayListOf(
            AuthUI.IdpConfig.FacebookBuilder().build(),
        )

        val btFacebook = findViewById<Button>(R.id.btFacebook)

        btFacebook.setOnClickListener {

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)

        }
        //----------------------------------------------


        val btLogin = findViewById<Button>(R.id.btLogin)
        val btCadastrar = findViewById<Button>(R.id.btCadastrar)


        btLogin.setOnClickListener {
            try {
                val etEmail = findViewById<EditText>(R.id.etEmail)
                val etSenha = findViewById<EditText>(R.id.etSenha)

                appAuth
                    .signInWithEmailAndPassword(etEmail.text.toString(), etSenha.text.toString())
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            appUser = appAuth.currentUser


                            intentAppActivity()

                        } else {
                            appUser = null
                            Toast.makeText(
                                this,
                                "Dados de credenciais inv??lidos ou Usu??rio n??o cadastrado.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.addOnFailureListener(this) {
                        Toast.makeText(
                            this,
                            "${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } catch (ex: IllegalArgumentException) {
                Toast.makeText(
                    this,
                    "O campo de e-mail e senha n??o podem ser nulos.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        btCadastrar.setOnClickListener {

            val etEmail = findViewById<EditText>(R.id.etEmail)
            val etSenha = findViewById<EditText>(R.id.etSenha)

            if (validaFormatoEmail(etEmail) && validaFormatoSenha(etSenha)) {

                appAuth.createUserWithEmailAndPassword(
                    etEmail.text.toString(),
                    etSenha.text.toString()
                )
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            appUser = appAuth.currentUser
                            Toast.makeText(
                                this,
                                "Cadastro realizado com sucesso!",
                                Toast.LENGTH_LONG
                            ).show()

                            intentAppActivity()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = appAuth.currentUser
        appUser = currentUser


        if (currentUser != null) {
            Toast.makeText(this, "Logado: ${appUser?.email}", Toast.LENGTH_LONG).show()
            intentAppActivity()
        }
    }


    private fun validaFormatoEmail(etEmail: EditText): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            true
        } else {
            etEmail.error = "E-mail inv??lido"
            false
        }
    }


    private fun validaFormatoSenha(etSenha: EditText): Boolean {
        return if (etSenha.text.toString().length >= 6) {
            true
        } else {
            etSenha.error = "M??nimo de 6 caracteres"
            false
        }
    }

    fun intentAppActivity() {

        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
        finishAfterTransition()
    }

    private fun onSignInResult(res: FirebaseAuthUIAuthenticationResult?) {
//        val response = res?.idpResponse
        if (res != null) {
            if (res.resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    Toast.makeText(
                        this,
                        "Login atrav??s do facebook: ${user.email}",
                        Toast.LENGTH_LONG
                    ).show()
                    intentAppActivity()
                }
            }
        }
    }
}