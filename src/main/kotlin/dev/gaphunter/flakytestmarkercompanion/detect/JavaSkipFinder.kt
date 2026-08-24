package dev.gaphunter.flakytestmarkercompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import dev.gaphunter.flakytestmarkercompanion.model.SkipHit

/**
 * Finds Java test methods annotated `@Disabled` (JUnit 5) or `@Ignore`
 * (JUnit 4) whose reason argument is missing/blank, or names a
 * `YYYY-MM-DD` date that has already passed ([SkipReasonRules]).
 * Matches by simple annotation name only, so it works whether the real
 * JUnit jar is on the classpath or not.
 */
object JavaSkipFinder {

    private val SKIP_ANNOTATIONS = setOf("Disabled", "Ignore")

    fun findAll(file: PsiFile): List<SkipHit> {
        val hits = mutableListOf<SkipHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitMethod(method: PsiMethod) {
                super.visitMethod(method)
                val annotation = skipAnnotation(method) ?: return
                val reason = reasonOf(annotation)
                val problem = SkipReasonRules.firstProblem(reason) ?: return
                val nameIdentifier = method.nameIdentifier ?: return
                hits += SkipHit(nameIdentifier, problem)
            }
        })
        return hits
    }

    private fun skipAnnotation(method: PsiMethod): PsiAnnotation? =
        method.modifierList?.annotations.orEmpty().firstOrNull {
            it.nameReferenceElement?.referenceName in SKIP_ANNOTATIONS
        }

    private fun reasonOf(annotation: PsiAnnotation): String? {
        val value = annotation.findAttributeValue("value") as? PsiLiteralExpression
        return value?.value as? String
    }
}
