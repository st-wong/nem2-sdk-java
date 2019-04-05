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

package io.nem.sdk.model.transaction;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;

import java.lang.*;
import java.io.*;
import java.util.Optional;

public class EmbeddedSecretLockTransaction extends EmbeddedTransaction {
    private SecretLockTransactionBody secretLockTransactionBody;

    EmbeddedSecretLockTransaction(NetworkType networkType, Mosaic mosaic, BlockDuration duration, LockHashAlgorithm lockHashAlgorithm, String secretHash, Address recipient) {
        this(networkType, mosaic, duration, lockHashAlgorithm, secretHash, recipient, Optional.empty());
    }

    EmbeddedSecretLockTransaction(NetworkType networkType, Mosaic mosaic, BlockDuration duration, LockHashAlgorithm lockHashAlgorithm, String secretHash, Address recipient, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(202), new EntityBody(networkType, (short)1, EntityType.SECRET_LOCK), transactionInfo);
        this.secretLockTransactionBody = new SecretLockTransactionBody(mosaic, duration, lockHashAlgorithm, secretHash, recipient);
    }

    EmbeddedSecretLockTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.secretLockTransactionBody = SecretLockTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedSecretLockTransaction create(Mosaic mosaic, BlockDuration duration, LockHashAlgorithm lockHashAlgorithm, String secretHash, Address recipient, NetworkType networkType) {
        return new EmbeddedSecretLockTransaction(networkType, mosaic, duration, lockHashAlgorithm, secretHash, recipient, Optional.empty());
    }

    public SecretLockTransactionBody getSecretLockTransactionBody() { return this.secretLockTransactionBody; }

    public static EmbeddedSecretLockTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedSecretLockTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.secretLockTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
