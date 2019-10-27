package br.com.piratasgoogle.imc_fit.banco

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.altura
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.classificacao
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.codResultado
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.criacaoTabelaResultado
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.criacaoTabelaUsuario
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.emailUsuario
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.emailUsuarioResultado
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.imc
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.nomeBanco
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.nomeUsuario
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.peso
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.sexo
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.tbResultado
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.tbUsuario
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.tipoLogin
import br.com.piratasgoogle.imc_fit.banco.DataBaseOptions.Companion.versaoBanco
import br.com.piratasgoogle.imc_fit.model.Resultado
import br.com.piratasgoogle.imc_fit.model.Usuario

class OpenHelper(context: Context): SQLiteOpenHelper(context, nomeBanco, null, versaoBanco) {

    override fun onCreate(bd: SQLiteDatabase?) {
        bd!!.execSQL(criacaoTabelaUsuario)
        bd.execSQL(criacaoTabelaResultado)
    }

    override fun onUpgrade(bd: SQLiteDatabase?, versaoAntiga: Int, versaoNova: Int) {
        bd!!.execSQL("DROP TABLE IF EXISTS $tbUsuario")
        bd.execSQL("DROP TABLE IF EXISTS $tbResultado")
        onCreate(bd)
    }

    /* Metodos */
    fun adicionarUsuario (usuario: Usuario) {
        val bd = this.writableDatabase
        val values = ContentValues()
        values.put(nomeUsuario, usuario.nome)
        values.put(emailUsuario, usuario.email)
        values.put(tipoLogin, usuario.tipoLogin)
        bd.insert(tbUsuario, null, values)
        bd.close()
    }

    fun adicionarResultado (resultado: Resultado) {
        val bd = this.writableDatabase
        val values = ContentValues()

        values.put(emailUsuarioResultado, resultado.emailUsuario)
        values.put(classificacao, resultado.classificacao)
        values.put(altura, resultado.altura)
        values.put(peso, resultado.peso)
        values.put(sexo, resultado.sexo)
        values.put(imc, resultado.imc)

        bd.insert(tbResultado, null, values)
        bd.close()
    }

    fun existeEmail(email: String) : Boolean {
        val bd = this.readableDatabase
        val query = "SELECT * FROM $tbUsuario WHERE $emailUsuario LIKE?"
        val cursor = bd.rawQuery(query, arrayOf(email))

        return if (cursor.count > 0) {
            cursor.close()
            true
        } else {
            cursor.close()
            false
        }
    }

    fun retornaDadosUsuario(email: String) : List<Usuario> {
        val usuarioList = ArrayList<Usuario>()

        val bd = this.readableDatabase
        val query = "SELECT $nomeUsuario, $emailUsuario FROM $tbUsuario WHERE $emailUsuario LIKE?"
        val cursor = bd.rawQuery(query, arrayOf(email))

        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(cursor.getString(0), cursor.getString(1))
                usuarioList.add(usuario)

            } while (cursor.moveToNext())
        }

        cursor.close()
        return usuarioList
    }

    fun retornaDadosResultado(email: String) : List<Resultado> {
        val resultadoList = ArrayList<Resultado>()

        val bd = this.readableDatabase
        val query = "SELECT $classificacao, $imc, $sexo, $codResultado FROM $tbResultado WHERE $emailUsuario LIKE? ORDER BY $codResultado DESC"
        val cursor = bd.rawQuery(query, arrayOf(email))

        if (cursor.moveToFirst()) {
            do {
                val resultado = Resultado(cursor.getString(0), cursor.getDouble(1), cursor.getString(2))
                resultadoList.add(resultado)

            } while (cursor.moveToNext())
        }

        cursor.close()
        return resultadoList
    }

}