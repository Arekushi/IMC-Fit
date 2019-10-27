package br.com.piratasgoogle.imc_fit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class CreateAlertDialog(private val context: Context, drawable: Int) {

    private val inflater = LayoutInflater.from(context)
    private val mView: View = inflater.inflate(drawable, null)

    private lateinit var dialog: AlertDialog

    fun createAlertDialog() {
        val mBuild: AlertDialog.Builder = AlertDialog.Builder(context)
        mBuild.setView(mView)

        dialog = mBuild.show()
    }

    fun createAlertDialog(mView : View) {
        val mBuild: AlertDialog.Builder = AlertDialog.Builder(context)
        mBuild.setView(mView)

        dialog = mBuild.show()
    }

    fun closeAlertDialog() {
        dialog.dismiss()
    }
}