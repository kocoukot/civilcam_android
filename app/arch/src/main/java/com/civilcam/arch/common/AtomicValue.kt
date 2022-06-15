package com.civilcam.arch.common

import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun atomicInteger(value: Int) = object : ReadWriteProperty<Any, Int> {
    private val atomicInteger: AtomicInteger = AtomicInteger(value)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
        atomicInteger.set(value)

    override fun getValue(thisRef: Any, property: KProperty<*>): Int = atomicInteger.get()
}