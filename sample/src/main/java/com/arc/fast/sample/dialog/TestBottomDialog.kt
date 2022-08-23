package com.arc.fast.sample.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.arc.fast.core.extensions.color
import com.arc.fast.core.screenHeight
import com.arc.fast.core.util.ImmersiveDialogConfig
import com.arc.fast.sample.R
import com.arc.fast.sample.databinding.DialogTestBottomBinding

class TestBottomDialog : ImmersiveBindingDialog<DialogTestBottomBinding>() {

    override fun createBinding(inflater: LayoutInflater): DialogTestBottomBinding =
        DialogTestBottomBinding.inflate(inflater)

    override val immersiveDialogConfig
        get() = ImmersiveDialogConfig.createBottomDialogConfig().apply {
            height = requireActivity().screenHeight / 2
            backgroundDimEnabled = false
            backgroundColor = R.color.md_theme_light_primary_transparent_26.color
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }
}