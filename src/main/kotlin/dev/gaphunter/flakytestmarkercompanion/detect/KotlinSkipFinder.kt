package dev.gaphunter.flakytestmarkercompanion.detect

import com.intellij.psi.PsiFile
import dev.gaphunter.flakytestmarkercompanion.model.SkipHit
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

/** Kotlin counterpart of [JavaSkipFinder]. */
object KotlinSkipFinder {

    private val SKIP_ANNOTATIONS = setOf("Disabled", "Ignore")

    fun findAll(file: PsiFile): List<SkipHit> {
        if (file !is KtFile) return emptyList()
        val hits = mutableListOf<SkipHit>()
        file.accept(object : KtTreeVisitorVoid() {
            override fun visitNamedFunction(function: KtNamedFunction) {
                super.visitNamedFunction(function)
                val annotation = skipAnnotation(function) ?: return
                val reason = reasonOf(annotation)
                val problem = SkipReasonRules.firstProblem(reason) ?: return
                val nameIdentifier = function.nameIdentifier ?: return
                hits += SkipHit(nameIdentifier, problem)
            }
        })
        return hits
    }

    private fun skipAnnotation(function: KtNamedFunction): KtAnnotationEntry? =
        function.annotationEntries.firstOrNull { it.shortName?.asString() in SKIP_ANNOTATIONS }

    private fun reasonOf(annotation: KtAnnotationEntry): String? {
        val arg = annotation.valueArguments.firstOrNull() ?: return null
        val text = arg.getArgumentExpression()?.text ?: return null
        return text.trim('"')
    }
}
