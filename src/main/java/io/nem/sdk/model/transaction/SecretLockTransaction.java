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
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;


import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class SecretLockTransaction extends Transaction {
    private final SecretLockTransactionBody secretLockTransactionBody;

    SecretLockTransaction(NetworkType networkType, Deadline deadline, long max_fee, Mosaic mosaic, BlockDuration duration, LockHashAlgorithm lockHashAlgorithm, String secretHash, Address recipient) {
        this(networkType, deadline, max_fee, mosaic, duration, lockHashAlgorithm, secretHash, recipient, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public SecretLockTransaction(NetworkType networkType, Deadline deadline, long max_fee, Mosaic mosaic, BlockDuration duration, LockHashAlgorithm lockHashAlgorithm, String secretHash, Address recipient, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, mosaic, duration, lockHashAlgorithm, secretHash, recipient, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    SecretLockTransaction(NetworkType networkType, Deadline deadline, long max_fee, Mosaic mosaic, BlockDuration duration, LockHashAlgorithm lockHashAlgorithm, String secretHash, Address recipient, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(202), new EntityBody(networkType, (short)1, EntityType.SECRET_LOCK, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.secretLockTransactionBody = new SecretLockTransactionBody(mosaic, duration, lockHashAlgorithm, secretHash, recipient);
    }

    SecretLockTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.secretLockTransactionBody = SecretLockTransactionBody.loadFromBinary(inputStream);
    }

    public static SecretLockTransaction create(Deadline deadline, Mosaic mosaic, BlockDuration duration, LockHashAlgorithm lockHashAlgorithm, String secretHash, Address recipient, NetworkType networkType) {
        return new SecretLockTransaction(networkType, deadline, 0, mosaic, duration, lockHashAlgorithm, secretHash, recipient);
    }

    public SecretLockTransactionBody getSecretLockTransactionBody()  { return this.secretLockTransactionBody; }

    public static SecretLockTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new SecretLockTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.secretLockTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
