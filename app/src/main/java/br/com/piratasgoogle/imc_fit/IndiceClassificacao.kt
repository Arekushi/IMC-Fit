package br.com.piratasgoogle.imc_fit

import android.content.Context

class IndiceClassificacao(private val imc: Double, private val sexo: String, val context: Context) {
    /* Vars */
    private val masculinoValores = context.resources.getStringArray(R.array.valoresMasculino)
    private val femininoValores = context.resources.getStringArray(R.array.valoresFeminino)
    private val arrayValores = arrayOf(masculinoValores, femininoValores)

    private lateinit var indiceClassificacao : String
    private lateinit var indiceSexo : String

    fun retornaIndice() : Int {

        when(sexo) {
            "Masculino" -> {indiceSexo = "0"}
            "Feminino" -> {indiceSexo = "1"}
        }

        @Suppress("LiftReturnOrAssignment")
        if(imc < arrayValores[Integer.parseInt(indiceSexo)][0].toDouble()) {
            indiceClassificacao = "0"

        } else if (imc > arrayValores[Integer.parseInt(indiceSexo)][0].toDouble() &&
            imc < arrayValores[Integer.parseInt(indiceSexo)][1].toDouble()) {
            indiceClassificacao = "1"

        } else if (imc > arrayValores[Integer.parseInt(indiceSexo)][1].toDouble() &&
            imc < arrayValores[Integer.parseInt(indiceSexo)][2].toDouble()) {
            indiceClassificacao = "2"

        } else if (imc > arrayValores[Integer.parseInt(indiceSexo)][2].toDouble() &&
            imc < arrayValores[Integer.parseInt(indiceSexo)][3].toDouble()) {
            indiceClassificacao = "3"

        } else {
            indiceClassificacao = "4"
        }

        return Integer.parseInt(indiceClassificacao)

    }
}