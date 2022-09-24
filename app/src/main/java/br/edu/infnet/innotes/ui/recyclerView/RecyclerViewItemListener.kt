package br.edu.infnet.innotes.ui.recyclerView

import android.view.View

interface RecyclerViewItemListener {

    fun itemClicked(view: View, id: String)

    fun itemDeletar(view: View, id:String)

    fun itemEditar(view: View, id:String)

}