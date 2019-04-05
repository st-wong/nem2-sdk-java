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

import io.nem.sdk.model.account.AddressPropertyModification;
import io.nem.sdk.model.blockchain.NetworkType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Optional;

public class EmbeddedAddressPropertyTransaction extends EmbeddedTransaction {
    private AddressPropertyTransactionBody addressPropertyTransactionBody;

    EmbeddedAddressPropertyTransaction(NetworkType networkType, PropertyType propertyType, ArrayList<AddressPropertyModification> addressPropertyModifications) {
        this(networkType, propertyType, addressPropertyModifications, Optional.empty());
    }

    EmbeddedAddressPropertyTransaction(NetworkType networkType, PropertyType propertyType, ArrayList<AddressPropertyModification> addressPropertyModifications, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(122 + (26 * addressPropertyModifications.size())), new EntityBody(networkType, (short)1, EntityType.ACCOUNT_PROPERTIES_ADDRESS), transactionInfo);
        this.addressPropertyTransactionBody = new AddressPropertyTransactionBody(propertyType, addressPropertyModifications);
    }

    EmbeddedAddressPropertyTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.addressPropertyTransactionBody = AddressPropertyTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedAddressPropertyTransaction create(PropertyType propertyType, ArrayList<AddressPropertyModification> addressPropertyModifications, NetworkType networkType) {
        return new EmbeddedAddressPropertyTransaction(networkType, propertyType, addressPropertyModifications);
    }

    public AddressPropertyTransactionBody getAddressPropertyTransactionBody() { return this.addressPropertyTransactionBody; }

    public static EmbeddedAddressPropertyTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedAddressPropertyTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.addressPropertyTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
