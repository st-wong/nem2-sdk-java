package io.nem.sdk.model.lock;

import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.mosaic.Mosaic;

import java.math.BigInteger;

public class LockFundsInfo {
    private final PublicAccount account;
    private final Mosaic mosaic;
    private final BigInteger expirationHeight;
    private final LockStatus status;
    private final String hash;

    public LockFundsInfo(PublicAccount account, Mosaic mosaic, BigInteger expirationHeight, LockStatus status, String hash) {
        this.account = account;
        this.mosaic = mosaic;
        this.expirationHeight = expirationHeight;
        this.status = status;
        this.hash = hash;
    }

    public PublicAccount getAccount() { return account; }

    public Mosaic getMosaic() { return mosaic; }

    public BigInteger getExpirationHeight() { return expirationHeight; }

    public LockStatus getStatus() { return status; }

    public String getHash() { return hash; }
}

