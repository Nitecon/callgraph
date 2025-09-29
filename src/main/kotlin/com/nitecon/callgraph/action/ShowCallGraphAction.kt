package com.nitecon.callgraph.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.cidr.lang.psi.*
import com.nitecon.callgraph.service.CallGraphService
import com.nitecon.callgraph.ui.CallGraphPanel

/**
 * Action to show call graph for the selected C++ element
 */
class ShowCallGraphAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        
        val caretOffset = editor.caretModel.offset
        val elementAtCaret = psiFile.findElementAt(caretOffset) ?: return
        
        // Find the nearest analyzable element (class, function, or method)
        val targetElement = findAnalyzableElement(elementAtCaret)
        if (targetElement == null) {
            return
        }

        val callGraphService = project.service<CallGraphService>()
        val callGraph = callGraphService.analyzeCallGraph(targetElement)
        
        if (callGraph != null) {
            // Show the call graph in the tool window
            val toolWindowManager = ToolWindowManager.getInstance(project)
            val toolWindow = toolWindowManager.getToolWindow("Call Graph")
            
            if (toolWindow != null) {
                val contentManager = toolWindow.contentManager
                val callGraphPanel = CallGraphPanel(callGraph, project)
                
                val content = contentManager.factory.createContent(
                    callGraphPanel,
                    "Call Graph: ${callGraph.rootNode.name}",
                    false
                )
                
                contentManager.addContent(content)
                contentManager.setSelectedContent(content)
                toolWindow.activate(null)
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        
        var isVisible = false
        
        if (project != null && editor != null && psiFile != null) {
            val caretOffset = editor.caretModel.offset
            val elementAtCaret = psiFile.findElementAt(caretOffset)
            
            if (elementAtCaret != null) {
                val targetElement = findAnalyzableElement(elementAtCaret)
                isVisible = targetElement != null
            }
        }
        
        e.presentation.isEnabledAndVisible = isVisible
    }

    private fun findAnalyzableElement(element: com.intellij.psi.PsiElement): com.intellij.psi.PsiElement? {
        // Look for class/struct
        val structLike = PsiTreeUtil.getParentOfType(element, OCStructLike::class.java)
        if (structLike != null) return structLike
        
        // Look for function definition
        val functionDef = PsiTreeUtil.getParentOfType(element, OCFunctionDefinition::class.java)
        if (functionDef != null) return functionDef
        
        // Look for function declaration
        val functionDecl = PsiTreeUtil.getParentOfType(element, OCFunctionDeclaration::class.java)
        if (functionDecl != null) return functionDecl
        
        return null
    }
}