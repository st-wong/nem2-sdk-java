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
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Optional;

public class TransferTransaction extends Transaction {
    private final TransferTransactionBody transferTransactionBody;

    TransferTransaction(NetworkType networkType, Deadline deadline, long max_fee, Address recipient, List<Mosaic> mosaics, Message message) {
        this(networkType, deadline, max_fee, recipient, mosaics, message, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public TransferTransaction(NetworkType networkType, Deadline deadline, long max_fee, Address recipient, List<Mosaic> mosaics, Message message, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, recipient, mosaics, message, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    TransferTransaction(NetworkType networkType, Deadline deadline, long max_fee, Address recipient, List<Mosaic> mosaics, Message message, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(149 + (16 * mosaics.size()) +  message.getPayload().getBytes().length), new EntityBody(networkType, (short)3, EntityType.TRANSFER, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.transferTransactionBody = new TransferTransactionBody(recipient, mosaics, message);
    }

    TransferTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.transferTransactionBody = TransferTransactionBody.loadFromBinary(inputStream);
    }

    /**
     * Create a transfer transaction object.
     *
     * @param deadline    - The deadline to include the transaction.
     * @param recipient   - The recipient of the transaction.
     * @param mosaics     - The array of mosaics.
     * @param message     - The transaction message.
     * @param networkType - The network type.
     * @return a TransferTransaction instance
     */
    public static TransferTransaction create(Deadline deadline, Address recipient, List<Mosaic> mosaics, Message message, NetworkType networkType) {
        return new TransferTransaction(networkType, deadline, 0, recipient, mosaics, message);
    }

    /**
     * Returns the body of the transfer transaction.
     *
     * @return body of transfer transaction.
     */
    public TransferTransactionBody getTransferTransactionBody()  {
        return this.transferTransactionBody;
    }

    /**
     * Load an instance of Transfer Transaction object from a binary layout.
     *
     * @param inputStream of the binary layout.
     * @return an instance of Transfer Transaction object from the binary layout.
     * @throws Exception when there are issues in reading the data input.
     */
    public static TransferTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new TransferTransaction(inputStream); }

    /**
     * Serialize the Transfer Transaction object to a binary layout.
     *
     * @return an array of bytes representing the binary layout of the Transfer Transaction object.
     * @throws Exception when there are issues in writing the binary layout.
     */
    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(transferTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
