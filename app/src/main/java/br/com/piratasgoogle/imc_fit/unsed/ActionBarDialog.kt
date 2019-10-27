package br.com.piratasgoogle.imc_fit.unsed

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import br.com.piratasgoogle.imc_fit.R

class ActionBarDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val strTitle = arguments?.getString(this.getString(R.string.app_name))
        val mView: View = inflater.inflate(R.layout.dialog_create_imc, container, false)
        val toolbar = mView.findViewById<Toolbar>(R.id.alertToolbar)
        toolbar.title = strTitle
        return mView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog

    }

}