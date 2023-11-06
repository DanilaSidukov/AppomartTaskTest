package dev.appomart.sweetdelivery.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.appomart.sweetdelivery.ui.auth.AuthFragment
import dev.appomart.sweetdelivery.ui.orders.OrdersFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.appomart.sweetdelivery.R

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