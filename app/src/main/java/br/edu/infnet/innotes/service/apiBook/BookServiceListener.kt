package br.edu.infnet.innotes.service.apiBook

import br.edu.infnet.innotes.domain.apiBook.QueryResult

interface BookServiceListener {

    fun onResponse(queryResult: QueryResult?)

    fun onFailure(message: String?)
}
