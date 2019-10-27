package br.com.piratasgoogle.imc_fit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val run = Runnable {
            showActivity()
        }

        val durationSec: Long  = (2000)
        Handler().postDelayed(run, durationSec)
    }

    private fun showActivity() {
        val shared = SharedPreferences(this)
        shared.editString(":firstEnter", "1")

        fun foo(firstEnter: Boolean, logged: Boolean) : Intent {
            return when (firstEnter) {
                false -> Intent(this, FirstVisitedActivity::class.java)
                true -> {
                    return when (logged) {
                        false -> Intent(this, RegisterLoginActivity::class.java)
                        true -> Intent(this, MainActivity::class.java)
                    }
                }
            }
        }

        startActivity(foo(
            shared.verificaExistencia(this.getString(R.string.firstEnter))
            , shared.verificaExistencia(this.getString(R.string.logged)))
        )

        finish()
    }
}
