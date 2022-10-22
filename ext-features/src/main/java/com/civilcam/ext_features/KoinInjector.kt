package com.civilcam.ext_features

import org.koin.core.Koin

/**
 * Dedicated mixin to inject dependencies with Koin avoiding KoinExtensionApi
 * Use [class Foo(injector: KoinInjector) : KoinInjector by injector], then inject dependencies as it used to be with KoinComponent
 *
 */
interface KoinInjector {
    val koin: Koin
}

inline fun <reified T : Any> KoinInjector.inject(): Lazy<T> = koin.inject()

class GlobalKoinInjector(override val koin: Koin) : KoinInjector