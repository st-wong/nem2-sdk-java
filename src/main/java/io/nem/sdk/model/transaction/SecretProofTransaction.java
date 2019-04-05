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
import io.nem.sdk.model.blockchain.NetworkType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class SecretProofTransaction extends Transaction {
    private final SecretProofTransactionBody secretProofTransactionBody;

    SecretProofTransaction(NetworkType networkType, Deadline deadline, long max_fee, LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof) {
        this(networkType, deadline, max_fee, lockHashAlgorithm, secretHash, proof, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public SecretProofTransaction(NetworkType networkType, Deadline deadline, long max_fee, LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, lockHashAlgorithm, secretHash, proof, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    SecretProofTransaction(NetworkType networkType, Deadline deadline, long max_fee, LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(155 + proof.length()), new EntityBody(networkType, (short)1, EntityType.SECRET_PROOF, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.secretProofTransactionBody = new SecretProofTransactionBody(lockHashAlgorithm, secretHash, proof);
    }

    SecretProofTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.secretProofTransactionBody = SecretProofTransactionBody.loadFromBinary(inputStream);
    }

    public static SecretProofTransaction create(Deadline deadline, LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof, NetworkType networkType) {
        return new SecretProofTransaction(networkType, deadline, 0, lockHashAlgorithm, secretHash, proof);
    }

    public SecretProofTransactionBody getSecretProofTransactionBody()  { return this.secretProofTransactionBody; }

    public static SecretProofTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new SecretProofTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.secretProofTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
