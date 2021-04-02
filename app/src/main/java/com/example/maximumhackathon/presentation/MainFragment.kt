package com.example.maximumhackathon.presentation

import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.presentation.base.BaseFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.lang.StringBuilder
import java.math.BigInteger


class MainFragment: BaseFragment() {

    private lateinit var pagerAdapter: MainViewPagerAdapter

    private val docRef = FirebaseFirestore.getInstance()


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
                RC_SIGN_IN
            )
        } else {
            sendData()
//            showMainUI()
        }

//        val hm = hashMapOf<String, Any>()
//        hm["orderNumber"] = 0
//        hm["name"] = "name"
//        hm["frequency"] = 123
//
//        docRef
//            .collection("words")
//            .add(hm).addOnSuccessListener {
//            Log.i("Save", "Done")
//        }.addOnFailureListener {
//            Log.i("Save", "Error")
//        }

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

                sendData()
//                showMainUI()

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

    private fun sendData(){

        try {
            val obj = JSONObject(loadJSONFromAsset())
            val m_jArry = obj.getJSONArray("data")
            val formList = ArrayList<String>()
            for (i in 0 until m_jArry.length()) {
                val jo_inside = m_jArry.getJSONObject(i)
                Log.d("Details-->", jo_inside.getString("column0"))
                val outData = jo_inside.getString("column0")

                formList.add(outData)
            }

            convertData(formList)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun convertData(inData: List<String>){
        val outData = mutableListOf<Word>()
        val outData2 = mutableListOf<Word>()

        inData.forEachIndexed { index, s ->
            val name = getName(s)
            if (name.length > 2){
                outData.add(
                    Word(
                        0,
                        name,
                        getFrequence(s)
                    )
                )
            } else {
                Log.d("Small word ", "$s , $index")
            }
        }

        for (i in 0..9999){
            outData2.add(
                Word(
                    i + 1,
                    outData[i].name,
                    outData[i].frequency
                )
            )
        }

        Log.d("outData size", "${outData2.size}")

        saveData(outData2)
    }

    private fun saveData(inData: List<Word>){
        inData.forEach {
            val hm = hashMapOf<String, Any>()
            hm["orderNumber"] = it.orderNumber
            hm["name"] = it.name
            hm["translate"] = "***"
            hm["frequency"] = it.frequency

            docRef
                .collection("words")
                .add(hm)
        }
    }

    private fun getName(inData: String): String {
        val sb = StringBuilder()

        inData.forEach {
            if (it.isLetter()){
                sb.append(it)
            }
        }

        return sb.toString()
    }

    private fun getFrequence(inData: String): Long {
        val sb = StringBuilder()

        inData.forEach {
            if (it.isDigit()){
                sb.append(it)
            }
        }

        return sb.toString().toLong()
    }


    private fun loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = activity!!.assets.open("data.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}