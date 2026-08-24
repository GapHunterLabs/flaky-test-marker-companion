package dev.gaphunter.flakytestmarkercompanion.model

enum class SkipProblem {
    /** `@Disabled`/`@Ignore` with no reason argument (or a blank one). */
    NO_REASON,

    /** The reason contains a `YYYY-MM-DD`-shaped date that has already passed. */
    STALE_REASON,
}
