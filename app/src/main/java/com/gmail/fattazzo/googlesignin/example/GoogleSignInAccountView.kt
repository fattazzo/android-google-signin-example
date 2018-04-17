package com.gmail.fattazzo.googlesignin.example

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.squareup.picasso.Picasso
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *
 *
 * date: 16/04/18
 */
@EViewGroup(R.layout.view_google_signin_account)
open class GoogleSignInAccountView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @ViewById
    internal lateinit var accountImage: ImageView

    @ViewById
    internal lateinit var idTV: TextView

    @ViewById
    internal lateinit var displayNameTV: TextView

    @ViewById
    internal lateinit var fullNameTV: TextView

    @ViewById
    internal lateinit var emailTV: TextView

    fun bind(account: GoogleSignInAccount?) {
        idTV.visibility = View.GONE
        displayNameTV.visibility = View.GONE
        fullNameTV.visibility = View.GONE
        emailTV.visibility = View.GONE
        accountImage.visibility = View.GONE

        if (account != null) {
            //user display name
            val personName = account.displayName

            //user first name
            val personGivenName = account.givenName

            //user last name
            val personFamilyName = account.familyName

            //user email id
            val personEmail = account.email

            //user unique id
            val personId = account.id

            //user profile pic
            val personPhoto = account.photoUrl

            idTV.text = "ID ${personId!!}"
            displayNameTV.text = personName.orEmpty()
            fullNameTV.text = "${personGivenName.orEmpty()} ${personFamilyName.orEmpty()}"
            emailTV.text = personEmail.orEmpty()
            Picasso.get().load(personPhoto).fit().placeholder(R.mipmap.ic_launcher_round).into(accountImage)

            idTV.visibility = View.VISIBLE
            displayNameTV.visibility = View.VISIBLE
            fullNameTV.visibility = View.VISIBLE
            emailTV.visibility = View.VISIBLE
            accountImage.visibility = View.VISIBLE
        }
    }
}
