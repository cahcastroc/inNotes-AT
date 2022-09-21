package br.edu.infnet.innotes.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    val CAMERA_PERMISSION_CODE = 100
    val CAMERA_REQUEST = 1888
//    private var localizacao: Location? = null


//    private val criptografador = Criptografador()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cria_anotacao, container, false)

        val etTitulo = view.findViewById<EditText>(R.id.etTitulo)
        val etTexto = view.findViewById<EditText>(R.id.etTexto)
        val ivCamera = view.findViewById<ImageView>(R.id.ivCamera)
        val btCamera = view.findViewById<ImageButton>(R.id.btCamera)


        //----View antes da criação da anotação
        val containerView = view.findViewById<LinearLayout>(R.id.containerView)
        containerView.visibility = View.INVISIBLE

        //----------Localização

        localizacaoPorGps()


        val email = FirebaseAuth.getInstance().currentUser?.email

        //---------

        val latitude = localizacaoPorGps()?.latitude.toString()
        val longitude = localizacaoPorGps()?.longitude.toString()
        val titulo = etTitulo.text.toString()
        val texto = etTexto.text.toString()
        val data = data()

        //---------------
        btCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                //Solicita permissão
                this.requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
                //Chama função que executa o Intent da Câmera
                openCamera()
            }
        }


        val btSalvarAnotacao = view.findViewById<Button>(R.id.btSalvarAnotacao)

        btSalvarAnotacao.setOnClickListener {
            containerView.visibility = View.VISIBLE


            val anotacao = Anotacao(null, email, data, latitude, longitude, titulo, texto)
            anotacaoDao.inserir(anotacao)?.addOnSuccessListener {
                Toast.makeText(activity, "Registro salvo com sucesso", Toast.LENGTH_LONG).show()
            }?.addOnFailureListener {
                Toast.makeText(activity, "Erro inesperado", Toast.LENGTH_LONG).show()
            }
            Log.i(
                "DR3",
                "email: ${email}, data ${data}, local: Latitude: ${latitude} , Logitude: ${longitude}"
            )


        }




        return view
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    private fun data(): String {
        val date = Calendar.getInstance().time

        val dateTimeFormat = SimpleDateFormat("dd_MM_yyyy_HH-mm-ss", Locale.getDefault())

        return dateTimeFormat.format(date)
    }

    override fun onLocationChanged(p0: Location) {

    }

    private fun localizacaoPorGps(): Location? {


        var localizacao: Location? = null

        var locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
//        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        val isServiceEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isServiceEnabled) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
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
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_REQUEST

                )

            }
        }
        return localizacao

    }

    private fun salvaFirestore() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (grantResults.isNotEmpty()) {
            if (requestCode == FINE_REQUEST) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    localizacaoPorGps()
                } else {
                    Toast.makeText(activity, "Não permitido acesso ao GPS", Toast.LENGTH_LONG)
                        .show()
                }
                if (requestCode == CAMERA_PERMISSION_CODE) {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera()

                    } else {
                        Toast.makeText(activity, "Não permitido acesso à câmera", Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }
        }


//        if (grantResults.isNotEmpty() && requestCode == FINE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            localizacaoPorGps()
//        }
//        if(requestCode == CAMERA_PERMISSION_CODE){
//
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openCamera()
//
//            }else{
//                Toast.makeText(activity, "Não permitido acesso à câmera", Toast.LENGTH_LONG).show()
//            }
//
//    }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            val picture = data?.extras!!["data"] as Bitmap
            val imgCamera = view?.findViewById<ImageView>(R.id.ivCamera)
            imgCamera?.setImageBitmap(picture)
        }
    }
}