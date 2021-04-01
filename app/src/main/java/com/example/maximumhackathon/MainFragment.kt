package com.example.maximumhackathon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: BaseFragment() {

    private lateinit var pagerAdapter: MainViewPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser
        Log.i("Auth", "Auth current user ${user?.providerData?.first()?.displayName}")
        if (user == null){
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.EmailBuilder().build()))
                    .setTheme(R.style.Theme_MaximumHackathon)
                    .build(),
                RC_SIGN_IN)
        } else {
            showMainUI()
        }
    }

    // [START auth_fui_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                Log.i("Auth", "Auth done with user $user")

                showMainUI()

            } else {
                Log.i("Auth", "Failed with ${response?.error?.errorCode}")
            }
        }
    }

    private fun showMainUI(){
        pagerAdapter = MainViewPagerAdapter(requireFragmentManager(), requireContext())
        viewPager.adapter = pagerAdapter
        tabsLayout.setupWithViewPager(viewPager)
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}