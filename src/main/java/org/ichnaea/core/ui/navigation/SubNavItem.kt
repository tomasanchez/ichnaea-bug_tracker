package org.ichnaea.core.ui.navigation

data class SubNavItem(
    val name: String,
    val onClick: () -> Unit = {},
)