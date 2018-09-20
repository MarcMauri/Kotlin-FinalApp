package es.marcmauri.finalapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.others.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient() }
    private val RC_GOOGLE_SIGN_IN = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logInBehavior()

        tv_forgotPassword.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        button_createAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        button_logInGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

    }

    private fun getGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        return GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    private fun logInByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            toast("Signed In by Google!!")
        }
    }

    private fun logInBehavior() {
        et_email.validate { email ->
            et_email.error = if (isValidEmail(email)) null else "Email is not valid"
        }

        et_password.validate { pwd ->
            et_password.error = if (isValidPassword(pwd)) null else "Password should contain:\n1 lowercase\n1 uppercase\n1 number\n1 special character (@#\\$%^&+=!.)\n4 characters lenght at least"
        }

        button_logIn.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()
            if (isValidEmail(email) && isValidPassword(password))
                logInByEmail(email, password)
            else
                toast("Please make sure all the data is correct.")
        }
    }

    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                if (mAuth.currentUser!!.isEmailVerified) {
                    toast("User is now logged in")
                } else {
                    toast("User must confirm email first")
                    mAuth.signOut()
                }
            } else {
                toast("The credentials are not valid")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            RC_GOOGLE_SIGN_IN -> {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result.isSuccess) {
                    val account = result.signInAccount
                    logInByGoogleAccountIntoFirebase(account!!)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("Connection Failed!!")
    }

}
