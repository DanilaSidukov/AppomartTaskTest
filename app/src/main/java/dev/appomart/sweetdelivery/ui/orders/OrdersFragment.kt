package dev.appomart.sweetdelivery.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.appomart.sweetdelivery.R
import dev.appomart.sweetdelivery.data.source.local.Settings
import dev.appomart.sweetdelivery.databinding.FragmentOrdersBinding
import dev.appomart.sweetdelivery.domain.OrderStatus
import dev.appomart.sweetdelivery.ui.auth.AuthFragment
import dev.appomart.sweetdelivery.ui.maps.MapsFragment
import dev.appomart.sweetdelivery.ui.showText
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment: Fragment(), OnChangeOrderStatusListener, OnStatusChangedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var ordersAdapter: OrdersAdapter

    private val ordersViewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
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
            ordersViewModel.uiState.collect { uiState ->
                binding.progressBar.isVisible = uiState.showLoader
                val orderList = uiState.orderList
                if (orderList.isEmpty()) return@collect
                ordersAdapter.updateList(orderList)

                if (uiState.canEditStatus == false) {
                    context?.showText(getString(R.string.error_cannot_change_status))
                }

                val availableStatuses = uiState.availableStatuses
                if (availableStatuses.isNotEmpty() && uiState.canEditStatus == true) {
                    showDialog(availableStatuses, uiState.currentOrderId)
                }

                uiState.isUpdateSuccessful?.let { isSuccessful ->
                    context?.showText(
                        getString(
                            if (isSuccessful) R.string.success_status_changed
                            else R.string.error_changing_status
                        )
                    )
                }
            }
        }
    }

    private fun bind() = with(binding){
        rvOrders.adapter = ordersAdapter
        iconLogOut.setOnClickListener {
            auth.signOut()
            openAuthFragment()
        }
        floatingButtonMap.setOnClickListener {
            openGoogleMaps()
        }
    }

    private fun openAuthFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AuthFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onChangeOrderStatusListener(orderId: Int, orderStatus: OrderStatus) {
        ordersViewModel.requestStatusList(orderId, orderStatus)
    }

    private fun showDialog(statusList: List<OrderStatus>, currentOrderId: Int) {
        val dialogFragment = ChangeStatusDialogFragment(this, statusList, currentOrderId)
        dialogFragment.show(childFragmentManager, null)
    }

    override fun onItemSelected(
        id: Int,
        status: OrderStatus,
        price: Int?,
        commentary: String?
    ) {
        ordersViewModel.updateStatus(
            id,
            status,
            price,
            commentary
        )
    }

    private fun openGoogleMaps(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MapsFragment())
            .addToBackStack( Settings.ORDERS_SCREEN)
            .commit()
    }

}