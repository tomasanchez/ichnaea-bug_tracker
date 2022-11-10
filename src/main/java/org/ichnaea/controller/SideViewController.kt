package org.ichnaea.controller

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.auth.IchnaeaSessionProvider
import org.ichnaea.core.ui.app.SideNavApp
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.navigation.AppBar
import org.ichnaea.core.ui.navigation.NavItem
import org.ichnaea.core.ui.navigation.SideNav
import org.ichnaea.core.ui.navigation.SubNavItem
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.model.User
import javax.swing.Icon
import javax.swing.ImageIcon

abstract class SideViewController : BaseController() {

    private val app: SideNavApp
        get() = this.view.app as SideNavApp

    protected val sessionProvider: IchnaeaSessionProvider =
        getSecurityContext().sessionManager as IchnaeaSessionProvider

    companion object {

        protected var isMenuInitialized = false

        val AVATAR_ICON: Icon = GoogleIconFactory.build(
            name = GoogleMaterialDesignIcons.ACCOUNT_CIRCLE,
            color = SemanticColor.SECONDARY.darker(),
        )

        var user: User? = null

        const val HOME_NAV = "Projects"

        const val USERS_NAV = "Users"

        const val PROJECT_REPORT_NAV = "Projects Report"

        const val ADD_USER_NAV = "Create User"

        const val END_SESSION_NAV = "Exit"
    }

    override fun show() {

        if (hasUserChanged()) {
            user = sessionProvider.session
            initComponents()
        }

        super.show()
    }


    private fun initComponents() {
        initSideNav()
        initAppBar()
    }

    private fun initAppBar() {
        val appBar: AppBar = app.appBar

        user?.let { it ->
            appBar.username = it.userName.lowercase().replaceFirstChar { username ->
                username
                    .uppercase()
            }
            appBar.role = it.role.name.toString()

            it.image?.let { image ->
                appBar.avatarImage = ImageIcon(javaClass.getResource(image))
            } ?: run {
                appBar.avatarImage = AVATAR_ICON
            }

            appBar.refresh()
        }
    }

    private fun initSideNav() {

        val sideNav: SideNav = app.sideNav

        sideNav.refresh()

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

        if (isUserAdmin()) {
            sideNav.addGroup(
                "Manage",
                NavItem(
                    text = "Admin",
                    icon = GoogleMaterialDesignIcons.VERIFIED_USER,
                    onClear = sideNav::clearSelected,
                    parentLayout = sideNav.menuLayout,
                    subItems = arrayListOf(
                        SubNavItem(
                            name = PROJECT_REPORT_NAV,
                            onClick = { navTo("ProjectReport") }
                        ),
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
        }

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
        sessionProvider.closeSession()
        navTo("SignIn")
    }

    // ------------------------------------------
    // Internal Methods
    // ------------------------------------------

    private fun hasUserChanged(): Boolean {
        val currentUser = sessionProvider.session
        return user == null || user?.id != currentUser?.id
    }

    protected fun isUserAdmin(): Boolean {
        view.model["isAdmin"] = user?.role?.name.toString().equals("ADMIN", true)
        return view.model["isAdmin"] as Boolean
    }


}