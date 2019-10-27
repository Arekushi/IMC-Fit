package br.com.piratasgoogle.imc_fit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class DialogTabelaIMC(private val context: Context) {
    private val drawable = R.layout.dialog_tabela

    private val inflater = LayoutInflater.from(this.context)
    private val mView: View = inflater.inflate(drawable, null)

    private val magreza = mView.findViewById<TextView>(R.id.valueMagreza)
    private val normal = mView.findViewById<TextView>(R.id.valueNormal)
    private val acimaDoPeso = mView.findViewById<TextView>(R.id.valueAcimaPeso)
    private val obesidadeI = mView.findViewById<TextView>(R.id.valueObesidadeI)
    private val obesidadeII = mView.findViewById<TextView>(R.id.valueObesidadeII)

    private val masculinoValores = this.context.resources.getStringArray(R.array.valoresMasculino)
    private val femininoValores = this.context.resources.getStringArray(R.array.valoresFeminino)
    private val arrayValores = arrayOf(masculinoValores, femininoValores)

    private lateinit var indiceSexo : String

    fun create(sexo: String) {
        CreateAlertDialog(context, drawable).createAlertDialog(mView)

        when(sexo) {
            "Masculino" -> indiceSexo = "0"
            "Feminino" -> indiceSexo = "1"
        }

        magreza.text = context.getString(
            R.string.abaixoDe
            , arrayValores[Integer.parseInt(indiceSexo)][0]
        )

        normal.text = context.getString(
            R.string.entreValores
            , arrayValores[Integer.parseInt(indiceSexo)][0]
            , arrayValores[Integer.parseInt(indiceSexo)][1]
        )

        acimaDoPeso.text = context.getString(
            R.string.entreValores
            , arrayValores[Integer.parseInt(indiceSexo)][1]
            , arrayValores[Integer.parseInt(indiceSexo)][2]
        )

        obesidadeI.text = context.getString(
            R.string.entreValores
            , arrayValores[Integer.parseInt(indiceSexo)][2]
            , arrayValores[Integer.parseInt(indiceSexo)][3]
        )

        obesidadeII.text = context.getString(
            R.string.acimaDe
            , arrayValores[Integer.parseInt(indiceSexo)][3]
        )

    }
}