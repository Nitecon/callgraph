package com.nitecon.callgraph.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.treeStructure.Tree
import com.nitecon.callgraph.model.CallGraph
import com.nitecon.callgraph.model.CallGraphEdge
import com.nitecon.callgraph.model.CallGraphNode
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel

/**
 * Panel for displaying call graph in a tree structure
 */
class CallGraphPanel(
    private val callGraph: CallGraph,
    private val project: Project
) : JPanel(BorderLayout()) {

    private val rootTreeNode = DefaultMutableTreeNode(callGraph.rootNode)
    private val treeModel = DefaultTreeModel(rootTreeNode)
    private val tree = Tree(treeModel)

    init {
        setupUI()
        populateTree()
    }

    private fun setupUI() {
        // Create main layout
        val mainPanel = JPanel(BorderLayout())
        
        // Add title
        val titleLabel = JLabel("Call Graph: ${callGraph.rootNode.name}")
        titleLabel.font = titleLabel.font.deriveFont(16f)
        titleLabel.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        mainPanel.add(titleLabel, BorderLayout.NORTH)

        // Setup tree
        tree.cellRenderer = CallGraphTreeCellRenderer()
        tree.rootVisible = true
        tree.showsRootHandles = true
        
        // Add tree in scroll pane
        val scrollPane = JBScrollPane(tree)
        scrollPane.preferredSize = Dimension(400, 300)
        mainPanel.add(scrollPane, BorderLayout.CENTER)

        // Add statistics panel
        val statsPanel = createStatisticsPanel()
        mainPanel.add(statsPanel, BorderLayout.SOUTH)

        add(mainPanel, BorderLayout.CENTER)
    }

    private fun populateTree() {
        // Add inbound calls
        if (callGraph.inboundCalls.isNotEmpty()) {
            val inboundNode = DefaultMutableTreeNode("Inbound Calls (${callGraph.inboundCalls.size})")
            rootTreeNode.add(inboundNode)
            
            callGraph.inboundCalls.forEach { edge ->
                val callNode = DefaultMutableTreeNode(edge)
                inboundNode.add(callNode)
            }
        }

        // Add outbound calls
        if (callGraph.outboundCalls.isNotEmpty()) {
            val outboundNode = DefaultMutableTreeNode("Outbound Calls (${callGraph.outboundCalls.size})")
            rootTreeNode.add(outboundNode)
            
            callGraph.outboundCalls.forEach { edge ->
                val callNode = DefaultMutableTreeNode(edge)
                outboundNode.add(callNode)
            }
        }

        // Expand all nodes by default
        for (i in 0 until tree.rowCount) {
            tree.expandRow(i)
        }
        
        treeModel.reload()
    }

    private fun createStatisticsPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        panel.border = BorderFactory.createTitledBorder("Statistics")
        
        val statsText = """
            <html>
            <b>Total Inbound Calls:</b> ${callGraph.inboundCalls.size}<br>
            <b>Total Outbound Calls:</b> ${callGraph.outboundCalls.size}<br>
            <b>Element Type:</b> ${callGraph.rootNode.type}<br>
            <b>File:</b> ${callGraph.rootNode.filePath}
            </html>
        """.trimIndent()
        
        val statsLabel = JLabel(statsText)
        statsLabel.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        panel.add(statsLabel, BorderLayout.CENTER)
        
        return panel
    }

    /**
     * Custom tree cell renderer for call graph nodes
     */
    private class CallGraphTreeCellRenderer : DefaultTreeCellRenderer() {
        override fun getTreeCellRendererComponent(
            tree: JTree?,
            value: Any?,
            sel: Boolean,
            expanded: Boolean,
            leaf: Boolean,
            row: Int,
            hasFocus: Boolean
        ): Component {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
            
            when (val userObject = (value as? DefaultMutableTreeNode)?.userObject) {
                is CallGraphNode -> {
                    text = "${userObject.name} (${userObject.type.name.lowercase()})"
                    icon = getIconForNodeType(userObject.type)
                }
                is CallGraphEdge -> {
                    val relationText = when (userObject.type) {
                        CallGraphEdge.EdgeType.CALLS -> "calls"
                        CallGraphEdge.EdgeType.INHERITS -> "inherits from"
                        CallGraphEdge.EdgeType.USES -> "uses"
                    }
                    text = "${userObject.to.name} ($relationText)"
                    foreground = when (userObject.type) {
                        CallGraphEdge.EdgeType.CALLS -> JBColor.BLACK
                        CallGraphEdge.EdgeType.INHERITS -> JBColor.BLUE
                        CallGraphEdge.EdgeType.USES -> JBColor.GRAY
                    }
                }
                is String -> {
                    text = userObject
                    font = font.deriveFont(font.style or java.awt.Font.BOLD)
                }
            }
            
            return this
        }

        private fun getIconForNodeType(nodeType: CallGraphNode.NodeType): Icon? {
            // In a real implementation, you would return appropriate icons
            // For now, we'll use default icons
            return null
        }
    }
}