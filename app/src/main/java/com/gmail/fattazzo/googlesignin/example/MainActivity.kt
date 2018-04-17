package com.gmail.fattazzo.googlesignin.example

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *
 *
 * date: 16/04/18
 */
@EActivity(R.layout.activity_main)
open class MainActivity : AppCompatActivity() {

    @ViewById
    internal lateinit var statusTV: TextView

    @ViewById
    internal lateinit var googleAccountView: GoogleSignInAccountView

    @ViewById
    internal lateinit var signInButton: SignInButton

    @ViewById
    internal lateinit var signOutAndDisconnectLayout: ConstraintLayout

    internal lateinit var googleSignInClient: GoogleSignInClient

    @AfterViews
    fun initViews() {
        updateInfoView(GoogleSignIn.getLastSignedInAccount(this), false)

        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.google_client_id))
                .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, options)
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateInfoView(account, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Companion.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                updateInfoView(account, false)
            } catch (e: ApiException) {
                updateInfoView(null, true)
            }
        }
    }

    @Click
    protected fun signInButtonClicked() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Companion.RC_SIGN_IN)
    }

    @Click
    protected fun signOutButtonClicked() {
        googleSignInClient.signOut().addOnCompleteListener { updateInfoView(null, false) }
    }

    @Click
    protected fun disconnectButtonClicked() {
        googleSignInClient.revokeAccess().addOnCompleteListener { updateInfoView(null, false) }
    }

    private fun updateInfoView(account: GoogleSignInAccount?, hasError: Boolean) {
        if (account == null) {
            statusTV.text = "Signed out"
            signInButton.visibility = View.VISIBLE
            signOutAndDisconnectLayout.visibility = View.GONE
        } else {
            statusTV.text = "Signed in"
            signInButton.visibility = View.GONE
            signOutAndDisconnectLayout.visibility = View.VISIBLE
        }
        googleAccountView.bind(account)

        if (hasError) {
            statusTV.text = "Error during sing-in process..."
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
