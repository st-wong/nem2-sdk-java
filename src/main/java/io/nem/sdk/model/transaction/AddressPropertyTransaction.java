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

import io.nem.core.crypto.PublicKey;
import io.nem.sdk.model.account.AccountLinkAction;
import io.nem.sdk.model.account.AddressPropertyModification;
import io.nem.sdk.model.blockchain.NetworkType;

import java.lang.*;
import java.io.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Optional;

public class AddressPropertyTransaction extends Transaction {
    private final AddressPropertyTransactionBody addressPropertyTransactionBody;

    AddressPropertyTransaction(NetworkType networkType, Deadline deadline, long max_fee, PropertyType propertyType, ArrayList<AddressPropertyModification> addressPropertyModifications) {
        this(networkType, deadline, max_fee, propertyType, addressPropertyModifications, new VerifiableEntity(Optional.empty()), Optional.empty());
    }

    AddressPropertyTransaction(NetworkType networkType, Deadline deadline, long max_fee, PropertyType propertyType, ArrayList<AddressPropertyModification> addressPropertyModifications, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(122 + (26 * addressPropertyModifications.size())), new EntityBody(networkType, (short)1, EntityType.ACCOUNT_PROPERTIES_ADDRESS), deadline, max_fee, verifiableEntity, transactionInfo);
        this.addressPropertyTransactionBody = new AddressPropertyTransactionBody(propertyType, addressPropertyModifications);
    }
    AddressPropertyTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.addressPropertyTransactionBody = AddressPropertyTransactionBody.loadFromBinary(inputStream);
    }

    public static AddressPropertyTransaction create(Deadline deadline, PropertyType propertyType, ArrayList<AddressPropertyModification> addressPropertyModifications, NetworkType networkType) {
        return new AddressPropertyTransaction(networkType, deadline, 0, propertyType, addressPropertyModifications);
    }

    public AddressPropertyTransactionBody getAddressPropertyTransactionBody() { return this.addressPropertyTransactionBody; }

    public static AddressPropertyTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new AddressPropertyTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.addressPropertyTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
