package br.edu.infnet.innotes.service.apiBook

import br.edu.infnet.innotes.domain.apiBook.QueryResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LivroApi {

    @GET("volumes")
    fun queryBooks(@Query("q") query: String?) : Call<QueryResult?>?
}