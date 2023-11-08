package dev.appomart.sweetdelivery.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.appomart.sweetdelivery.R
import dev.appomart.sweetdelivery.databinding.ActivityAuthBinding
import dev.appomart.sweetdelivery.ui.orders.OrdersActivity
import dev.appomart.sweetdelivery.ui.showText

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        bind()

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            finish()
            startActivity(
                Intent(
                    this,
                    OrdersActivity::class.java
                )
            )
        }
    }

    private fun bind() = with(binding) {

        buttonLogin.setOnClickListener {
            val login = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()
            if (!checkOnBlankAndNull(login, password)) {
                showText(
                    getString(
                        R.string.please_fill_all_fields
                    )
                )
            } else {
                auth.signInWithEmailAndPassword(login, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            openOrdersActivity()
                        } else {
                            showText(
                                getString(R.string.authentication_failed)
                            )
                        }
                    }
                    .addOnFailureListener { exception ->
                        showText(
                            getString(R.string.error_authentication) + exception.localizedMessage
                        )
                    }
            }
        }
    }

    private fun openOrdersActivity() {
        startActivity(
            Intent(
                this,
                OrdersActivity::class.java
            )
        )
        finish()
    }

    private fun checkOnBlankAndNull(login: String?, password: String?): Boolean {
        val isLoginFilled = !login.isNullOrBlank() && login.isNotEmpty()
        val isPasswordFilled = !password.isNullOrBlank() && password.isNotEmpty()
        return isLoginFilled && isPasswordFilled
    }

}