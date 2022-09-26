package br.edu.infnet.innotes.domain

import com.android.billingclient.api.SkuDetails

class Produto(var sku : String, var descricao : String?, var preco : String?) {
    var skuDetails : SkuDetails? = null
}