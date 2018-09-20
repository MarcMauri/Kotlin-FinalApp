package es.marcmauri.finalapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.others.goToActivity
import es.marcmauri.finalapp.others.isValidEmail
import es.marcmauri.finalapp.others.toast
import es.marcmauri.finalapp.others.validate
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        button_goLogIn.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        et_email.validate { email ->
            et_email.error = if (isValidEmail(email)) null else "Email is not valid"
        }

        button_forgot.setOnClickListener {
            val email = et_email.text.toString()
            if (isValidEmail(email)) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    toast("Email has been sent to reset your password.")
                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else {
                toast("Please make sure the email address is correct.")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
