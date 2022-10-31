package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.ScrollContainer
import org.ichnaea.core.ui.container.TabContainer
import org.ichnaea.core.ui.container.TransparentPanel
import org.ichnaea.core.ui.form.TextArea
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import org.ichnaea.model.Project
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JPanel
import javax.swing.JScrollPane

@UIView
class ProjectDetailsView : SideView() {

    private var project: Project? = null

    private lateinit var tabs: TabContainer

    private val membersScrollPanel: JScrollPane = JScrollPane()

    private val issuesScrollPanel: JScrollPane = JScrollPane()

    private val addMemberButton = Button(text = "Add Member")

    private val userIDInput: TextField = TextField(label = "Username").also { it.isOpaque = false }

    private val userForm: JPanel = TransparentPanel()

    private val issueForm: IssueForm = IssueForm()

    class IssueForm(
        val title: TextField = TextField(label = "Title").also { it.isOpaque = false },
        val descriptionScroll: ScrollContainer = ScrollContainer().also { it.isOpaque = false },
        val description: TextArea = TextArea(label = "Description", scroll = descriptionScroll).also {
            it.background = SemanticColor.LIGHT
        },
        val points: TextField = TextField(label = "Estimated Story Points").also { it.isOpaque = false },
        val assignee: TextField = TextField(label = "Assignee", required = false).also { it.isOpaque = false },
        val submit: Button = Button(text = "Report", color = SemanticColor.PRIMARY),
    ) {
        fun clear() {
            title.text = ""
            description.text = ""
            points.text = ""
            assignee.text = ""

            title.setError(false)
            description.setError(false)
            points.setError(false)
            assignee.setError(false)
        }
    }

    init {
        model["addMemberButton"] = addMemberButton
        model["userForm"] = userForm
        model["userIdInput"] = userIDInput
        model["issueForm"] = issueForm
    }

    private fun onBeforeRendering() {
        containerPanel.layout = MigLayout(
            "fill",
            "0[]10[100%, fill]0",
            "0[fill, top]0"
        )
        header()
        body()
    }

    override fun repaint() {
        containerPanel.removeAll()
        super.repaint()
        onBeforeRendering()
    }

    // ---------------------------------------------------------------------------------------------
    // View Drawing
    // ---------------------------------------------------------------------------------------------

    private fun header() {

        project = model["project"] as Project?

        project?.let {

            val title = Title(text = it.name, level = TitleLevel.H3)
            containerPanel.add(title, "align left, wrap")
            val description =
                Typography(text = it.description, color = SemanticColor.DARK.brighter())
            containerPanel.add(description, "align right, growx, wrap")
            val date = Typography(text = "<b>Created at:</b> ${it.creationDate}", size = 12f)
            containerPanel.add(date, "align right, growx, wrap")
        }

    }

    private fun body() {

        containerPanel.add(
            Box.createRigidArea(
                Dimension(200, 50)
            ),
            "align center, w 300!, growx, wrap"
        )

        createTab()
        containerPanel.add(tabs, "align center, growx,  spanx, wrap")

    }

    // ---------------------------------------------------------------------------------------------
    // Components
    // ---------------------------------------------------------------------------------------------

    private fun createTab() {
        tabs = TabContainer()
        issueTab()
        membersTab()
        addIssueTab()
    }

    private fun issueTab() {

        // Tab Container
        val tab = tabPanel()
        val icon = GoogleIconFactory.build(
            name = GoogleMaterialDesignIcons.BUG_REPORT,
            color = SemanticColor.DARK,
            size = 24f
        )

        // Table
        val columns = arrayOf("Id", "Name", "Assignee", "SP", "Real SP", "Status")
        val issuesTable = table(scrollPanel = issuesScrollPanel, columns = columns, showHeader = true).also {
            it.columnModel.getColumn(0).maxWidth = 40
            it.columnModel.getColumn(1).minWidth = 130
            it.columnModel.getColumn(2).minWidth = 100
            it.columnModel.getColumn(3).maxWidth = 60
            it.columnModel.getColumn(4).maxWidth = 100
        }

        model["issuesTable"] = issuesTable
        val tableHeader = issuesTable.tableHeader
        val listTitle = Title(text = "Issues", level = TitleLevel.H4)
        model["issuesTitle"] = listTitle
        tab.add(listTitle, "align left, wrap")
        tab.add(tableHeader, "align center, growx, wrap")
        tab.add(issuesScrollPanel, "align center, growx, wrap")
        tabs.addTab("Issues", icon, tab)
    }

    private fun membersTab() {
        val tab = tabPanel()

        val columns = arrayOf("Id", "Name", "Actions")
        val membersTable = table(scrollPanel = membersScrollPanel, columns = columns).also {

            it.columnModel.getColumn(2).minWidth = 10
            it.columnModel.getColumn(2).preferredWidth = 20
            it.columnModel.getColumn(2).maxWidth = 35

        }

        val listTitle = Title(text = "Members", level = TitleLevel.H4)
        model["membersTitle"] = listTitle
        tab.add(listTitle, "align left, wrap")
        tab.add(
            Typography(text = "Admins are not listed", color = SemanticColor.SECONDARY, size = 12f),
            "align left, wrap"
        )
        model["membersTable"] = membersTable
        tab.add(membersScrollPanel, "align center, height 100:n:n, growx, growy, wrap")


        tab.add(registerForm(), "align center, growx, wrap")
        tabs.addTab("Members", tab)
    }

    private fun addIssueTab() {
        val tab = tabPanel()
        val tabTitle = Title(text = "Report an Issue", level = TitleLevel.H4)

        issueForm.clear()
        tab.add(tabTitle, "align left, wrap")
        tab.add(issueForm.title, "align center, wrap")
        tab.add(issueForm.descriptionScroll, "align center, w 300!, wrap")
        tab.add(issueForm.points, "align center, wrap")
        tab.add(issueForm.assignee, "align center, wrap")
        tab.add(issueForm.submit, "align center, h 45!, w 150!, wrap")

        tabs.addTab("Report Issue", tab)
    }

    // ---------------------------------------------------------------------------------------------
    // Internal Methods
    // ---------------------------------------------------------------------------------------------

    private fun tabPanel(): JPanel {
        return JPanel().also {
            it.background = SemanticColor.LIGHT
            it.layout = MigLayout(
                "fill",
                "15[100%, fill]15",
                "15[fill]15"
            )
        }
    }

    private fun registerForm(): JPanel {
        userForm.removeAll()
        val container = userForm

        container.layout = MigLayout(
            "fill",
            "5[100%, fill]25",
            "0[fill]0"
        )

        container.add(Title(text = "Add Member", level = TitleLevel.H4), "align center, wrap")
        container.add(
            Typography(text = "Enter an existing Username", color = SemanticColor.SECONDARY, size = 12f),
            "align center, wrap"
        )

        val form = JPanel()

        form.isOpaque = false
        form.layout = MigLayout(
            "fill",
            "0[100%, fill]0",
            "15[fill]15"
        )


        form.add(userIDInput, "align center, growx, wrap")
        form.add(addMemberButton, "align center, h 40!, w 150!, wrap")
        container.add(form, "align center, growx, growy, wrap")

        return userForm
    }

}