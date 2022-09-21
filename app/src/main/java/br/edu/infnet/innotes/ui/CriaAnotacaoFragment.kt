package br.edu.infnet.innotes.ui

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission


import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.Anotacao
import br.edu.infnet.innotes.service.AnotacaoDao
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*


class CriaAnotacaoFragment : Fragment(), LocationListener {

    private val anotacaoDao = AnotacaoDao()
    private lateinit var anotacao: Anotacao

    val FINE_REQUEST = 5678
    val COARSE_REQUEST = 8765
//    private var localizacao: Location? = null


//    private val criptografador = Criptografador()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_cria_anotacao, container, false)



        //----View após criação da anotação
        val containerView = view.findViewById<LinearLayout>(R.id.containerView)
        containerView.visibility = View.INVISIBLE

        //----------Localização

        localizacaoPorGps()



        //---------


        val email = FirebaseAuth.getInstance().currentUser?.email
        var data = data()










        val btSalvarAnotacao = view.findViewById<Button>(R.id.btSalvarAnotacao)

        btSalvarAnotacao.setOnClickListener {
            containerView.visibility = View.VISIBLE
            var latitude = localizacaoPorGps()?.latitude.toString()
            var longitude = localizacaoPorGps()?.longitude.toString()
            Log.i("DR3", "email: ${email}, data ${data}, local: Latitude: ${latitude} , Logitude: ${longitude}")
        }


        return view
    }

    private fun data(): String {
        val date = Calendar.getInstance().time

        val dateTimeFormat = SimpleDateFormat("dd_MM_yyyy_HH-mm-ss", Locale.getDefault())

        return dateTimeFormat.format(date)
    }

    override fun onLocationChanged(p0: Location) {

    }

    private fun localizacaoPorGps() : Location?{


        var localizacao: Location? = null

        var locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
//        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        val isServiceEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isServiceEnabled) {

            if(checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//            if (context?.let { ActivityCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_FINE_LOCATION) }
                == PermissionChecker.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    0f,
                    this
                )

                localizacao = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_REQUEST

                )

            }
        }
        return localizacao

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            localizacaoPorGps()
        }



    }




}