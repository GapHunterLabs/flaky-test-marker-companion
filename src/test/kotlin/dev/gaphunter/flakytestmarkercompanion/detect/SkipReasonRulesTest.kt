package dev.gaphunter.flakytestmarkercompanion.detect

import dev.gaphunter.flakytestmarkercompanion.model.SkipProblem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class SkipReasonRulesTest {

    private val today = LocalDate.of(2026, 8, 24)

    @Test
    fun `null reason is NO_REASON`() {
        assertEquals(SkipProblem.NO_REASON, SkipReasonRules.firstProblem(null, today))
    }

    @Test
    fun `blank reason is NO_REASON`() {
        assertEquals(SkipProblem.NO_REASON, SkipReasonRules.firstProblem("   ", today))
    }

    @Test
    fun `reason with no date is not flagged`() {
        assertNull(SkipReasonRules.firstProblem("flaky in CI, see JIRA-123", today))
    }

    @Test
    fun `reason with a past date is STALE_REASON`() {
        assertEquals(SkipProblem.STALE_REASON, SkipReasonRules.firstProblem("disabled until 2026-01-01, see JIRA-123", today))
    }

    @Test
    fun `reason with a future date is not flagged`() {
        assertNull(SkipReasonRules.firstProblem("disabled until 2027-01-01, see JIRA-123", today))
    }

    @Test
    fun `reason with today's date is not flagged`() {
        assertNull(SkipReasonRules.firstProblem("disabled until 2026-08-24, see JIRA-123", today))
    }
}
