package br.edu.infnet.innotes.ui

import android.Manifest
import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey


import br.edu.infnet.innotes.R
import br.edu.infnet.innotes.domain.Anotacao
import br.edu.infnet.innotes.service.AnotacaoDao
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CriaAnotacaoFragment : Fragment(), LocationListener {

    private val anotacaoDao = AnotacaoDao()
    private lateinit var anotacao: Anotacao
    private lateinit var etTitulo: EditText
    private lateinit var etTexto: EditText
    private lateinit var imgCamera: ImageView
    private lateinit var date: String
    private lateinit var latitude: String
    private lateinit var longitude: String

    val FINE_REQUEST = 5678
    val CAMERA_PERMISSION_CODE = 100
    val CAMERA_REQUEST = 1888
    private var localizacao: Location? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cria_anotacao, container, false)

        etTitulo = view.findViewById(R.id.etTitulo)
        etTexto = view.findViewById<EditText>(R.id.etTexto)
        imgCamera = view.findViewById<ImageView>(R.id.ivCamera)
        val btCamera = view.findViewById<ImageButton>(R.id.btCamera)

        //----View antes da criação da anotação
        val containerView = view.findViewById<LinearLayout>(R.id.containerView)
        containerView.visibility = View.INVISIBLE


        localizacao = localizacaoPorGps()
        date = data()

        val email = FirebaseAuth.getInstance().currentUser?.email


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
                intentCamera()
            }
        }


        val btSalvarAnotacao = view.findViewById<Button>(R.id.btSalvarAnotacao)

        btSalvarAnotacao.setOnClickListener {

            if (localizacao == null) {
                localizacao = localizacaoPorGps()

            }
            latitude = localizacao?.latitude.toString()
            longitude = localizacao?.longitude.toString()


            containerView.visibility = View.VISIBLE


            anotacao = Anotacao(
                null,
                email,
                date,
                latitude,
                longitude,
                etTitulo.text.toString(),
                etTexto.text.toString()
            )
            anotacaoDao.inserir(anotacao)?.addOnSuccessListener {
                Toast.makeText(activity, "Registro salvo com sucesso", Toast.LENGTH_LONG).show()
                salvaArquivoTxt()
            }?.addOnFailureListener {
                Toast.makeText(activity, "Erro inesperado", Toast.LENGTH_LONG).show()
            }
            Log.i(
                "DR3",
                "email: ${email}, data ${date}, local: Latitude: ${latitude} , Logitude: ${longitude}"
            )

        }

        return view
    }


    private fun data(): String {
        val data = Calendar.getInstance().time

        val dataFormat = SimpleDateFormat("dd_MM_yyyy_HH-mm-ss", Locale.getDefault())

        return dataFormat.format(data)
    }


    private fun localizacaoPorGps(): Location? {


        var localizacao: Location? = null

        var locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        val servicoAtivado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (servicoAtivado) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PermissionChecker.PERMISSION_GRANTED
            ) {
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


    private fun salvaArquivoTxt() {
        val masterKey = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val file = File(requireActivity().filesDir, "${etTitulo.text}-${date}.txt")
        Log.i(
            "DR3",
            "local: ${latitude} - ${longitude}, data ${date}, titulo: ${etTitulo.text}, texto: ${etTexto.text}"
        )

        if (file.exists()) {
            file.delete()
        }
        val encryptedFile = EncryptedFile.Builder(
            requireContext(),
            file,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        val fos = encryptedFile.openFileOutput()

        fos.write("Localização: \nLatitude: ${localizacao?.latitude.toString()}, longitude: ${localizacao?.longitude}\n${etTexto.text}".toByteArray())
        fos.close()
    }

    private fun intentCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    private fun salvaArquivoFig(bitmap: Bitmap?) {
        val masterKey = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val file = File(requireActivity().filesDir, "${etTitulo.text}-${date}.fig")

        if (file.exists()) {
            file.delete()
        }
        val encryptedFile = EncryptedFile.Builder(
            requireContext(),
            file,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        val fos = encryptedFile.openFileOutput()



        fos.write(bitmap.toString().toByteArray())
        fos.close()
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
                        intentCamera()

                    } else {
                        Toast.makeText(activity, "Não permitido acesso à câmera", Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            val imgBitmap = data?.extras!!["data"] as Bitmap
            imgCamera = requireView().findViewById<ImageView>(R.id.ivCamera)
            imgCamera?.setImageBitmap(imgBitmap)
            val bitmap = (imgCamera.drawable as BitmapDrawable).bitmap

            salvaArquivoFig(bitmap)

        }
    }

    override fun onLocationChanged(p0: Location) {
    }
}