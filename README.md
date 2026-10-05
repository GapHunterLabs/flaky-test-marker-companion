# Flaky Test Marker Companion

Gutter warning icon on any Java/Kotlin test method annotated
`@Disabled` (JUnit 5) or `@Ignore` (JUnit 4) with no reason given, or
whose reason names a `YYYY-MM-DD` revisit date that has already
passed. Skipped tests are easy to forget about entirely — without a
reason, future readers have no context for why it's disabled; with a
stale date, it's a real signal the test is overdue for a second look.

## Why it exists

A skipped test with no reason is a silent, permanent hole in coverage
— nobody remembers why it was disabled, and nobody feels safe
re-enabling it. A reason with a "revisit by" date is better discipline,
but nothing enforces that anyone actually revisits it once that date
passes. Nothing in the IDE flags either case today.

## Why built this way

- **100% static text/PSI analysis** — matches the annotation by simple
  name, so it works whether the real JUnit jar is on the classpath or
  not.
- **Two concrete, checkable signals, not a linguistic quality check** —
  this plugin doesn't judge whether a *given* reason is actually a good
  one, only whether one exists and (if dated) is still current.

## v0.1 scope — stated honestly, not exhaustively

Only recognizes a `YYYY-MM-DD` date embedded directly in the reason
string — other date formats, or a date referenced only in a linked
ticket, aren't covered.

## Usage

Open any Java/Kotlin test file. A `@Disabled`/`@Ignore` test with no
reason, or a stale dated reason, shows a warning icon on the method
name.

## Support

- **Bugs and feature requests:** [GitHub Issues](https://github.com/GapHunterLabs/flaky-test-marker-companion/issues)
- **Questions, or custom rules for a team's codebase:** **gaphunterlabs@gmail.com**
- **Security vulnerabilities:** report privately as described in [SECURITY.md](SECURITY.md), not in a public issue.
- **Privacy and network behavior:** [PRIVACY.md](PRIVACY.md)

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
