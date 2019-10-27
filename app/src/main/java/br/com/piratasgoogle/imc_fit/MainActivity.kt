package br.com.piratasgoogle.imc_fit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import br.com.piratasgoogle.imc_fit.adapter.ListResultadosAdapter
import br.com.piratasgoogle.imc_fit.banco.OpenHelper
import br.com.piratasgoogle.imc_fit.model.Resultado
import br.com.piratasgoogle.imc_fit.model.Usuario
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SwitchDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /* Google */
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    /* Menu */
    private lateinit var accountHeader: AccountHeader
    private lateinit var home : PrimaryDrawerItem
    private lateinit var conta : PrimaryDrawerItem
    private lateinit var modoNoturno : SwitchDrawerItem
    private lateinit var classificar : PrimaryDrawerItem
    private lateinit var sobreNos : PrimaryDrawerItem
    private lateinit var gitHub : PrimaryDrawerItem
    private lateinit var logout : SecondaryDrawerItem
    private lateinit var result : Drawer

    /* Usuario */
    private lateinit var nome : String
    private lateinit var email : String

    /* Banco */
    private lateinit var dbHelper : OpenHelper
    private lateinit var usuario : List<Usuario>
    private lateinit var resultados : List<Resultado>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        setContentView(R.layout.activity_main)

        val fab : FloatingActionButton = findViewById(R.id.flbCriarIMC)
        fab.setOnClickListener(flbCriarIMCClickListener)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val shared = SharedPreferences(this)
        dbHelper = OpenHelper(this)
        usuario = dbHelper.retornaDadosUsuario(shared.returnString(this.getString(R.string.email)))
        resultados = ArrayList()
        atualizarDados()

        nome = usuario[0].nome.toString()
        email = usuario[0].email.toString()

        /* Toolbar */
        toolbar.background = this.getDrawable(R.color.colorPrimary)
        setSupportActionBar(toolbar)

        /* Account Header */
        accountHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withTranslucentStatusBar(true)
            .withTextColorRes(R.color.colorPrimary)
            .withHeaderBackground(R.color.colorBackground)
            .addProfiles(
                ProfileDrawerItem().withName(nome).withEmail(email)

            ).withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
                override fun onProfileChanged (
                    view: View?, profile: IProfile<*>, current: Boolean): Boolean {
                    return false
                }
            }).build()

        /* Itens */
        home = PrimaryDrawerItem()
            .withIdentifier(1)
            .withName(this.getString(R.string.menu_home))
            .withIcon(FontAwesome.Icon.faw_home)
            .withSelectedColorRes(R.color.colorPrimary)
            .withSelectedIconColorRes(R.color.colorBackground)
            .withSelectedTextColorRes(R.color.colorBackground)

        conta = PrimaryDrawerItem()
            .withIdentifier(2)
            .withName(this.getString(R.string.menu_conta))
            .withIcon(FontAwesome.Icon.faw_user_circle)
            .withSelectable(false)

        modoNoturno = SwitchDrawerItem()
            .withIdentifier(3)
            .withName(this.getString(R.string.menu_noturno))
            .withIcon(FontAwesome.Icon.faw_moon)
            .withSelectable(false)
            .withChecked(false)
            .withOnCheckedChangeListener(onCheckedChangeListener)

        classificar = PrimaryDrawerItem()
            .withIdentifier(4)
            .withName(this.getString(R.string.menu_classificar))
            .withIcon(FontAwesome.Icon.faw_star)
            .withSelectable(false)

        sobreNos = PrimaryDrawerItem()
            .withIdentifier(5)
            .withName(this.getString(R.string.menu_sobre))
            .withIcon(FontAwesome.Icon.faw_users)
            .withSelectable(false)

        gitHub = PrimaryDrawerItem()
            .withIdentifier(6)
            .withName(this.getString(R.string.menu_github))
            .withIcon(FontAwesome.Icon.faw_github)
            .withSelectable(false)

        logout = SecondaryDrawerItem()
            .withName(this.getString(R.string.menu_logout))
            .withIcon(FontAwesome.Icon.faw_sign_out_alt)
            .withSelectable(false)
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    @Suppress("NAME_SHADOWING")
                    val shared = SharedPreferences(this@MainActivity)

                    when (shared.returnString(this@MainActivity.getString(R.string.typeLogin))) {
                        "Google" -> signOutGoogle()
                        "Facebook" -> signOutFacebook()
                        "Convidado" -> signOutConvidado()
                    }

                    return false
                }
            })

        result = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withAccountHeader(accountHeader)
            .addDrawerItems(
                home, conta, modoNoturno, classificar, sobreNos, gitHub
            )
            .addStickyDrawerItems(logout)
            .build()
    }

    private fun atualizarDados() {
        val shared = SharedPreferences(this)
        resultados = dbHelper.retornaDadosResultado(shared.returnString(this.getString(R.string.email)))
        val adapter = ListResultadosAdapter(this, this@MainActivity, resultados)
        listResultados.adapter = adapter
    }

    private val onCheckedChangeListener = object : OnCheckedChangeListener {
        override fun onCheckedChanged(drawerItem: IDrawerItem<*>, buttonView: CompoundButton, isChecked: Boolean) {
            /*if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {}*/
        }
    }

    /* Tela IMC */
    private var flbCriarIMCClickListener = View.OnClickListener {
        val alertDialogIMC = DialogCreateIMC(this, this, listResultados)
        alertDialogIMC.create()
    }

    /* Google */
    @Suppress("ObjectLiteralToLambda")
    private fun signOutGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                val signOut = SignOut(this@MainActivity, this@MainActivity)
                signOut.signOut()
            }
        })
    }

    /* Facebook */
    @Suppress("ObjectLiteralToLambda")
    private fun signOutFacebook() {
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/"
            , null, HttpMethod.DELETE, object : GraphRequest.Callback {
                override fun onCompleted(response: GraphResponse?) {
                    val signOut = SignOut(this@MainActivity, this@MainActivity)
                    signOut.signOut()
                    LoginManager.getInstance().logOut()
                }
        }).executeAsync()
    }

    /* Convidado */
    private fun signOutConvidado() {
        val signOut = SignOut(this@MainActivity, this@MainActivity)
        signOut.signOut()
    }

}
