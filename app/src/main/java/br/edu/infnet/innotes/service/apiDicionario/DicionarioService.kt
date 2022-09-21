package br.edu.infnet.innotes.service.apiDicionario

import android.util.Log
import br.edu.infnet.innotes.domain.apiDicionario.Dicionario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DicionarioService {

    private var api: DicionarioApi
    private lateinit var listener: DicionarioServiceListener

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://significado.herokuapp.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(DicionarioApi::class.java)
    }

    fun setDicionarioServiceListener(listener: DicionarioServiceListener) {
        this.listener = listener
    }

    fun consultaPalavra(palavra: String) {
        if (palavra.isNotEmpty()) {
            val call = api.consultaPalavra(palavra)

            call!!.enqueue(object : Callback<ArrayList<Dicionario>?> {

                override fun onResponse(
                    call: Call<ArrayList<Dicionario>?>,
                    response: Response<ArrayList<Dicionario>?>
                ) {
                    if (response.isSuccessful) {
                        listener.onResponse(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Dicionario>?>, t: Throwable) {
                    Log.i("DR3", "onFailure ERRO ${t.message}")
                    listener.onFailure(t.message)
                }
            })
        }




    }
}