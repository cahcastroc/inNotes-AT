package br.edu.infnet.innotes.domain.apiDicionario

data class Dicionario (

//    var items: ArrayList<ResultadoPalavra>
    val partOfSpeech: String,
    val meanings: List<String>,
    val etymology: String

)