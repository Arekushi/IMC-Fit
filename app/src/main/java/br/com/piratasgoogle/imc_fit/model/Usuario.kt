package br.com.piratasgoogle.imc_fit.model

class Usuario {
    var cod : Int = 0
    var nome : String? = null
    var email : String? = null
    var tipoLogin : String? = null

    constructor(nome: String, email: String, tipoLogin : String) {
        this.nome = nome
        this.email = email
        this.tipoLogin = tipoLogin
    }

    constructor(nome: String, email: String) {
        this.nome = nome
        this.email = email
    }
}