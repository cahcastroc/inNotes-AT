package br.edu.infnet.innotes.service.apiBook

import br.edu.infnet.innotes.domain.apiLivros.QueryResult

interface bookServiceListener {

    fun onResponse(queryResult: QueryResult?)

    fun onFailure(message: String?)
}
