package dev.gaphunter.flakytestmarkercompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinSkipFinderTest : BasePlatformTestCase() {

    fun `test a Disabled test with no reason is flagged`() {
        val file = myFixture.configureByText(
            "OrderServiceTest.kt",
            """
            class OrderServiceTest {
                @Disabled
                @Test
                fun testSomething() {}
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinSkipFinder.findAll(file).size)
    }

    fun `test a Disabled test with a real reason is not flagged`() {
        val file = myFixture.configureByText(
            "OrderServiceTest.kt",
            """
            class OrderServiceTest {
                @Disabled("flaky under load, see JIRA-456")
                @Test
                fun testSomething() {}
            }
            """.trimIndent(),
        )
        assertTrue(KotlinSkipFinder.findAll(file).isEmpty())
    }

    fun `test a non-skipped test method is never flagged`() {
        val file = myFixture.configureByText(
            "OrderServiceTest.kt",
            """
            class OrderServiceTest {
                @Test
                fun testSomething() {}
            }
            """.trimIndent(),
        )
        assertTrue(KotlinSkipFinder.findAll(file).isEmpty())
    }
}
