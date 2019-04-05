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

import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class EmbeddedHashLockTransaction extends EmbeddedTransaction {
    private HashLockTransactionBody hashLockTransactionBody;

    EmbeddedHashLockTransaction(NetworkType networkType, Mosaic mosaic, BlockDuration duration, String secretHash) {
        this(networkType, mosaic, duration, secretHash, new VerifiableEntity(Optional.empty()), Optional.empty());
    }

    EmbeddedHashLockTransaction(NetworkType networkType, Mosaic mosaic, BlockDuration duration, String secretHash, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(176), new EntityBody(networkType, (short)1, EntityType.LOCK), transactionInfo);
        this.hashLockTransactionBody = new HashLockTransactionBody(mosaic, duration, secretHash);
    }

    EmbeddedHashLockTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.hashLockTransactionBody = HashLockTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedHashLockTransaction create(Mosaic mosaic, BlockDuration duration, String secretHash, NetworkType networkType) {
        return new EmbeddedHashLockTransaction(networkType, mosaic, duration, secretHash);
    }

    public HashLockTransactionBody getHashLockTransactionBody()  { return this.hashLockTransactionBody; }

    public static EmbeddedHashLockTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedHashLockTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.hashLockTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
