package dev.gaphunter.flakytestmarkercompanion.model

import com.intellij.psi.PsiElement

/** One `@Disabled`/`@Ignore` test method with a real problem in its justification. */
data class SkipHit(val nameElement: PsiElement, val problem: SkipProblem)
