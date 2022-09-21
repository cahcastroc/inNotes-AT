package br.edu.infnet.innotes.service.apiDicionario

import br.edu.infnet.innotes.domain.apiDicionario.Dicionario

interface DicionarioServiceListener {

    fun onResponse(dicionario: ArrayList<Dicionario>?)

    fun onFailure(message: String?)
}