package es.marcmauri.finalapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.others.goToActivity
import es.marcmauri.finalapp.others.toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (mAuth.currentUser == null) {
            toast("Nope")
        } else {
            toast("Yep")
            mAuth.signOut()
        }

        button_logIn.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()
            if (isValidEmailAndPassword(email, password)) {
                logInByEmail(email, password)
            } else {
                toast("Please fill all the data.",
                        Toast.LENGTH_LONG)
            }
        }

        tv_forgotPassword.setOnClickListener { goToActivity<ForgotPasswordActivity>() }
        button_createAccount.setOnClickListener { goToActivity<SignUpActivity>() }
    }

    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("User is now logged in")
            } else {
                toast("The credentials are not valid")
            }
        }
    }

    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return !email.isEmpty() && !password.isEmpty()
    }

}
