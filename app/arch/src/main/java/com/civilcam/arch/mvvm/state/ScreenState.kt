package com.civilcam.arch.mvvm.state

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.civilcam.arch.common.livedata.SingleLiveEvent
import kotlin.collections.set
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

interface ObservableScreenState {

    operator fun <T> get(prop: KProperty1<*, T>): LiveData<T>

    fun <T> observe(lifecycleOwner: LifecycleOwner, prop: KProperty1<*, T>, observer: (T) -> Unit) =
        this[prop].observe(lifecycleOwner, Observer(observer))
}

open class ScreenState : ObservableScreenState {
    private val propertyMap = mutableMapOf<KProperty1<*, *>, PropertyProvider<*>>()

    @Suppress("UNCHECKED_CAST")
    override operator fun <T> get(prop: KProperty1<*, T>): LiveData<T> =
        propertyMap.getValue(prop).wrapper.liveData as LiveData<T>

    protected fun <T> property(
        value: T? = null,
        requiresEqualsCheck: Boolean = true,
    ) = MutableLiveData<T>().property(value, requiresEqualsCheck)

    protected fun <T> message(default: T? = null, requiresEqualsCheck: Boolean = false) =
        SingleLiveEvent<T>().property(default, requiresEqualsCheck)

    protected fun <T> MutablePropertyProvider<T>.transform(transform: (T?) -> T?) =
        MutablePropertyProvider(wrapper.transform(transform), defaultValue, requiresEqualsCheck)

    protected fun <T, P : List<T>> MutablePropertyProvider<P>.distinct() =
        transform {
            val distinctValues = mutableListOf<T>()
            val oldValue = wrapper.getter() ?: emptyList()
            it?.forEach { value ->
                if (!oldValue.contains(value)) {
                    distinctValues.add(value)
                }
            }
            distinctValues as P
        }

    private fun <T> MutableLiveData<T>.property(
        defaultValue: T?,
        requiresEqualsCheck: Boolean = false
    ) = MutablePropertyProvider(LiveDataWrapper(this), defaultValue, requiresEqualsCheck)

    open inner class PropertyProvider<T>(
        open val wrapper: LiveDataWrapper<T>,
        val defaultValue: T? = null,
    ) {
        @Suppress("UNCHECKED_CAST")
        open operator fun provideDelegate(
            thisRef: ScreenState,
            prop: KProperty<*>,
        ): ReadOnlyProperty<ScreenState, T> {
            val property = prop as? KProperty1<*, T>
                ?: throw IllegalArgumentException("Invalid property type")
            propertyMap[property] = this
            return ReadOnlyProperty { _, _ ->
                (wrapper.getter() ?: defaultValue) as T
            }
        }
    }

    inner class MutablePropertyProvider<T>(
        override val wrapper: LiveDataWrapper<T>,
        defaultValue: T? = null,
        val requiresEqualsCheck: Boolean = false
    ) : PropertyProvider<T>(wrapper, defaultValue) {

        private fun setValue(value: T) {
            if (requiresEqualsCheck && wrapper.getter() == value) {
                return
            }
            wrapper.setter(value)
        }

        var initialValue = defaultValue

        override operator fun provideDelegate(
            thisRef: ScreenState,
            prop: KProperty<*>,
        ): ReadWriteProperty<ScreenState, T> {
            val readOnlyProperty = super.provideDelegate(thisRef, prop)
            initialValue?.let(::setValue)
            return object : ReadWriteProperty<ScreenState, T> {

                override fun setValue(thisRef: ScreenState, property: KProperty<*>, value: T) =
                    setValue(value)

                override fun getValue(thisRef: ScreenState, property: KProperty<*>): T =
                    readOnlyProperty.getValue(thisRef, property)
            }
        }
    }
}

class LiveDataWrapper<T>(
    liveDataProvider: () -> MutableLiveData<T>,
    val getter: () -> T?,
    val setter: (T?) -> Unit
) {
    val liveData by lazy(liveDataProvider)

    constructor(liveData: MutableLiveData<T>) : this(
        liveDataProvider = { liveData },
        getter = liveData::getValue,
        setter = liveData::setValue
    )

    fun transform(transform: (T?) -> T?): LiveDataWrapper<T> =
        LiveDataWrapper(
            liveDataProvider = { liveData },
            getter = liveData::getValue,
            setter = { liveData.value = transform(it) }
        )
}
