package com.wan.core.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 当生命周期结束时，自动将 value 置为空
 */
class AutoClearedValue<T : Any?>(lifecycle: Lifecycle) : ReadWriteProperty<LifecycleOwner, T?> {
    private var _value: T? = null

    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                _value = null
            }
        })
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T? {
        return _value
    }

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T?) {
        _value = value
    }
}

fun <T : Any?> Fragment.autoCleared() = AutoClearedValue<T?>(lifecycle)