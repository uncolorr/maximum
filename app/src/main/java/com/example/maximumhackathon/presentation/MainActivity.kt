package com.example.maximumhackathon.presentation

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.maximumhackathon.R
import com.example.maximumhackathon.transaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.transaction {
            add(
                R.id.mainContainer,
                SplashFragment(),
                SplashFragment::class.java.name
            )
        }

        val timer = object: CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                supportFragmentManager.transaction {
                    replace(
                        R.id.mainContainer,
                        MainFragment(),
                        MainFragment::class.java.name
                    )
                    addToBackStack(MainFragment::class.java.name)
                }
            }
        }
        timer.start()
    }
}
