package org.ichnaea

import org.ichnaea.view.MainFrame
import java.awt.EventQueue

fun main() = run {
    EventQueue.invokeLater(::launch)
}

fun launch() = MainFrame.open()
