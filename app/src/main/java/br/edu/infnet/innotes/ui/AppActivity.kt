package br.edu.infnet.innotes.ui

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.edu.infnet.innotes.R
import com.google.firebase.auth.FirebaseAuth

class AppActivity : AppCompatActivity(), LocationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem)

        

    }



    override fun onLocationChanged(p0: Location) {

    }


}
