package br.com.piratasgoogle.imc_fit.banco

@Suppress("unused", "MemberVisibilityCanBePrivate")
class DataBaseOptions {

    companion object {
        const val nomeBanco = "imc.db"
        const val versaoBanco = 4

        /* TABLES */
        const val tbUsuario = "tbUsuario"
            const val codUsuario = "codUsuario"
            const val nomeUsuario = "nomeUsuario"
            const val emailUsuario = "emailUsuario"
            const val tipoLogin = "tipoLogin"

        const val tbResultado = "tbResultado"
            const val codResultado = "codResultado"
            const val emailUsuarioResultado = "emailUsuario"
            const val classificacao = "classificacao"
            const val altura = "altura"
            const val peso = "peso"
            const val sexo = "sexo"
            const val imc = "imc"

        /* CREATE TABLE QUERY */
        const val criacaoTabelaUsuario =
            ("CREATE TABLE $tbUsuario (" +
                "$codUsuario INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", $nomeUsuario TEXT" +
                ", $emailUsuario TEXT" +
                ", $tipoLogin TEXT" +
            ")")

        const val criacaoTabelaResultado =
            ("CREATE TABLE $tbResultado (" +
                "$codResultado INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", $emailUsuarioResultado TEXT" +
                ", $classificacao TEXT" +
                ", $altura REAL" +
                ", $peso INTEGER" +
                ", $sexo TEXT" +
                ", $imc REAL" +
                ", FOREIGN KEY($emailUsuarioResultado) REFERENCES $tbUsuario($emailUsuario)" +
            ")")
    }
}