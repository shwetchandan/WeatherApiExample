package com.example.sm.weatherapiexample.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var vbbinding: VB? = null
    protected val binding: VB
        get() = vbbinding
            ?: throw IllegalStateException("Binding is not initialized")

    abstract fun inflateBinding(layoutInflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vbbinding = inflateBinding(layoutInflater)
        setContentView(vbbinding!!.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        vbbinding = null
    }
}