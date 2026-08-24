package com.acmecorp.orders;

public class OrderServiceTest {

    // No reason given -- flagged.
    @Disabled
    @Test
    void testCreateOrder() {}

    // Reason names a revisit date that already passed -- flagged.
    @Disabled("disabled until 2026-01-01, see JIRA-456")
    @Test
    void testCancelOrder() {}

    // Real, current reason -- not flagged.
    @Disabled("flaky under load, see JIRA-789")
    @Test
    void testRefundOrder() {}
}
