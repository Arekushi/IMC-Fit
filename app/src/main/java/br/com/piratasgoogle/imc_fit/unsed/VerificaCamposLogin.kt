package br.com.piratasgoogle.imc_fit

import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.Toast

class VerificaCamposLogin (private val context: Context
                           , private val edtLogin: EditText
                           , private val edtSenha: EditText
                           , private val activity: RegisterLoginActivity) {

    private val strLogin = edtLogin.text.trim().toString()
    private val strSenha = edtSenha.text.trim().toString()
    private var bolPasse = true

    fun resposta () {
        /* Se os dois campos estão vazios */
        when (vefCamposVazios()) {
            true -> {

                /* Se Login tem 8 caracteres */
                when (vefCamposLength(strLogin)) {
                    true -> edtLogin.background = context.getDrawable(R.drawable.common_google_signin_btn_icon_dark)
                    false -> {
                        edtLogin.background = context.getDrawable(R.drawable.edit_text_border)
                        bolPasse = false
                    }
                }

                /* Se Senha tem 8 caracteres */
                when(vefCamposLength(strSenha)) {
                    true -> edtSenha.background = context.getDrawable(R.drawable.common_google_signin_btn_icon_dark)
                    false -> {
                        edtSenha.background = context.getDrawable(R.drawable.edit_text_border)
                        bolPasse = false
                    }
                }

                when (bolPasse) {
                    true -> {
                        val shared = SharedPreferences(context)
                        shared.editString(context.getString(R.string.logged), "1")

                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }

                    false -> {

                    }
                }
            }

            false -> {
                edtLogin.background = context.getDrawable(R.drawable.edit_text_border)
                edtSenha.background = context.getDrawable(R.drawable.edit_text_border)

                Toast.makeText(context
                    , "a"
                    , Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun vefCamposVazios () : Boolean {
        return (!(strLogin.isEmpty() && strSenha.isEmpty()))
    }

    private fun vefCamposLength (campo: String) : Boolean {
        return (campo.length > 8)
    }



}