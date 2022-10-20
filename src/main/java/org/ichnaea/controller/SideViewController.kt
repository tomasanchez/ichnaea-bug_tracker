package org.ichnaea.controller

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.app.SideNavApp
import org.ichnaea.core.ui.navigation.NavItem
import org.ichnaea.core.ui.navigation.SideNav
import org.ichnaea.core.ui.navigation.SubNavItem

abstract class SideViewController : BaseController() {

    protected val app: SideNavApp
        get() = this.view.app as SideNavApp

    companion object {
        protected var isMenuInitialized = false
        protected var wasUserChanged = true

        const val HOME_NAV = "Projects"

        const val USERS_NAV = "Users"

        const val ADD_USER_NAV = "Create User"

        const val END_SESSION_NAV = "Exit"
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
                text = HOME_NAV,
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
                icon = GoogleMaterialDesignIcons.VERIFIED_USER,
                onClear = sideNav::clearSelected,
                parentLayout = sideNav.menuLayout,
                subItems = arrayListOf(
                    SubNavItem(
                        name = USERS_NAV,
                        onClick = { navTo("Users") }
                    ),
                    SubNavItem(
                        name = ADD_USER_NAV,
                        onClick = { navTo("SignUp") }
                    ),
                )
            ),
        )

        sideNav.addGroup(
            "Session",
            NavItem(
                text = END_SESSION_NAV,
                icon = GoogleMaterialDesignIcons.EXIT_TO_APP,
                onClear = sideNav::clearSelected,
                parentLayout = sideNav.menuLayout,
                onClickHandler = { onSignOut() },
                isMainButton = true,
            ),
        )
    }

    /**
     * Updates nav selection
     *
     * @param name of the nav item
     */
    fun updateNavSelection(name: String) {
        val sideNav: SideNav = app.sideNav
        sideNav.clearSelected()
        sideNav.setSelectedByText(name)
    }

    // ------------------------------------------
    // Event Handlers
    // ------------------------------------------

    private fun onSignOut() {
        navTo("SignIn")
    }

    // ------------------------------------------
    // Internal Methods
    // ------------------------------------------


}