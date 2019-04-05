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

import io.nem.core.utils.ByteUtils;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * The account properties transaction type modification body object containing necessary data for
 * account properties transaction type modification transactions
 * and embedded account properties transaction type modification transactions.
 *
 * @since 1.0
 */
public class TransactionTypePropertyTransactionBody {
    private final PropertyType propertyType;
    private final ArrayList<TransactionTypePropertyModification> transactionTypePropertyModifications;

    /**
     * Constructor
     *
     * @param propertyType of the transaction.
     * @param transactionTypePropertyModifications list of the modifications for the transaction.
     */
    TransactionTypePropertyTransactionBody(PropertyType propertyType, ArrayList<TransactionTypePropertyModification> transactionTypePropertyModifications) {
        Validate.notNull(propertyType, "Property Type must not be null");
        Validate.notNull(transactionTypePropertyModifications, "List of Transaction Type Property Modification must not be null");
        this.propertyType = propertyType;
        this.transactionTypePropertyModifications = transactionTypePropertyModifications;
    }

    /**
     * Constructor
     *
     * @param inputStream of the binary layout.
     * @throws Exception when there are issues in reading the data input.
     */
    TransactionTypePropertyTransactionBody(final DataInput inputStream) throws Exception {
        this.propertyType = PropertyType.loadFromBinary(inputStream);
        short transactionTypePropertyModificationsCount = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.transactionTypePropertyModifications = new ArrayList<>(transactionTypePropertyModificationsCount);
        for (int i = 0; i < transactionTypePropertyModificationsCount; i++) {
            transactionTypePropertyModifications.add(TransactionTypePropertyModification.loadFromBinary(inputStream));
        }
    }

    /**
     * Returns the property type.
     *
     * @return property type.
     */
    public PropertyType getPropertyType()  { return this.propertyType; }

    /**
     * Returns the list of account properties transaction type modifications.
     *
     * @return Link<{ @ link   TransactionTypePropertyModification }>
     */
    public ArrayList<TransactionTypePropertyModification> getTransactionTypePropertyModifications()  { return this.transactionTypePropertyModifications; }

    /**
     * Load an instance of Transaction Type Property Transaction Body object from a binary layout.
     *
     * @param inputStream of the binary layout.
     * @return an instance of Transaction Type Property Transaction Body object from the binary layout.
     * @throws Exception when there are issues in reading the data input.
     */
    public static TransactionTypePropertyTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new TransactionTypePropertyTransactionBody(inputStream); }

    /**
     * Serialize the Transaction Type Property Transaction Body object to a binary layout.
     *
     * @return an array of bytes representing the binary layout of the Transfer Transaction Body object.
     * @throws Exception when there are issues in writing the binary layout.
     */
    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.propertyType.serialize());
        stream.write(ByteUtils.byteToBytes((byte)this.transactionTypePropertyModifications.size(), ByteOrder.LITTLE_ENDIAN));
        for (int i = 0; i < this.transactionTypePropertyModifications.size(); i++) {
            byte[] transactionTypePropertyModification = this.transactionTypePropertyModifications.get(i).serialize();
            stream.write(transactionTypePropertyModification, 0, transactionTypePropertyModification.length);
        }
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}