package es.marcmauri.finalapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.others.goToActivity
import es.marcmauri.finalapp.others.toast
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        button_goLogIn.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }

        button_signUp.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (isValidEmailAndPassword(email, password)) {
                signUpByEmail(email, password)
            } else {
                toast("Please fill all the data and confirm password is correct.",
                        Toast.LENGTH_LONG)
            }
        }

    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("An email has been sent to you. Please, confirm before sign in", Toast.LENGTH_LONG)
            } else {
                toast("An unexpected error occurred, please try again.")
            }
        }
    }

    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                password == et_confirmPassword.text.toString()
    }
}
