package com.nitecon.callgraph.service

import com.intellij.psi.PsiElement
import com.nitecon.callgraph.model.CallGraph

/**
 * Service interface for call graph analysis
 */
interface CallGraphService {
    /**
     * Analyzes the given PSI element and returns a call graph
     */
    fun analyzeCallGraph(element: PsiElement): CallGraph?
    
    /**
     * Checks if the given element can be analyzed for call graph
     */
    fun canAnalyze(element: PsiElement): Boolean
}