package dev.appomart.sweetdelivery.ui.orders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.appomart.sweetdelivery.R

@AndroidEntryPoint
class OrdersActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        auth = Firebase.auth

        openOrdersFragment()

    }

    private fun openOrdersFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, OrdersFragment())
            .commit()
    }

}