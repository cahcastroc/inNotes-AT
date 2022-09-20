package br.edu.infnet.innotes.service

import br.edu.infnet.innotes.domain.apiBook.QueryResult

interface ServiceListener {

    fun onResponse(queryResult: QueryResult?)

    fun onFailure(message: String?)
}
