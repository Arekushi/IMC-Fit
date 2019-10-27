package br.com.piratasgoogle.imc_fit

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import br.com.piratasgoogle.imc_fit.adapter.ListResultadosAdapter
import br.com.piratasgoogle.imc_fit.banco.OpenHelper
import br.com.piratasgoogle.imc_fit.model.Resultado

class DialogCreateIMC(private val context: Context
                      , private val activity: Activity
                      , private val listResultados: ListView) {

    private val drawable = R.layout.dialog_create_imc

    private val inflater = LayoutInflater.from(this.context)
    private val mView: View = inflater.inflate(drawable, null)

    private val seekPeso = mView.findViewById<SeekBar>(R.id.skbPeso)
    private val seekAltura = mView.findViewById<SeekBar>(R.id.skbAltura)
    private val txvSeekPeso = mView.findViewById<EditText>(R.id.txvSeekPeso)
    private val txvSeekAltura = mView.findViewById<EditText>(R.id.txvSeekAltura)
    private val btnCalcular = mView.findViewById<Button>(R.id.btnCalcular)
    private val btnTabela = mView.findViewById<Button>(R.id.btnTabela)
    private val spnSexo = mView.findViewById<Spinner>(R.id.spnSexo)
    private val hint = mView.findViewById<LinearLayout>(R.id.ll_hint_spinner)

    private val createAlertDialog = CreateAlertDialog(this.context, drawable)

    /* Banco */
    private lateinit var dbHelper : OpenHelper
    private lateinit var resultados : List<Resultado>

    fun create() {
        createAlertDialog.createAlertDialog(mView)
        dbHelper = OpenHelper(context)

        /* SeekBar */
        seekPeso.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txvSeekPeso.setText(progress.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        seekAltura.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val progressDecimal : Double = (progress.toDouble() / 100)

                    if (progressDecimal != 1.0 && progressDecimal != 2.0 && progressDecimal != 0.0) {
                        txvSeekAltura.setText(progressDecimal.toString().replace(",", "."))
                    } else {
                        txvSeekAltura.setText(progressDecimal.toInt().toString())
                    }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        /* EditText SeekBar */
        txvSeekPeso.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, position: Int, p2: Int, p3: Int) {
                if (s!!.isNotEmpty()) {
                    seekPeso.progress = s.toString().toInt()
                    txvSeekPeso.requestFocus()
                    txvSeekPeso.setSelection(txvSeekPeso.length())
                }
            }

        })

        txvSeekAltura.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s!!.isNotEmpty()) {
                    seekAltura.progress = (s.toString().toDouble() * 100).toInt()
                    txvSeekAltura.requestFocus()
                    txvSeekAltura.setSelection(txvSeekAltura.length())
                }
            }

        })

        /* Buttons */
        btnCalcular!!.setOnClickListener {
            if (hint.visibility == View.VISIBLE) {
                selecioneSexo()

            } else {
                val alertIMC = DialogIMC(context)
                alertIMC.create(
                    spnSexo.selectedItem.toString()
                    , seekPeso.progress.toDouble()
                    , (seekAltura.progress.toDouble() / 100)
                )

                atualizarDados()
                createAlertDialog.closeAlertDialog()
            }
        }

        btnTabela!!.setOnClickListener {
            if (hint.visibility == View.VISIBLE) {
                selecioneSexo()

            } else {
                val alertTabelaIMC = DialogTabelaIMC(context)
                alertTabelaIMC.create(spnSexo.selectedItem.toString())
                createAlertDialog.closeAlertDialog()
            }
        }

        /* Spinner */
        val adaptador = ArrayAdapter<String>(context
            , android.R.layout.simple_spinner_dropdown_item
            , context.resources.getStringArray(R.array.spnSexo)
        )
        spnSexo!!.adapter = adaptador

        hint.setOnClickListener {
            spnSexo.performClick()
            setLinearVisibility(false)
            hint.isEnabled = false

            spnSexo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    spnSexo.setSelection(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    setLinearVisibility(true)
                }
            }
        }
    }

    private fun setLinearVisibility(visible: Boolean) {
        if (visible) {
            hint.visibility = View.VISIBLE
        } else {
            hint.visibility = View.INVISIBLE
        }
    }

    private fun atualizarDados() {
        val shared = SharedPreferences(context)
        resultados = dbHelper.retornaDadosResultado(shared.returnString(context.getString(R.string.email)))
        val adapter = ListResultadosAdapter(context, activity, resultados)
        listResultados.adapter = adapter
    }

    private fun selecioneSexo() {
        Snackbar.make(mView, context.getString(R.string.selecioneSexo), Snackbar.LENGTH_LONG).show()
    }
}