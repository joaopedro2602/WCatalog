package com.example.wcatalog.views

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcatalog.ui.theme.WCatalogTheme
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun MainScreen(navController: NavController, db: FirebaseFirestore) {

    var nome_obra by remember {
        mutableStateOf("")
    }
    var composer by remember {
        mutableStateOf("")
    }
    var catalog by remember {
        mutableStateOf("")
    }
    var data_lancamento by remember {
        mutableStateOf("")
    }
    var descricao by remember {
        mutableStateOf("")
    }

    val obras = remember {
        mutableStateListOf<Pair<String, HashMap<String, String>>>()
    }

    LaunchedEffect(Unit) {
        db.collection("obras")
            .get()
            .addOnSuccessListener { documents ->
                obras.clear() // Limpa a lista antes de adicionar os documentos
                for (document in documents) {
                    val data = hashMapOf(
                        "nome_obra" to "${document.data["nome_obra"]}",
                        "composer" to "${document.data["composer"]}"
                    )
                    obras.add(document.id to data) // Adiciona o ID do documento junto com os dados
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents: ", e)
            }
    }

    WCatalogTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background

        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)

            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                }
                Row(
                    Modifier
                        .fillMaxWidth(),
                    Arrangement.Center
                ) {
                    Text(text = "W Catalog")
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()

                    ) {
                        Text(text = "Nome da obra:")


                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = nome_obra,
                            onValueChange = { nome_obra = it }
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Compositor:")

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = composer,
                            onValueChange = { composer = it }
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Catalogação:")

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = catalog,
                            onValueChange = { catalog = it }
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Ano de lançamento:")

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = data_lancamento,
                            onValueChange = { data_lancamento = it }
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Descrição:")

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = descricao,
                            onValueChange = { descricao = it }
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                }
                Row(
                    Modifier
                        .fillMaxWidth(),
                    Arrangement.Center
                ) {
                    Button(onClick = {
                        val obra = hashMapOf(
                            "nome_obra" to nome_obra,
                            "composer" to composer,
                            "catalog" to catalog,
                            "data_lancamento" to data_lancamento,
                            "descricao" to descricao
                        )
                        db.collection("obras").add(obra)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "DocumentSnapshot successfully written, id: ${documentReference.id}")

                                // Adiciona a nova obra à lista local
                                obras.add(
                                    documentReference.id to hashMapOf(
                                        "nome_obra" to nome_obra,
                                        "composer" to composer
                                    )
                                )
                                // Limpa os campos de entrada
                                nome_obra = ""
                                composer = ""
                                catalog = ""
                                data_lancamento = ""
                                descricao = ""
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error writing document", e)
                            }
                    }) {
                        Text("Cadastrar")
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                ) {

                    val obras2 = mutableStateListOf<HashMap<String, String>>()
                    db.collection("obras")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val lista = hashMapOf(
                                    "nome_obra" to "${document.data.get("nome_obra")}",
                                    "composer" to "${document.data.get("composer")}"
                                )
                                obras2.add(lista)
                                Log.d(TAG, "${document.id} => ${document.data}")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error getting documents: ", e)
                        }


                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(obras) { obra -> // obra é um Pair<String, HashMap<String, String>>
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column {
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                obra.second["nome_obra"] ?: "--"
                                            )
                                        },
                                        supportingContent = {
                                            Text(
                                                obra.second["composer"] ?: "--"
                                            )
                                        },
                                        leadingContent = {
                                            Icon(
                                                Icons.Filled.PlayArrow,
                                                contentDescription = "Localized description",
                                            )
                                        },
                                        trailingContent = {
                                            IconButton(onClick = {
                                                // Exclusão do documento no Firestore
                                                db.collection("obras")
                                                    .document(obra.first) // Usa obra.first como o ID
                                                    .delete()
                                                    .addOnSuccessListener {
                                                        Log.d(
                                                            TAG,
                                                            "DocumentSnapshot successfully deleted!"
                                                        )
                                                        obras.remove(obra) // Atualiza a lista local
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.w(TAG, "Error deleting document", e)
                                                    }
                                            }) {
                                                Icon(
                                                    Icons.Filled.Delete,
                                                    contentDescription = "Delete Icon"
                                                )
                                            }
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}


