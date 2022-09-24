package br.edu.infnet.innotes.service

import br.edu.infnet.innotes.domain.Anotacao
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AnotacaoDao {

    private val collection = "anotacoes"
    val db = Firebase.firestore

    fun setUpSnapShotListener(
        listener: (
            QuerySnapshot?,
            FirebaseFirestoreException?
        ) -> Unit
    ) = db
        .collection(collection)
        .addSnapshotListener(listener)

    fun listar(): Task<QuerySnapshot> {
        return db.collection(collection).get()
    }


    fun inserir(anotacao: Anotacao): Task<Void>? {
        var task: Task<Void>? = null
        if (anotacao.id == null) {
            val ref: DocumentReference = db.collection(collection).document()
            anotacao.id = ref.id
            task = ref.set(anotacao)
        }
        return task
    }

    fun deletar(id: String): Task<Void> {
        return db.collection(collection).document(id).delete()
    }

    fun editar(id:String, titulo:String,texto:String): Task<Void>?{
        return db.collection(collection).document(id).update("titulo",titulo, "texto",texto)
    }


    fun anotacoesUsuario(email: String): Task<QuerySnapshot> {
        return db.collection(collection).whereEqualTo("email", email).get()

    }


}