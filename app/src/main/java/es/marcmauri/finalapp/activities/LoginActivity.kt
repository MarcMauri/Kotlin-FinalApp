package es.marcmauri.finalapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.others.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_logIn.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()
            if (isValidEmail(email) && isValidPassword(password))
                logInByEmail(email, password)
            else
                toast("Please make sure all the data is correct.")
        }

        et_email.validate { email ->
            et_email.error = if (isValidEmail(email)) null else "Email is not valid"
        }

        et_password.validate { pwd ->
            et_password.error = if (isValidPassword(pwd)) null else "Password should contain:\n1 lowercase\n1 uppercase\n1 number\n1 special character (@#\\$%^&+=!.)\n4 characters lenght at least"
        }


        tv_forgotPassword.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        button_createAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("User is now logged in")
                val currentUser = mAuth.currentUser!!
                currentUser.displayName
                currentUser.email
                currentUser.photoUrl
                currentUser.phoneNumber
                currentUser.isEmailVerified
            } else {
                toast("The credentials are not valid")
            }
        }
    }

}
