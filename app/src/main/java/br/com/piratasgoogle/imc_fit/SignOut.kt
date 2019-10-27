package br.com.piratasgoogle.imc_fit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SignOut(private val context: Context, private val activity: Activity) {

    fun signOut() {
        val shared = SharedPreferences(context)

        shared.removeString(context.getString(R.string.logged))
        shared.removeString(context.getString(R.string.typeLogin))

        val intent = Intent(context, RegisterLoginActivity::class.java)
        context.startActivity(intent)
        activity.finish()

        Toast.makeText(context
            , context.getString(R.string.logout)
            , Toast.LENGTH_SHORT).show()
    }
}