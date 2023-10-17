package com.example.photobuilder.feature.tekephoto

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.example.photobuilder.databinding.TagsChooserDialogBinding

class ChooseTagDialog(
    context: Context,
    private val chooseTag: (tag: String) -> Unit,
) : Dialog(context) {

    private lateinit var binding: TagsChooserDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        this.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        setOnCancelListener { dismiss() }
        binding = TagsChooserDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}