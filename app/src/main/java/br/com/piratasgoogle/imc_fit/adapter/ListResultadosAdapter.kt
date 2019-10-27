package br.com.piratasgoogle.imc_fit.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import br.com.piratasgoogle.imc_fit.IndiceClassificacao
import br.com.piratasgoogle.imc_fit.R
import br.com.piratasgoogle.imc_fit.model.Resultado
import kotlinx.android.synthetic.main.row_layout.view.*

class ListResultadosAdapter (val context: Context, activity: Activity, var listResultados: List<Resultado>): BaseAdapter() {

    private var inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var rowView : View

    private val cores = context.resources.obtainTypedArray(R.array.coresClassificacao)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        rowView = inflater.inflate(R.layout.row_layout, null)

        val imc = listResultados[position].imc!!
        val sexo = listResultados[position].sexo!!

        val objIndiceClassificacao = IndiceClassificacao(imc, sexo, context)
        val indiceClassificacao = objIndiceClassificacao.retornaIndice()

        rowView.txvClassificacao.text = listResultados[position].classificacao
        rowView.txvClassificacaoIMC.text = String.format("%.2f", imc)
        rowView.txvClassificacao.setBackgroundColor(cores.getColor(indiceClassificacao, 0))
        rowView.txvClassificacaoIMC.setBackgroundColor(cores.getColor(indiceClassificacao, 0))

        return rowView
    }

    override fun getItem(position: Int): Any {
        return listResultados[position]
    }

    override fun getItemId(position: Int): Long {
        return listResultados[position].cod.toLong()
    }

    override fun getCount(): Int {
        return listResultados.size
    }

}