package com.test.appomarttaskertest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.appomarttaskertest.R
import com.test.appomarttaskertest.ui.auth.AuthFragment
import com.test.appomarttaskertest.ui.orders.OrdersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, OrdersFragment()).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, AuthFragment()).commit()
        }

    }

}