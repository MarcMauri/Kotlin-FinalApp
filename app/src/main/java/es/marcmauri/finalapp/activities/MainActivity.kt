package es.marcmauri.finalapp.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.mylibrary.ToolbarActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarToLoad(toolbarView as Toolbar)
    }
}
