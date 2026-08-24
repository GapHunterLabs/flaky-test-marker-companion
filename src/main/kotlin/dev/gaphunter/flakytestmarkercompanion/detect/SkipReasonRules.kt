package dev.gaphunter.flakytestmarkercompanion.detect

import dev.gaphunter.flakytestmarkercompanion.model.SkipProblem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Real-world rules for judging whether a `@Disabled`/`@Ignore` reason
 * string is a genuine, still-current justification -- not a linguistic
 * quality check, just two concrete, checkable signals:
 *
 * 1. No reason at all (blank/absent) -- the annotation gives future
 *    readers zero context for why the test is skipped or when it's safe
 *    to re-enable.
 * 2. A reason that names a `YYYY-MM-DD` date that has already passed --
 *    the common real pattern ("disabled until 2026-03-01, see JIRA-123")
 *    where the date was meant as a revisit deadline, not decoration.
 */
object SkipReasonRules {

    private val DATE_PATTERN = Regex("""\b(\d{4})-(\d{2})-(\d{2})\b""")
    private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE

    fun firstProblem(reason: String?, today: LocalDate = LocalDate.now()): SkipProblem? {
        val trimmed = reason?.trim().orEmpty()
        if (trimmed.isEmpty()) return SkipProblem.NO_REASON

        val match = DATE_PATTERN.find(trimmed) ?: return null
        val date = parseDate(match.value) ?: return null
        if (date.isBefore(today)) return SkipProblem.STALE_REASON
        return null
    }

    private fun parseDate(text: String): LocalDate? = try {
        LocalDate.parse(text, FORMATTER)
    } catch (e: DateTimeParseException) {
        null
    }
}
