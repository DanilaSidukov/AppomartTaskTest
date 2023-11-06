package com.test.appomarttaskertest.ui.orders

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.test.appomarttaskertest.databinding.DialogChangeStatusBinding
import com.test.appomarttaskertest.domain.OrderStatus
import com.test.appomarttaskertest.ui.asString

class ChangeStatusDialogFragment(
    private val listener: OnStatusChangedListener,
    private val statusList: List<OrderStatus>,
    private val currentOrderId: Int
) : DialogFragment() {

    private lateinit var bindingDialog: DialogChangeStatusBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bindingDialog = DialogChangeStatusBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context)
        val dialog = builder.setView(bindingDialog.root)
        bind()
        return dialog.create()
    }

    private fun bind() = with(bindingDialog) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            statusList.toTypedArray().map {
                it.asString(requireContext())
            }
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        statusSpinner.adapter = adapter

        buttonCancel.setOnClickListener {
            dismiss()
        }
        buttonChange.setOnClickListener {
            listener.onItemSelected(currentOrderId, statusList[statusSpinner.selectedItemPosition])
            dismiss()
        }
    }

}

interface OnStatusChangedListener {
    fun onItemSelected(id: Int, status: OrderStatus)
}