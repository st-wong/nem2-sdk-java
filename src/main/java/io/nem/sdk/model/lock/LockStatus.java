package io.nem.sdk.model.lock;

public enum LockStatus {
    /**
     * Main net network
     */
    UNUSED(0),
    /**
     * Test net network
     */
    USED(1);

    private final int value;

    LockStatus(int value) {
        this.value = value;
    }

    /**
     * Returns enum value.
     *
     * @return int
     */
    public int getValue() {
        return this.value;
    }
}
