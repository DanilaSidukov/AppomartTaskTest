package com.test.appomarttaskertest.ui.orders

import android.R
import com.test.appomarttaskertest.R as Res
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.test.appomarttaskertest.databinding.DialogChangeStatusBinding
import com.test.appomarttaskertest.domain.OrderStatus
import com.test.appomarttaskertest.ui.asString
import com.test.appomarttaskertest.ui.showText

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
            if (isEditApplicable(statusList[statusSpinner.selectedItemPosition])){
                listener.onItemSelected(
                    currentOrderId,
                    statusList[statusSpinner.selectedItemPosition],
                    editPrice.text.toString().toIntOrNull(),
                    editCommentary.text?.toString()
                )
                dismiss()
            } else {
                requireContext().showText(
                    requireContext().getString(Res.string.error_fill_all_fields)
                )
            }
        }
    }

    private fun isEditApplicable(status: OrderStatus): Boolean{
        with(bindingDialog){
            if (status == OrderStatus.Closed){
                var isPriceSet = false
                var isCommentarySet = false
                editPrice.text?.toString()?.toIntOrNull()?.let {
                    isPriceSet = it.toString().toInt() > 0
                }
                editCommentary.text.toString().let {
                    println("text = $it")
                    isCommentarySet = it.isNotBlank()|| it.isNotEmpty()
                }
                return isPriceSet && isCommentarySet
            }
            return true
        }
    }
}

interface OnStatusChangedListener {
    fun onItemSelected(
        id: Int,
        status: OrderStatus,
        price: Int?,
        commentary: String?
    )
}