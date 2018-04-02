package io.nem.sdk.model.lock;

import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;

public class LockStatusTest {

    @Test
    void UNUSEDIs0() {
        assertTrue(0 == LockStatus.UNUSED.getValue());
    }

    @Test
    void USEDIs0() {
        assertTrue(1 == LockStatus.USED.getValue());
    }

}
