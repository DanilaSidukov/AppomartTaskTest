package dev.appomart.sweetdelivery.ui

import android.content.Context
import android.widget.Toast
import dev.appomart.sweetdelivery.R
import dev.appomart.sweetdelivery.domain.OrderStatus

fun Context.showText(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun OrderStatus.asString(context: Context): String = when (this) {
    OrderStatus.Canceled -> context.getString(R.string.label_canceled)
    OrderStatus.Closed -> context.getString(R.string.label_closed)
    OrderStatus.Finished -> context.getString(R.string.label_finished)
    OrderStatus.InProgress -> context.getString(R.string.label_in_progress)
    else -> context.getString(R.string.label_new)
}