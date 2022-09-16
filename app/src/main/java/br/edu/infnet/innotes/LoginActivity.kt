package br.edu.infnet.innotes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import org.json.JSONException


class LoginActivity : AppCompatActivity() {

    private lateinit var appAuth: FirebaseAuth
    private var appUser: FirebaseUser? = null

    private lateinit var callbackManager: CallbackManager
    private lateinit var buttonFacebookLogin: LoginButton


    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        appAuth = FirebaseAuth.getInstance()

        val providers = arrayListOf(
            AuthUI.IdpConfig.FacebookBuilder().build(),
       )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)


        //------------

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
                                "Dados de credenciais inválidos ou Usuário não cadastrado.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } catch (ex: IllegalArgumentException) {
                Toast.makeText(
                    this,
                    "O campo de e-mail e senha não podem ser nulos.",
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

                            val emailUsuario = "${appUser!!.email}"
                            val ultimoLogin = "Esse é o seu primeiro login!"
                            intentAppActivity()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Processo inválido. Caso seja cadastrado clique no botão de login.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }

        }


        //---------------fACEBOOK

//
//        val x = FacebookLoginActivity()
//        Toast.makeText(this, "Facebook ${x}", Toast.LENGTH_LONG).show()


    }

    override fun onStart() {
        super.onStart()
        val currentUser = appAuth.currentUser
        appUser = currentUser


        if (currentUser != null) {
            Toast.makeText(this, "Logado: ${appUser?.email}", Toast.LENGTH_LONG).show()
            intentAppActivity()
            ///--------
        } else {
            Toast.makeText(this, "Não está logado", Toast.LENGTH_LONG).show()
        }
    }


    private fun validaFormatoEmail(etEmail: EditText): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            true
        } else {
            etEmail.error = "E-mail inválido"
            false
        }
    }


    private fun validaFormatoSenha(etSenha: EditText): Boolean {
        return if (etSenha.text.toString().length >= 6) {
            true
        } else {
            etSenha.error = "Mínimo de 6 caracteres"
            false
        }
    }

    fun intentAppActivity() {

        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
        finishAfterTransition()
    }

    private fun onSignInResult(res: FirebaseAuthUIAuthenticationResult?) {
        val response = res?.idpResponse
        if (res != null) {
            if (res.resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    Toast.makeText(this, "${user.email}, aqui o user", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}