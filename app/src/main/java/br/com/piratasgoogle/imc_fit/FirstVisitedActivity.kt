package br.com.piratasgoogle.imc_fit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_first_visited.*

class FirstVisitedActivity : AppCompatActivity() {

    private val arrayImagens : Array<Int> = arrayOf(
        R.drawable.img01,
        R.drawable.img02,
        R.drawable.img03,
        R.drawable.img04
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_visited)

        crvCarrosel.pageCount = arrayImagens.size
        crvCarrosel.setImageListener(crvCarroselImageListener)

        btnComecar.setOnClickListener(btnComecarclickListener)

    }

    private var btnComecarclickListener = View.OnClickListener {
        val shared = SharedPreferences(this)
        btnComecar.isEnabled = false

        shared.editString (this.getString(R.string.firstEnter), "1")

        val intent = Intent(this, RegisterLoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private var crvCarroselImageListener = ImageListener {
            position: Int, imageView: ImageView ->
            imageView.setImageResource(arrayImagens[position])
    }

}
