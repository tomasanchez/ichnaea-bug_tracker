package org.ichnaea.form

import org.ichnaea.core.ui.form.Validatable
import javax.swing.JComponent
import javax.swing.text.JTextComponent
import kotlin.reflect.full.memberProperties

abstract class Form {

    fun fields(): List<JComponent> {
        return this::class.memberProperties.map { it.getter.call(this) }.filterIsInstance<JComponent>()
    }

    open fun clear() {

        fields().forEach {

            if (it is JTextComponent)
                it.text = ""

            if (it is Validatable)
                it.setError(false)
        }
    }
}