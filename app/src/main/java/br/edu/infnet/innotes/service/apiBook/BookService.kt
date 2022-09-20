package br.edu.infnet.innotes.service.apiBook

import android.util.Log
import br.edu.infnet.innotes.domain.apiBook.QueryResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookService {

    private  var api: BookApi
    private lateinit var listener: BookServiceListener

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(BookApi::class.java)
    }


    fun setBookServiceListener(listener: BookServiceListener){
        this.listener = listener
    }

    fun queryBooks(type:String,query: String){

        if(type.isNotEmpty() && query.isNotEmpty()){
            var queryWithType = ""

            //"Converte" o valor do Spinner

            when(type){
                "Titulo" -> queryWithType = "intitle:"
                "Autor" -> queryWithType = "inauthor:"
            }
            queryWithType += query

            val call = api.queryBooks(queryWithType)

            call!!.enqueue(object : Callback<QueryResult?> {
                override fun onResponse(
                    call: Call<QueryResult?>,
                    response: Response<QueryResult?>
                ) {
                    Log.i("DR3", "onResponse  ${response.code()}")
                    if(response.isSuccessful){
                        listener.onResponse(response.body())
                    }

                }

                override fun onFailure(call: Call<QueryResult?>, t: Throwable) {
                    Log.i("DR3", "onFailure ERRO ${t.message}")
                    listener.onFailure(t.message)
                }


            })

        }
    }
}