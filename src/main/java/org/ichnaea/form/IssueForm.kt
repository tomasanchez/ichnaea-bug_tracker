package org.ichnaea.form

import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.form.TextArea
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.model.Issue

class IssueForm(
    issue: Issue? = null,
) : Form() {

    val title =
        TextField(label = "Title")
            .also {
                it.text = issue?.title
                it.isOpaque = false
            }

    val description =
        TextArea(label = "Description")
            .also {
                it.scroll.isOpaque = false
                it.text = issue?.description
            }

    val estimate =
        TextField(label = "Estimate", type = TextField.Type.NUMBER)
            .also {
                it.text = issue?.estimatedPoints.toString()
                it.isOpaque = false
            }

    val real =
        TextField(label = "Real", type = TextField.Type.NUMBER)
            .also {
                it.text = issue?.realPoints?.toString()
                it.isOpaque = false
            }

    val assignee = TextField(label = "Assignee", required = false).also {
        it.text = issue?.assignee?.userName
        it.isOpaque = false
    }

    val submit = Button(text = "Submit")

    fun values(): Map<String, String> {
        return mapOf(
            "title" to title.text,
            "description" to description.text,
            "estimatedPoints" to estimate.text,
            "realPoints" to real.text,
            "username" to assignee.text,
        )
    }


}