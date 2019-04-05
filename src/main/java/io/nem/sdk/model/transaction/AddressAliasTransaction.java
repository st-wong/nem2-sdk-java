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
import io.nem.sdk.model.namespace.AliasAction;
import io.nem.sdk.model.namespace.NamespaceId;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class AddressAliasTransaction extends Transaction {
    private final AddressAliasTransactionBody addressAliasTransactionBody;

    AddressAliasTransaction(NetworkType networkType, Deadline deadline, long max_fee, AliasAction aliasAction, NamespaceId namespaceId, Address address) {
        this(networkType, deadline, max_fee, aliasAction, namespaceId, address, new VerifiableEntity(Optional.empty()), Optional.empty());
    }

    AddressAliasTransaction(NetworkType networkType, Deadline deadline, long max_fee, AliasAction aliasAction, NamespaceId namespaceId, Address address, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(122), new EntityBody(networkType, (short)1, EntityType.ADDRESS_ALIAS), deadline, max_fee, verifiableEntity, transactionInfo);
        this.addressAliasTransactionBody = new AddressAliasTransactionBody(address, namespaceId, aliasAction);
    }

    AddressAliasTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.addressAliasTransactionBody = AddressAliasTransactionBody.loadFromBinary(inputStream);
    }

    public static AddressAliasTransaction create(Deadline deadline, AliasAction aliasAction, NamespaceId namespaceId, Address address, NetworkType networkType) {
        return new AddressAliasTransaction(networkType, deadline, 0, aliasAction, namespaceId, address);
    }

    public AddressAliasTransactionBody getAddressAliasTransactionBody() { return this.addressAliasTransactionBody; }

    public static AddressAliasTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new AddressAliasTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.addressAliasTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
