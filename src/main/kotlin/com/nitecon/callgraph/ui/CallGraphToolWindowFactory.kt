package com.nitecon.callgraph.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JLabel
import javax.swing.SwingConstants

/**
 * Factory for creating the Call Graph tool window
 */
class CallGraphToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        
        // Create initial empty content
        val welcomePanel = JLabel(
            "<html><center>" +
            "<h2>C++ Call Graph</h2>" +
            "<p>Right-click on a C++ class, function, or method<br>" +
            "and select 'Show Call Graph' to analyze its dependencies.</p>" +
            "<br>" +
            "<p><b>Features:</b></p>" +
            "<ul>" +
            "<li>• View inbound calls (who calls this)</li>" +
            "<li>• View outbound calls (what this calls)</li>" +
            "<li>• Navigate through code relationships</li>" +
            "<li>• Understand code dependencies</li>" +
            "</ul>" +
            "</center></html>",
            SwingConstants.CENTER
        )
        
        val content = contentFactory.createContent(welcomePanel, "Welcome", false)
        toolWindow.contentManager.addContent(content)
    }
}