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

import com.google.flatbuffers.FlatBufferBuilder;
import io.nem.core.crypto.Signer;
import io.nem.core.utils.ByteUtils;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The aggregate innerTransactions contain multiple innerTransactions that can be initiated by different accounts.
 *
 * @since 1.0
 */
public class AggregateTransaction extends Transaction {
    private final List<Transaction> innerTransactions;
    private final List<AggregateTransactionCosignature> cosignatures;

    AggregateTransaction(NetworkType networkType, Deadline deadline, long max_fee, List<Transaction> innerTransactions, List<AggregateTransactionCosignature> cosignatures) {
        this(networkType, deadline, max_fee, innerTransactions, cosignatures, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public AggregateTransaction(NetworkType networkType, Deadline deadline, long max_fee, List<Transaction> innerTransactions, List<AggregateTransactionCosignature> cosignatures, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, innerTransactions, cosignatures, new VerifiableEntity(Optional.of(signature)), Optional.of(signer), Optional.of(transactionInfo));
    }

    AggregateTransaction(NetworkType networkType, Deadline deadline, long max_fee, List<Transaction> innerTransactions, List<AggregateTransactionCosignature> cosignatures, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(120 + 4 + innerTransactions.size()), new EntityBody(networkType, (short)2, EntityType.AGGREGATE_BONDED, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        Validate.notNull(innerTransactions, "InnerTransactions must not be null");
        Validate.notNull(cosignatures, "Cosignatures must not be null");
        this.innerTransactions = innerTransactions;
        this.cosignatures = cosignatures;
    }

    AggregateTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        int size = ByteUtils.streamToInt(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.innerTransactions = new ArrayList<>(size);
        this.cosignatures = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Transaction transaction = Transaction.loadFromBinary(inputStream);
            innerTransactions.add(transaction);
        }
        for (int i = 0; i < size; i++) {
            AggregateTransactionCosignature cosignature = AggregateTransactionCosignature.loadFromBinary(inputStream, this.getEntityBody().getNetworkType());
            cosignatures.add(cosignature);
        }
    }

    /**
     * Create an aggregate complete transaction object
     *
     * @param deadline          The deadline to include the transaction.
     * @param innerTransactions The list of inner innerTransactions.
     * @param networkType       The network type.
     * @return {@link AggregateTransaction}
     */
    public static AggregateTransaction createComplete(Deadline deadline, List<Transaction> innerTransactions, NetworkType networkType) {
        return new AggregateTransaction(networkType, deadline, 0, innerTransactions, new ArrayList<>());
    }

    /**
     * Create an aggregate bonded transaction object
     *
     * @param deadline          The deadline to include the transaction.
     * @param innerTransactions The list of inner innerTransactions.
     * @param networkType       The network type.
     * @return {@link AggregateTransaction}
     */
    public static AggregateTransaction createBonded(Deadline deadline, List<Transaction> innerTransactions, NetworkType networkType) {
        return new AggregateTransaction(networkType, deadline, 0, innerTransactions, new ArrayList<>());
    }

    /**
     * Returns list of innerTransactions included in the aggregate transaction.
     *
     * @return List of innerTransactions included in the aggregate transaction.
     */
    public List<Transaction> getInnerTransactions() { return innerTransactions;}

    /**
     * Returns list of transaction cosigners signatures.
     *
     * @return List of transaction cosigners signatures.
     */
    public List<AggregateTransactionCosignature> getCosignatures() { return cosignatures; }

    public static AggregateTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new AggregateTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(ByteUtils.intToBytes(innerTransactions.size(), ByteOrder.LITTLE_ENDIAN));
        for (int i = 0; i < this.innerTransactions.size(); i++) {
            byte[] transaction = this.innerTransactions.get(i).serialize();
            stream.write(transaction, 0, transaction.length);
        }
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Sign transaction with cosignatories creating a new SignedTransaction.
     *
     * @param initiatorAccount Initiator account
     * @param cosignatories    The list of accounts that will cosign the transaction
     * @return {@link SignedTransaction}
     */
    public SignedTransaction signTransactionWithCosigners(Account initiatorAccount, List<Account> cosignatories) throws Exception {
        SignedTransaction signedTransaction = this.signWith(initiatorAccount);
        String payload = signedTransaction.getPayload();

        for (Account cosignatory : cosignatories) {
            Signer signer = new Signer(cosignatory.getKeyPair());
            byte[] bytes = Hex.decode(signedTransaction.getHash());
            byte[] signatureBytes = signer.sign(bytes).getBytes();
            payload += cosignatory.getPublicKey() + Hex.toHexString(signatureBytes);
        }

        byte[] payloadBytes = Hex.decode(payload);

        byte[] size = BigInteger.valueOf(payloadBytes.length).toByteArray();
        ArrayUtils.reverse(size);

        System.arraycopy(size, 0, payloadBytes, 0, size.length);

        return new SignedTransaction(Hex.toHexString(payloadBytes), signedTransaction.getHash(), getEntityBody().getEntityType());
    }

    /**
     * Check if account has signed transaction.
     *
     * @param publicAccount - Signer public account
     * @return boolean
     */
    public boolean signedByAccount(PublicAccount publicAccount) {
        return this.getEntityBody().getSigner().get().equals(publicAccount) || this.getCosignatures().stream().anyMatch(o -> o.getSigner().equals(publicAccount));
    }
}