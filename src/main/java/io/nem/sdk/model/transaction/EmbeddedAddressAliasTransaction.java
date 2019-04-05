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

public class EmbeddedAddressAliasTransaction extends EmbeddedTransaction {
    private final AddressAliasTransactionBody addressAliasTransactionBody;

    EmbeddedAddressAliasTransaction(NetworkType networkType, AliasAction aliasAction, NamespaceId namespaceId, Address address) {
        this(networkType, aliasAction, namespaceId, address, Optional.empty());
    }

    EmbeddedAddressAliasTransaction(NetworkType networkType, AliasAction aliasAction, NamespaceId namespaceId, Address address, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(122), new EntityBody(networkType, (short)1, EntityType.ADDRESS_ALIAS), transactionInfo);
        this.addressAliasTransactionBody = new AddressAliasTransactionBody(address, namespaceId, aliasAction);
    }
    EmbeddedAddressAliasTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.addressAliasTransactionBody = AddressAliasTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedAddressAliasTransaction create(AliasAction aliasAction, NamespaceId namespaceId, Address address, NetworkType networkType) {
        return new EmbeddedAddressAliasTransaction(networkType, aliasAction, namespaceId, address);
    }

    public AddressAliasTransactionBody getAddressAliasTransactionBody() { return this.addressAliasTransactionBody; }

    public static EmbeddedAddressAliasTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedAddressAliasTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.addressAliasTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
