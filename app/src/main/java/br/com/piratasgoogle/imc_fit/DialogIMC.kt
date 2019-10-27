package br.com.piratasgoogle.imc_fit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import br.com.piratasgoogle.imc_fit.banco.OpenHelper
import br.com.piratasgoogle.imc_fit.model.Resultado

class DialogIMC(private val context: Context) {
    private val drawable = R.layout.dialog_imc

    private val inflater = LayoutInflater.from(this.context)
    private val mView: View = inflater.inflate(drawable, null)

    private val txvIMC = mView.findViewById<TextView>(R.id.txvIMC)
    private val txvClassificacao = mView.findViewById<TextView>(R.id.classificacaoToolbar)

    private val cores = this.context.resources.obtainTypedArray(R.array.coresClassificacao)
    private val classificacao = this.context.resources.getStringArray(R.array.classificacoes)

    private lateinit var dbHelper : OpenHelper

    fun create(sexo: String, peso: Double, altura: Double) {
        CreateAlertDialog(context, drawable).createAlertDialog(mView)
        dbHelper = OpenHelper(context)

        val imc: Double = (peso / (altura * altura))

        txvIMC.text = context.getString(R.string.imc, String.format("%.2f", imc))

        val objIndiceClassificacao = IndiceClassificacao(imc, sexo, context)
        val indiceClassificacao = objIndiceClassificacao.retornaIndice()

        val shared = SharedPreferences(context)

        dbHelper.adicionarResultado(
            Resultado(
                shared.returnString(context.getString(R.string.email))
                , classificacao[indiceClassificacao]
                , altura
                , peso.toInt()
                , sexo
                , imc
            )
        )

        txvClassificacao.text = classificacao[indiceClassificacao]
        txvClassificacao.setBackgroundColor(cores.getColor(indiceClassificacao, 0))

    }
}