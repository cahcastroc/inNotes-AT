package br.edu.infnet.innotes.service.apiDicionario

import br.edu.infnet.innotes.domain.apiDicionario.Dicionario
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DicionarioApi {

    @GET("{palavra}")
    fun consultaPalavra(@Path("palavra") query: String) : Call<ArrayList<Dicionario>>?
}