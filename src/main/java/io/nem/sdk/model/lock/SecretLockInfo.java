/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private final String metaId;

    public SecretLockInfo(PublicAccount account, Mosaic mosaic, BigInteger expirationHeight, LockStatus status, HashType hashType, String secret, Address recipient, String metaId) {
        this.account = account;
        this.mosaic = mosaic;
        this.expirationHeight = expirationHeight;
        this.status = status;
        this.hashType = hashType;
        this.secret = secret;
        this.recipient = recipient;
        this.metaId = metaId;
    }

    public PublicAccount getAccount() { return account; }

    public Mosaic getMosaic() { return mosaic; }

    public BigInteger getExpirationHeight() { return expirationHeight; }

    public LockStatus getStatus() { return status; }

    public HashType getHashType() { return hashType; }

    public String getSecret() { return secret; }

    public Address getRecipient() { return recipient; }

    public String getMetaId() { return metaId; }
}

