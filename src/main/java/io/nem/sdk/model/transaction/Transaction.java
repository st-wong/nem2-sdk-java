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

import io.nem.core.crypto.Hashes;
import io.nem.core.crypto.Signature;
import io.nem.core.crypto.Signer;
import io.nem.core.utils.ByteUtils;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.PublicAccount;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.Optional;

public class Transaction {
    private final SizePrefixedEntity sizePrefixedEntity;
    private final EntityBody entityBody;
    private final long max_fee;
    private final Deadline deadline;
    private final VerifiableEntity verifiableEntity;
    private final Optional<TransactionInfo> transactionInfo;

    Transaction(SizePrefixedEntity sizePrefixedEntity, EntityBody entityBody, Deadline deadline, long max_fee, VerifiableEntity verifiableEntity) {
        this(sizePrefixedEntity, entityBody, deadline, max_fee, verifiableEntity, Optional.empty());
    }

    Transaction(SizePrefixedEntity sizePrefixedEntity, EntityBody entityBody, Deadline deadline, long max_fee, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        Validate.notNull(sizePrefixedEntity, "Size Prefixed Entity must not be null");
        Validate.notNull(entityBody, "Entity Body must not be null");
        Validate.notNull(deadline, "Deadline must not be null");
        Validate.notNull(verifiableEntity, "Verifiable Entity must not be null");
        this.sizePrefixedEntity = sizePrefixedEntity;
        this.entityBody = entityBody;
        this.deadline = deadline;
        this.max_fee = max_fee;
        this.verifiableEntity = verifiableEntity;
        this.transactionInfo = transactionInfo;
    }

    Transaction(final DataInput inputStream) throws Exception {
        this.sizePrefixedEntity = SizePrefixedEntity.loadFromBinary(inputStream);
        this.verifiableEntity = VerifiableEntity.loadFromBinary(inputStream);
        this.entityBody = EntityBody.loadFromBinary(inputStream);
        this.max_fee = ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.deadline = new Deadline(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
        this.transactionInfo = Optional.empty();
    }

    public SizePrefixedEntity getSizePrefixedEntity()  {
        return this.sizePrefixedEntity;
    }

    public VerifiableEntity getVerifiableEntity()  {
        return this.verifiableEntity;
    }

    public EntityBody getEntityBody()  {
        return this.entityBody;
    }

    public long getFee()  {
        return this.max_fee;
    }

    public Deadline getDeadline()  {
        return this.deadline;
    }

    public Optional<TransactionInfo> getTransactionInfo() { return this.transactionInfo; }

    /**
     * Generates hash for a serialized transaction payload.
     *
     * @param transactionPayload Transaction payload
     * @return generated transaction hash.
     */
    public static String createTransactionHash(String transactionPayload) {
        byte[] bytes = Hex.decode(transactionPayload);
        byte[] signingBytes = new byte[bytes.length - 36];
        System.arraycopy(bytes, 4, signingBytes, 0, 32);
        System.arraycopy(bytes, 68, signingBytes, 32, bytes.length - 68);

        byte[] result = Hashes.sha3_256(signingBytes);
        return Hex.toHexString(result).toUpperCase();
    }

    /**
     * Serialize and sign transaction creating a new SignedTransaction.
     *
     * @param account The account to sign the transaction.
     * @return {@link SignedTransaction}
     */
    public SignedTransaction signWith(Account account) throws Exception {
        Signer signer = new Signer(account.getKeyPair());
        byte[] bytes = this.serialize();
        byte[] signingBytes = new byte[bytes.length - 100];
        System.arraycopy(bytes, 100, signingBytes, 0, bytes.length - 100);
        Signature signature = signer.sign(signingBytes);

        byte[] payload = new byte[bytes.length];
        System.arraycopy(bytes, 0, payload, 0, 4); // Size
        System.arraycopy(signature.getBytes(), 0, payload, 4, signature.getBytes().length); // Signature
        System.arraycopy(account.getKeyPair().getPublicKey().getRaw(), 0, payload, 64 + 4, account.getKeyPair().getPublicKey().getRaw().length); // Signer
        System.arraycopy(bytes, 100, payload, 100, bytes.length - 100);

        String hash = Transaction.createTransactionHash(Hex.toHexString(payload));
        return new SignedTransaction(Hex.toHexString(payload).toUpperCase(), hash, entityBody.getEntityType());
    }

    /**
     * Takes a transaction and formats bytes to be included in an aggregate transaction.
     *
     * @return transaction with signer serialized to be part of an aggregate transaction
     */
    byte[] toAggregateTransactionBytes() throws Exception {
        byte[] signerBytes = Hex.decode(this.entityBody.getSigner().get().getPublicKey());
        byte[] bytes = this.serialize();
        byte[] resultBytes = new byte[bytes.length - 64 - 16];

        System.arraycopy(signerBytes, 0, resultBytes, 4, 32); // Copy signer
        System.arraycopy(bytes, 100, resultBytes, 32 + 4, 4); // Copy type and version
        System.arraycopy(bytes, 100 + 2 + 2 + 16, resultBytes, 32 + 4 + 4, bytes.length - 120); // Copy following data

        byte[] size = BigInteger.valueOf(bytes.length - 64 - 16).toByteArray();
        ArrayUtils.reverse(size);

        System.arraycopy(size, 0, resultBytes, 0, size.length);

        return resultBytes;
    }

    /**
     * Convert an aggregate transaction to an inner transaction including transaction signer.
     *
     * @param signer Transaction signer.
     * @return instance of Transaction with signer
     */
    public Transaction toAggregate(PublicAccount signer) {
        this.entityBody.setSigner(signer);
        return this;
    }

    /**
     * Returns if a transaction is pending to be included in a block.
     *
     * @return if a transaction is pending to be included in a block
     */
    public boolean isUnconfirmed() {
        return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().equals(BigInteger.valueOf(0)) && this.transactionInfo.get().getHash().equals(this.transactionInfo.get().getMerkleComponentHash());
    }

    /**
     * Return if a transaction is included in a block.
     *
     * @return if a transaction is included in a block
     */
    public boolean isConfirmed() {
        return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().intValue() > 0;
    }

    /**
     * Returns if a transaction has missing signatures.
     *
     * @return if a transaction has missing signatures
     */
    public boolean hasMissingSignatures() {
        return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().equals(BigInteger.valueOf(0)) && !this.transactionInfo.get().getHash().equals(this.transactionInfo.get().getMerkleComponentHash());
    }

    /**
     * Returns if a transaction is not known by the network.
     *
     * @return if a transaction is not known by the network
     */
    public boolean isUnannounced() {
        return !this.transactionInfo.isPresent();
    }

    public static Transaction loadFromBinary(final DataInput inputStream) throws Exception { return new Transaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.sizePrefixedEntity.serialize());
        stream.write(this.verifiableEntity.serialize());
        stream.write(this.entityBody.serialize());
        stream.write(ByteUtils.longToBytes(this.max_fee, ByteOrder.LITTLE_ENDIAN));
        stream.write(ByteUtils.longToBytes(this.deadline.getInstant(), ByteOrder.LITTLE_ENDIAN));
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}