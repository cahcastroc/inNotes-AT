package br.edu.infnet.innotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class ListagemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem)

        val button2 = findViewById<Button>(R.id.button2)

        button2.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

        }


            // Handle the back button event
        }
    }
