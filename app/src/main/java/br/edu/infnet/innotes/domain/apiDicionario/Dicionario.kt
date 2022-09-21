package br.edu.infnet.innotes.domain.apiDicionario

import com.google.gson.annotations.SerializedName

data class Dicionario (

    @SerializedName("meanings")
    val significado: List<String>,



) {
    override fun toString(): String {
        return significado.toString().replace(".,", ";\n").replace("["," ").replace("]"," ")
    }
}
