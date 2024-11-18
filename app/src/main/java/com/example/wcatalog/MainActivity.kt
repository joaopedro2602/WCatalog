package com.example.wcatalog

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wcatalog.ui.theme.WCatalogTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class MainActivity : ComponentActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WCatalogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(db)

                }

            }
        }
    }
}


@Composable
fun App(db: FirebaseFirestore) {
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
                    .fillMaxWidth(),

                ) {
                Text(text = "Nome da obra:")


                TextField(
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
                    value = descricao,
                    onValueChange = { descricao = it }
                )
            }
        }
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
            Button(onClick = {
                val obras = hashMapOf(
                    "nome_obra" to nome_obra,
                    "composer" to composer,
                    "catalog" to catalog,
                    "data_lancamento" to data_lancamento,
                    "descricao" to descricao
                )
                db.collection("obras")
                    .add(obras)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            ContentValues.TAG,
                            "DocumentSnapshot successfully written, id: ${documentReference.id}"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            ContentValues.TAG,
                            "Error writing document",
                            e
                        )
                    }
            }) {
                Text(text = "Cadastrar")
            }
        }



        Row(
            Modifier
                .fillMaxWidth()
        ) {

            val obras = mutableStateListOf<HashMap<String, String>>()
            db.collection("obras")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val lista = hashMapOf(
                            "nome_obra" to "${document.data.get("nome_obra")}",
                            "composer" to "${document.data.get("composer")}"
                        )
                        obras.add(lista)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting documents: ", e)
                }


            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(obras) { obra ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column() {
                            ListItem(
                                headlineContent = { Text(obra["nome_obra"] ?: "--") },
                                supportingContent = {
                                    Text(obra["composer"] ?: "--")
                                },
                                leadingContent = {
                                    Icon(
                                        Icons.Filled.PlayArrow,
                                        contentDescription = "Localized description",
                                    )
                                },
                                trailingContent = { Text("meta") }
                            )
                            HorizontalDivider()

                        }
                    }
                }
            }

        }
    }
}


