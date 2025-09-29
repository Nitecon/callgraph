package com.nitecon.callgraph

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

/**
 * Main plugin service for CallGraph functionality
 */
@Service(Service.Level.PROJECT)
class CallGraphPlugin(private val project: Project) {
    
    fun generateCallGraph() {
        // TODO: Implement call graph generation logic
    }
}