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
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Optional;

public class EmbeddedTransferTransaction extends EmbeddedTransaction {
    private final TransferTransactionBody transferTransactionBody;

    EmbeddedTransferTransaction(NetworkType networkType, Address recipient, ArrayList<Mosaic> mosaics, Message message) {
        this(networkType, recipient, mosaics, message, Optional.empty());
    }

    EmbeddedTransferTransaction(NetworkType networkType, Address recipient, ArrayList<Mosaic> mosaics, Message message, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(149 + (16 * mosaics.size()) +  message.getPayload().getBytes().length), new EntityBody(networkType, (short)3, EntityType.TRANSFER), transactionInfo);
        this.transferTransactionBody = new TransferTransactionBody(recipient, mosaics, message);
    }

    EmbeddedTransferTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.transferTransactionBody = TransferTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedTransferTransaction create(Address recipient, ArrayList<Mosaic> mosaics, Message message, NetworkType networkType) {
        return new EmbeddedTransferTransaction(networkType, recipient, mosaics, message);
    }

    public TransferTransactionBody getTransferTransactionBody()  {
        return this.transferTransactionBody;
    }

    public static EmbeddedTransferTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedTransferTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.transferTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
