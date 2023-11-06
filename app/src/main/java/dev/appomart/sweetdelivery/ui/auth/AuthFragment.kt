package dev.appomart.sweetdelivery.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.appomart.sweetdelivery.ui.orders.OrdersFragment
import dev.appomart.sweetdelivery.ui.showText
import dev.appomart.sweetdelivery.R
import dev.appomart.sweetdelivery.databinding.FragmentAuthBinding

class AuthFragment: Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        bind()
    }

    private fun bind() = with(binding) {

        buttonLogin.setOnClickListener {
            val login = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()

            auth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        openOrdersFragment()
                    } else {
                        requireContext().showText("Authentication failed")
                    }
                }
                .addOnFailureListener { exception ->
                    requireContext().showText("Error: ${exception.localizedMessage}")
                }
        }
    }

    private fun openOrdersFragment(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, OrdersFragment())
            .addToBackStack(null)
            .commit()
    }

}