package com.github.motoshige021.hiltdiprac

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingResource
import java.util.*

class DataBindingIdlingResource :IdlingResource {
    private val idlingCallback = mutableListOf<IdlingResource.ResourceCallback>()

    private val id = UUID.randomUUID().toString()

    private var wasNotIdle = false

    lateinit var activity: FragmentActivity


    override fun getName() = "DataBinding $id" // UUID.randomUUID().toString()

    override fun isIdleNow(): Boolean {
        var idle =!getBindings().any { it.hasPendingBindings() }
        if (idle) {
            if (wasNotIdle) {
                idlingCallback.forEach { it.onTransitionToIdle() }
            }
            wasNotIdle = false
        } else {
            wasNotIdle = true
            activity.findViewById<View>(android.R.id.content).postDelayed({
                isIdleNow
            }, 16)
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        callback?.let { callback ->
            idlingCallback.add(callback)
        }
    }

    /**
     * Find all binding classes in all currently available fragments.
     */
    private fun getBindings(): List<ViewDataBinding> {
        val fragments = (activity as? FragmentActivity)
            ?.supportFragmentManager
            ?.fragments

        val bindings =
            fragments?.mapNotNull {
                it.view?.getBinding()
            } ?: emptyList()
        val childrenBindings = fragments?.flatMap { it.childFragmentManager.fragments }
            ?.mapNotNull { it.view?.getBinding() } ?: emptyList()

        return bindings + childrenBindings
    }
}

private fun View.getBinding(): ViewDataBinding? = DataBindingUtil.getBinding(this)

fun DataBindingIdlingResource.monitorActivity(
    activtyScenario: ActivityScenario<out FragmentActivity>
) {
    activtyScenario.onActivity {
        this.activity = it
    }
}

fun <T: Fragment> DataBindingIdlingResource.monitorFragment(
    fragmentScenario: FragmentScenario<T>
) {
    fragmentScenario.onFragment {
        this.activity = it.requireActivity()
    }
}