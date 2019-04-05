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

import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class HashLockTransaction extends Transaction {
    private HashLockTransactionBody hashLockTransactionBody;

    HashLockTransaction(NetworkType networkType, Deadline deadline, long max_fee, Mosaic mosaic, BlockDuration duration, String secretHash) {
        this(networkType, deadline, max_fee, mosaic, duration, secretHash, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public HashLockTransaction(NetworkType networkType, Deadline deadline, long max_fee, Mosaic mosaic, BlockDuration duration, String secretHash, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, mosaic, duration, secretHash, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    HashLockTransaction(NetworkType networkType, Deadline deadline, long max_fee, Mosaic mosaic, BlockDuration duration, String secretHash, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(176), new EntityBody(networkType, (short)1, EntityType.LOCK, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.hashLockTransactionBody = new HashLockTransactionBody(mosaic, duration, secretHash);
    }

    HashLockTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.hashLockTransactionBody = HashLockTransactionBody.loadFromBinary(inputStream);
    }

    public static HashLockTransaction create(Deadline deadline, Mosaic mosaic, BlockDuration duration, String secretHash, NetworkType networkType) {
        return new HashLockTransaction(networkType, deadline, 0, mosaic, duration, secretHash);
    }

    public HashLockTransactionBody getHashLockTransactionBody()  { return this.hashLockTransactionBody; }

    public static HashLockTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new HashLockTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.hashLockTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
