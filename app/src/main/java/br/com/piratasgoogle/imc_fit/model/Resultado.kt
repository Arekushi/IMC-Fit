package br.com.piratasgoogle.imc_fit.model

class Resultado {
    var cod : Int = 0
    var emailUsuario : String? = null
    var classificacao : String? = null
    var altura : Double? = null
    var peso : Int = 0
    var sexo : String? = null
    var imc : Double? = null

    constructor(emailUsuario : String, classificacao : String
                , altura : Double, peso : Int, sexo : String, imc : Double) {

        this.emailUsuario = emailUsuario
        this.classificacao = classificacao
        this.altura = altura
        this.peso = peso
        this.sexo = sexo
        this.imc = imc
    }

    constructor(classificacao: String, imc: Double, sexo: String) {
        this.classificacao = classificacao
        this.imc = imc
        this.sexo = sexo
    }
}