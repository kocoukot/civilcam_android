package com.civilcam.arch.presentation

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.civilcam.arch.mvvm.viewmodel.StatefulViewModel
import kotlin.reflect.KProperty1

abstract class StatefulFragment<VM : StatefulViewModel<*>>(@LayoutRes layout: Int) :
    Fragment(layout) {
    protected abstract val viewModel: VM

    override fun onStart() {
        super.onStart()
        viewModel.attach()
    }

    override fun onStop() {
        viewModel.detach()
        super.onStop()
    }

    protected fun <T> observeScreenState(property: KProperty1<*, T>, action: (T) -> Unit) =
        viewModel.state[property].observe(viewLifecycleOwner, action)
}