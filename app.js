const express = require("express")
const app = express()
const handlebars = require("express-handlebars").engine
const bodyParser = require("body-parser")
const { initializeApp, applicationDefault, cert } = require('firebase-admin/app')
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore')
const serviceAccount = require('./firebase-key.json')
initializeApp({
  credential: cert(serviceAccount)
})
const db = getFirestore()
app.engine("handlebars", handlebars({defaultLayout: "main"}))
app.set("view engine", "handlebars")
app.use(bodyParser.urlencoded({extended: false}))
app.use(bodyParser.json())
app.get("/", function(req, res){
    res.render("primeira_pagina")
})
app.get("/consulta", async function(req, res){
    const dataSnapshot = await db.collection("obras").get()
    const data = []
    dataSnapshot.forEach((doc) => {
        data.push({
            id: doc.id,
            nome_obra: doc.get("nome_obra"),
            composer: doc.get("composer"),
            catalog: doc.get("catalog"),
            data_lancamento: doc.get("data_lancamento"),
            descricao: doc.get("descricao")
        })
    })
    res.render("consulta", {data})
})
app.get("/editar/:id", async function(req, res){
    const dataSnapshot = await db.collection("obras").doc(req.params.id).get()
    const data = {
        id: dataSnapshot.id,
        nome_obra: dataSnapshot.get("nome_obra"),
        composer: dataSnapshot.get("composer"),
        catalog: dataSnapshot.get("catalog"),
        data_lancamento: dataSnapshot.get("data_lancamento"),
        descricao: dataSnapshot.get("descricao")
    }
    res.render("editar", {data})
})
app.get("/excluir/:id", function(req, res){
    db.collection("obras").doc(req.params.id).delete()
    res.redirect("/consulta")
})
app.post("/cadastrar", function(req, res){
    var result = db.collection('obras').add({
        nome_obra: req.body.nome_obra,
        composer: req.body.composer,
        catalog: req.body.catalog,
        data_lancamento: req.body.data_lancamento,
        descricao: req.body.descricao
    }).then(function(){
        console.log('Added document');
        res.redirect('/')
    })
})
app.post("/atualizar", function(req, res){
    const result = db.collection("obras")
    .doc(req.body.id).update({
        nome_obra: req.body.nome_obra,
        composer: req.body.composer,
        catalog: req.body.catalog,
        data_lancamento: req.body.data_lancamento,
        descricao: req.body.descricao
    })
    res.redirect("/consulta")
})
app.listen(8081, function(){
    console.log("Servidor ativo!")
})