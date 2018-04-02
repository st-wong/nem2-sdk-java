package io.nem.sdk.model.lock;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.transaction.HashType;

import java.math.BigInteger;

public class SecretLockInfo {
    private final PublicAccount account;
    private final Mosaic mosaic;
    private final BigInteger expirationHeight;
    private final LockStatus status;
    private final HashType hashType;
    private final String secret;
    private final Address recipient;

    public SecretLockInfo(PublicAccount account, Mosaic mosaic, BigInteger expirationHeight, LockStatus status, HashType hashType, String secret, Address recipient) {
        this.account = account;
        this.mosaic = mosaic;
        this.expirationHeight = expirationHeight;
        this.status = status;
        this.hashType = hashType;
        this.secret = secret;
        this.recipient = recipient;
    }

    public PublicAccount getAccount() { return account; }

    public Mosaic getMosaic() { return mosaic; }

    public BigInteger getExpirationHeight() { return expirationHeight; }

    public LockStatus getStatus() { return status; }

    public HashType getHashType() { return hashType; }

    public String getSecret() { return secret; }

    public Address getRecipient() { return recipient; }
}

