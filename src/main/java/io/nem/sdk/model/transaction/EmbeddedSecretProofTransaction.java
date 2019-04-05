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

import io.nem.sdk.model.blockchain.NetworkType;

import java.lang.*;
import java.io.*;
import java.nio.*;
import java.util.Optional;

public class EmbeddedSecretProofTransaction extends EmbeddedTransaction {
    private SecretProofTransactionBody secretProofTransactionBody;

    EmbeddedSecretProofTransaction(NetworkType networkType, LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof) {
        this(networkType, lockHashAlgorithm, secretHash, proof, Optional.empty());
    }

    EmbeddedSecretProofTransaction(NetworkType networkType, LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(155 + proof.length()), new EntityBody(networkType, (short)1, EntityType.SECRET_PROOF), transactionInfo);
        this.secretProofTransactionBody = new SecretProofTransactionBody(lockHashAlgorithm, secretHash, proof);
    }

    EmbeddedSecretProofTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.secretProofTransactionBody = SecretProofTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedSecretProofTransaction create(LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof, NetworkType networkType) {
        return new EmbeddedSecretProofTransaction(networkType, lockHashAlgorithm, secretHash, proof);
    }

    public SecretProofTransactionBody getSecretProofTransactionBody()  { return this.secretProofTransactionBody; }

    public static EmbeddedSecretProofTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedSecretProofTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.secretProofTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
