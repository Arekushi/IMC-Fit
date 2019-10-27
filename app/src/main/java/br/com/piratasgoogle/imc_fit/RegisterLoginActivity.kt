package br.com.piratasgoogle.imc_fit

import android.content.Intent
import kotlinx.android.synthetic.main.activity_register_login.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.piratasgoogle.imc_fit.banco.OpenHelper
import br.com.piratasgoogle.imc_fit.model.Usuario
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import org.json.JSONException
import org.json.JSONObject
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class RegisterLoginActivity : AppCompatActivity() {

    /* Google */
    @Suppress("PrivatePropertyName")
    private var RC_SIGN_IN: Int = 0
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    /* Facebook */
    private lateinit var callBack : CallbackManager

    /* Banco */
    private lateinit var dbHelper : OpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_login)

        dbHelper = OpenHelper(this)
        btnConvidado.setOnClickListener(btnConvidadoClickListener)

        /* Google */
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        btnGoogleLoginIn.setOnClickListener(btnGoogleLoginInClickListener)

        /* Facebook */
        callBack = CallbackManager.Factory.create()
        btnFacebook.setPermissions(arrayListOf("email", "public_profile"))
        btnFacebook.registerCallback(callBack, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                loadUserProfile(AccessToken.getCurrentAccessToken())

                Toast.makeText(
                    this@RegisterLoginActivity
                    , this@RegisterLoginActivity.getString(R.string.sucess)
                    , Toast.LENGTH_SHORT
                ).show()

                val shared = SharedPreferences(this@RegisterLoginActivity)
                shared.editString(this@RegisterLoginActivity.getString(R.string.logged), "1")
                shared.editString(this@RegisterLoginActivity.getString(R.string.typeLogin), this@RegisterLoginActivity.getString(R.string.facebook))

                val intent = Intent(this@RegisterLoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onCancel() {
                Toast.makeText(
                    this@RegisterLoginActivity
                    , this@RegisterLoginActivity.getString(R.string.cancel)
                    , Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(
                    this@RegisterLoginActivity
                    , this@RegisterLoginActivity.getString(R.string.error)
                    , Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private val btnConvidadoClickListener = View.OnClickListener {
        val shared = SharedPreferences(this)
        btnConvidado.isEnabled = false

        shared.editString(this@RegisterLoginActivity.getString(R.string.typeLogin), this@RegisterLoginActivity.getString(R.string.convidadoName))
        shared.editString(this@RegisterLoginActivity.getString(R.string.logged), "1")
        shared.editString(this@RegisterLoginActivity.getString(R.string.email), this@RegisterLoginActivity.getString(R.string.convidadoEmail))

        when(dbHelper.existeEmail(this@RegisterLoginActivity.getString(R.string.convidadoEmail))) {
            false -> {
                dbHelper.adicionarUsuario(Usuario(this@RegisterLoginActivity.getString(R.string.convidadoName)
                    , this@RegisterLoginActivity.getString(R.string.convidadoEmail), this@RegisterLoginActivity.getString(R.string.convidadoName)))
            }
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, this.getString(R.string.convidado), Toast.LENGTH_LONG).show()
    }

    /* Google */
    private var btnGoogleLoginInClickListener = View.OnClickListener {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /* Result */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBack.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                when(dbHelper.existeEmail(account!!.email!!)) {
                    false -> {
                        dbHelper.adicionarUsuario(Usuario(account.displayName!!, account.email!!, this@RegisterLoginActivity.getString(R.string.google)))
                    }
                }

                val shared = SharedPreferences(this)
                shared.editString(this.getString(R.string.logged), "1")
                shared.editString(this.getString(R.string.typeLogin), this@RegisterLoginActivity.getString(R.string.google))
                shared.editString(this@RegisterLoginActivity.getString(R.string.email), account.email!!)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, this.getString(R.string.sucess), Toast.LENGTH_LONG).show()

            } catch (e: ApiException) {
                Toast.makeText(this, this.getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        }
    }

    /* Facebook */
    private fun loadUserProfile(newAccessToken: AccessToken){
        val request = GraphRequest.newMeRequest(
                newAccessToken, GraphRequest.GraphJSONObjectCallback { `object`, _ ->
                    //getData(`object`)
                    return@GraphJSONObjectCallback
                }
            )

        val parameters = Bundle()
        parameters.putString("fields", "name, email, id")
        request.parameters = parameters
        request.executeAsync()
    }

    /*private fun getData(`object`: JSONObject?) {
        try {
            val profilePicture = URL("https://graph.facebook.com/${`object`?.getString("id")}/picture?width=250&height=250")
            val profileName = `object`!!.getString("name")
            val profileEmail = `object`.getString("email")

            when(dbHelper.existeEmail(profileEmail)) {
                false -> {
                    dbHelper.adicionarUsuario(Usuario(profileName, profileEmail, this@RegisterLoginActivity.getString(R.string.facebook)))
                }
            }

            Picasso.get().load(profilepicture.toString()).into(imvAvatar)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }*/
}
