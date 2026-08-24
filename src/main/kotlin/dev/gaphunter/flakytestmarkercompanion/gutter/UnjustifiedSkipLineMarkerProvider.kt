package dev.gaphunter.flakytestmarkercompanion.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import dev.gaphunter.flakytestmarkercompanion.detect.JavaSkipFinder
import dev.gaphunter.flakytestmarkercompanion.detect.KotlinSkipFinder
import dev.gaphunter.flakytestmarkercompanion.model.SkipHit
import dev.gaphunter.flakytestmarkercompanion.model.SkipProblem
import dev.gaphunter.flakytestmarkercompanion.review.ReviewPrompt

class UnjustifiedSkipLineMarkerProvider : LineMarkerProviderDescriptor(), DumbAware {

    override fun getName(): String = "Unjustified or stale skipped test"

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        val file = elements.firstOrNull()?.containingFile ?: return
        val hits = when (file.language.id) {
            "JAVA" -> JavaSkipFinder.findAll(file)
            "kotlin" -> KotlinSkipFinder.findAll(file)
            else -> emptyList()
        }
        if (hits.isEmpty()) return

        val hitsByElement = hits.associateBy { it.nameElement }
        for (element in elements) {
            val hit = hitsByElement[element] ?: continue
            result.add(buildMarker(hit))

            val path = file.virtualFile?.path ?: continue
            val lineNumber = file.viewProvider.document?.getLineNumber(element.textRange.startOffset) ?: -1
            ReviewPrompt.recordHit(file.project, "$path:$lineNumber")
        }
    }

    private fun buildMarker(hit: SkipHit): LineMarkerInfo<PsiElement> {
        val tooltip = when (hit.problem) {
            SkipProblem.NO_REASON -> "This test is skipped with no reason given -- future readers have no context for why, or when it's safe to re-enable"
            SkipProblem.STALE_REASON -> "This test's skip reason names a revisit date that has already passed -- it's due for a second look"
        }
        return LineMarkerInfo(
            hit.nameElement,
            hit.nameElement.textRange,
            SkipIcons.RISK,
            { _: PsiElement -> tooltip },
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { tooltip },
        )
    }
}
