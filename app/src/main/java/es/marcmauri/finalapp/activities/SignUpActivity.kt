package es.marcmauri.finalapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.utils.*
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
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        button_createAccount.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()
            val confirmPassword = et_confirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, confirmPassword))
                signUpByEmail(email, password)
            else
                toast("Please make sure all the data is correct.")
        }

        et_email.validate { email ->
            et_email.error = if (isValidEmail(email)) null else "Email is not valid"
        }

        et_password.validate { pwd ->
            et_password.error = if (isValidPassword(pwd)) null else "Password should contain:\n1 lowercase\n1 uppercase\n1 number\n1 special character (@#\\$%^&+=!.)\n4 characters lenght at least"
        }

        et_confirmPassword.validate { confirmPwd ->
            et_confirmPassword.error = if (isValidConfirmPassword(et_password.text.toString(), confirmPwd)) null else "Confirm Password does not match with Password"
        }


    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this) {
                    toast("An email has been sent to you. Please, confirm before sign in")
                }
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                toast("An unexpected error occurred, please try again.")
            }
        }
    }


    //* EVENTS *//

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
