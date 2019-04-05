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

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Optional;

public class TransactionTypePropertyTransaction extends Transaction {
    private final TransactionTypePropertyTransactionBody transactionTypePropertyTransactionBody;

    TransactionTypePropertyTransaction(NetworkType networkType, Deadline deadline, long max_fee, PropertyType propertyType, ArrayList<TransactionTypePropertyModification> transactionTypePropertyModifications) {
        this(networkType, deadline, max_fee, propertyType, transactionTypePropertyModifications, new VerifiableEntity(Optional.empty()), Optional.empty());
    }

    TransactionTypePropertyTransaction(NetworkType networkType, Deadline deadline, long max_fee, PropertyType propertyType, ArrayList<TransactionTypePropertyModification> transactionTypePropertyModifications, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(125 + (3 * transactionTypePropertyModifications.size())), new EntityBody(networkType, (short)1, EntityType.ACCOUNT_PROPERTIES_TRANSACTION_ENTITY_TYPE), deadline, max_fee, verifiableEntity, transactionInfo);
        this.transactionTypePropertyTransactionBody = new TransactionTypePropertyTransactionBody(propertyType, transactionTypePropertyModifications);
    }

    TransactionTypePropertyTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.transactionTypePropertyTransactionBody = TransactionTypePropertyTransactionBody.loadFromBinary(inputStream);
    }

    public static TransactionTypePropertyTransaction create(Deadline deadline, PropertyType propertyType, ArrayList<TransactionTypePropertyModification> transactionTypePropertyModifications, NetworkType networkType) {
        return new TransactionTypePropertyTransaction(networkType, deadline, 0, propertyType, transactionTypePropertyModifications);
    }

    public TransactionTypePropertyTransactionBody getTransactionTypePropertyTransactionBody()  { return this.transactionTypePropertyTransactionBody; }

    public static TransactionTypePropertyTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new TransactionTypePropertyTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.transactionTypePropertyTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
