package com.nitecon.callgraph.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.cidr.lang.psi.*
import com.jetbrains.cidr.lang.psi.visitors.OCRecursiveVisitor
import com.nitecon.callgraph.model.*

/**
 * Implementation of CallGraphService for C++ code analysis
 */
@Service
class CallGraphServiceImpl : CallGraphService {

    override fun canAnalyze(element: PsiElement): Boolean {
        return element is OCStructLike || 
               element is OCFunctionDeclaration ||
               element is OCFunctionDefinition
    }

    override fun analyzeCallGraph(element: PsiElement): CallGraph? {
        if (!canAnalyze(element)) return null

        val rootNode = createNodeFromElement(element) ?: return null
        val project = element.project
        
        val inboundCalls = findInboundCalls(element, project)
        val outboundCalls = findOutboundCalls(element)

        return CallGraph(rootNode, inboundCalls, outboundCalls)
    }

    private fun createNodeFromElement(element: PsiElement): CallGraphNode? {
        return when (element) {
            is OCStructLike -> {
                val name = element.name ?: "Unknown"
                val qualifiedName = element.qualifiedName?.name ?: name
                CallGraphNode(
                    name = name,
                    qualifiedName = qualifiedName,
                    type = CallGraphNode.NodeType.CLASS,
                    psiElement = element,
                    filePath = element.containingFile?.virtualFile?.path ?: ""
                )
            }
            is OCFunctionDeclaration -> {
                val name = element.name ?: "Unknown"
                CallGraphNode(
                    name = name,
                    qualifiedName = name,
                    type = CallGraphNode.NodeType.FUNCTION,
                    psiElement = element,
                    filePath = element.containingFile?.virtualFile?.path ?: ""
                )
            }
            is OCFunctionDefinition -> {
                val name = element.name ?: "Unknown"
                CallGraphNode(
                    name = name,
                    qualifiedName = name,
                    type = if (element.declarator?.parent is OCStructLike) 
                        CallGraphNode.NodeType.METHOD else CallGraphNode.NodeType.FUNCTION,
                    psiElement = element,
                    filePath = element.containingFile?.virtualFile?.path ?: ""
                )
            }
            else -> null
        }
    }

    private fun findInboundCalls(element: PsiElement, project: Project): List<CallGraphEdge> {
        val inboundCalls = mutableListOf<CallGraphEdge>()
        val rootNode = createNodeFromElement(element) ?: return emptyList()

        // Search for references to this element
        val searchScope = GlobalSearchScope.projectScope(project)
        val references = ReferencesSearch.search(element, searchScope).findAll()

        for (reference in references) {
            val referenceElement = reference.element
            val containingFunction = PsiTreeUtil.getParentOfType(referenceElement, 
                OCFunctionDeclaration::class.java, OCFunctionDefinition::class.java)
            val containingClass = PsiTreeUtil.getParentOfType(referenceElement, OCStructLike::class.java)

            val fromNode = when {
                containingFunction != null -> createNodeFromElement(containingFunction)
                containingClass != null -> createNodeFromElement(containingClass)
                else -> null
            }

            if (fromNode != null && fromNode != rootNode) {
                inboundCalls.add(CallGraphEdge(fromNode, rootNode, CallGraphEdge.EdgeType.CALLS))
            }
        }

        return inboundCalls
    }

    private fun findOutboundCalls(element: PsiElement): List<CallGraphEdge> {
        val outboundCalls = mutableListOf<CallGraphEdge>()
        val rootNode = createNodeFromElement(element) ?: return emptyList()

        // Find all calls made from this element
        element.accept(object : OCRecursiveVisitor() {
            override fun visitCallExpression(callExpression: OCCallExpression) {
                super.visitCallExpression(callExpression)
                
                val calledElement = callExpression.reference?.resolve()
                if (calledElement != null) {
                    val toNode = createNodeFromElement(calledElement)
                    if (toNode != null && toNode != rootNode) {
                        outboundCalls.add(CallGraphEdge(rootNode, toNode, CallGraphEdge.EdgeType.CALLS))
                    }
                }
            }

            override fun visitReferenceExpression(referenceExpression: OCReferenceExpression) {
                super.visitReferenceExpression(referenceExpression)
                
                // Handle other types of references like class usage
                val referencedElement = referenceExpression.reference?.resolve()
                if (referencedElement is OCStructLike) {
                    val toNode = createNodeFromElement(referencedElement)
                    if (toNode != null && toNode != rootNode) {
                        outboundCalls.add(CallGraphEdge(rootNode, toNode, CallGraphEdge.EdgeType.USES))
                    }
                }
            }
        })

        return outboundCalls
    }
}