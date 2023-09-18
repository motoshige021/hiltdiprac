package com.github.motoshige021.hiltdiprac.util

object EspressoIdlingResource {
    private const val RESOURE = "GLOBAL"

    @JvmField
    val coutingIdlingResource = SimpleCountingIdlingResource(RESOURE)

    fun increment() { coutingIdlingResource.increment() }

    fun decrement() {
        if (!coutingIdlingResource.isIdleNow) {
            coutingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T) : T {
    EspressoIdlingResource.increment()
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement()
    }
}