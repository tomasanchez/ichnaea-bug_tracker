package org.ichnaea.controller

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.app.SideNavApp
import org.ichnaea.core.ui.navigation.NavItem
import org.ichnaea.core.ui.navigation.SideNav
import org.ichnaea.core.ui.navigation.SubNavItem

abstract class SideViewController : BaseController() {

    private val app: SideNavApp
        get() = this.view.app as SideNavApp

    companion object {
        protected var isMenuInitialized = false
        protected var wasUserChanged = true


    }

    override fun show() {

        if (!isMenuInitialized) {
            initComponents()
            isMenuInitialized = true
        }

        super.show()
    }

    private fun initComponents() {

        val sideNav: SideNav = app.sideNav

        sideNav.addGroup(
            "Home",
            NavItem(
                text = "Projects",
                icon = GoogleMaterialDesignIcons.WORK,
                onClear = sideNav::clearSelected,
                parentLayout = sideNav.menuLayout,
                onClickHandler = { navTo("Projects") },
                isMainButton = true
            ).also { it.button.isSelected = true },
        )

        sideNav.addGroup(
            "Manage",
            NavItem(
                text = "Admin",
                icon = GoogleMaterialDesignIcons.BUILD,
                onClear = sideNav::clearSelected,
                parentLayout = sideNav.menuLayout,
                subItems = arrayListOf(
                    SubNavItem(
                        name = "Add User",
                        onClick = { navTo("SignUp") }
                    )
                )
            ),
        )

        sideNav.addGroup(
            "Session",
            NavItem(
                text = "Exit",
                icon = GoogleMaterialDesignIcons.EXIT_TO_APP,
                onClear = sideNav::clearSelected,
                parentLayout = sideNav.menuLayout,
                onClickHandler = { onSignOut() },
                isMainButton = true,
            ),
        )
    }

    // ------------------------------------------
    // Event Handlers
    // ------------------------------------------

    private fun onSignOut() {
        navTo("SignIn")
    }


}