package br.edu.infnet.innotes.domain

data class Anotacao (
    var id: String? = null,
    var email: String? = null,
    var data:  String? = null, //ou data?
    var localizacao: String? = null,
    var titulo: String? = null,
    var texto: String? = null,
    var livro: String? = null,
    var imagem: String? = null //usar metadados
)