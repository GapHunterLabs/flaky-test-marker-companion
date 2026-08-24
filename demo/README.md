# Demo data for screenshots

`OrderServiceTest.java` — `testCreateOrder` has no reason (flagged),
`testCancelOrder` has a stale 2026-01-01 date (flagged),
`testRefundOrder` has a real, current reason (not flagged).

## How to get the screenshot

1. `./gradlew runIde` from `flaky-test-marker-companion`, open this
   `demo/` folder as the project.
2. Full Screen, open `OrderServiceTest.java` — warning icons should
   appear on the first two test methods but not the third.
3. Screenshot with all 3 test methods visible, save into
   `flaky-test-marker-companion/docs/screenshots/`. Close the sandbox.
