package com.nitecon.callgraph.model

import com.intellij.psi.PsiElement

/**
 * Represents a node in the call graph
 */
data class CallGraphNode(
    val name: String,
    val qualifiedName: String,
    val type: NodeType,
    val psiElement: PsiElement?,
    val filePath: String
) {
    enum class NodeType {
        CLASS,
        FUNCTION,
        METHOD
    }
}

/**
 * Represents a relationship between two nodes in the call graph
 */
data class CallGraphEdge(
    val from: CallGraphNode,
    val to: CallGraphNode,
    val type: EdgeType
) {
    enum class EdgeType {
        CALLS,
        INHERITS,
        USES
    }
}

/**
 * Complete call graph data structure
 */
data class CallGraph(
    val rootNode: CallGraphNode,
    val inboundCalls: List<CallGraphEdge>,
    val outboundCalls: List<CallGraphEdge>
)