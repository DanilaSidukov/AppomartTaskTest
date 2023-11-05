package com.test.appomarttaskertest.ui.orders

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.test.appomarttaskertest.R
import com.test.appomarttaskertest.databinding.ChangeStatusDialogBinding
import com.test.appomarttaskertest.databinding.OrdersFragmentBinding
import com.test.appomarttaskertest.ui.auth.AuthFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment: Fragment(), AdapterView.OnItemSelectedListener, OnChangeOrderStatusListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var binding: OrdersFragmentBinding
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var bindingDialog: ChangeStatusDialogBinding

    private val ordersViewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrdersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore
        ordersAdapter = OrdersAdapter(emptyList(), this)

        bind()
        collectState()

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            ordersViewModel.listOfOrders.collect{ list ->
                if (list.isEmpty()) return@collect
                ordersAdapter.updateList(list)
            }
        }
    }

    private fun bind() = with(binding){
        rvOrders.adapter = ordersAdapter
        iconLogOut.setOnClickListener {
            auth.signOut()
            openAuthFragment()
        }
    }

    private fun openAuthFragment(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AuthFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onChangeOrderStatusListener(orderStatus: String) {
        bindingDialog = ChangeStatusDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        val dialog = builder.setView(bindingDialog.root)
        dialog.show()
    }

}