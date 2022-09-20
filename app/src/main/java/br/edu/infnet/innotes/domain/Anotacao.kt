package br.edu.infnet.innotes.domain

data class Anotacao (
    var id: String? = null, //auto
    var email: String? = null, //auth
    var data:  String? = null, //na hora que salvar
    var latitude: String? = null, //na hora que salvar
    var longitude: String? = null, //na hora que salvar
    var titulo: String? = null,
    var texto: String? = null,

)