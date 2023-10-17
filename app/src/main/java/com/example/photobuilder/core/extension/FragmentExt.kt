package com.example.photobuilder.core.extension

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String) {
    Toast.makeText(
        requireActivity().applicationContext,
        message,
        Toast.LENGTH_SHORT
    ).show()
}
