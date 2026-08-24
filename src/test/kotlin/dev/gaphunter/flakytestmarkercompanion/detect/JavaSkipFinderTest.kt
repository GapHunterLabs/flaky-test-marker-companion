package dev.gaphunter.flakytestmarkercompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaSkipFinderTest : BasePlatformTestCase() {

    fun `test a Disabled test with no reason is flagged`() {
        val file = myFixture.configureByText(
            "OrderServiceTest.java",
            """
            class OrderServiceTest {
                @Disabled
                @Test
                void testSomething() {}
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaSkipFinder.findAll(file).size)
    }

    fun `test an Ignore test with a real reason is not flagged`() {
        val file = myFixture.configureByText(
            "OrderServiceTest.java",
            """
            class OrderServiceTest {
                @Ignore("flaky under load, see JIRA-456")
                @Test
                void testSomething() {}
            }
            """.trimIndent(),
        )
        assertTrue(JavaSkipFinder.findAll(file).isEmpty())
    }

    fun `test a Disabled test with a stale date reason is flagged`() {
        val file = myFixture.configureByText(
            "OrderServiceTest.java",
            """
            class OrderServiceTest {
                @Disabled("disabled until 2020-01-01, see JIRA-456")
                @Test
                void testSomething() {}
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaSkipFinder.findAll(file).size)
    }

    fun `test a non-skipped test method is never flagged`() {
        val file = myFixture.configureByText(
            "OrderServiceTest.java",
            """
            class OrderServiceTest {
                @Test
                void testSomething() {}
            }
            """.trimIndent(),
        )
        assertTrue(JavaSkipFinder.findAll(file).isEmpty())
    }
}
