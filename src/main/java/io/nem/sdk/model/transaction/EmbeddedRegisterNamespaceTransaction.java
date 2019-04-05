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

import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.namespace.NamespaceType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class EmbeddedRegisterNamespaceTransaction extends EmbeddedTransaction {
    private RegisterNamespaceTransactionBody registerNamespaceTransactionBody;

    EmbeddedRegisterNamespaceTransaction(NetworkType networkType, NamespaceType namespaceType, NamespaceId namespaceId, String name, Optional<BlockDuration> duration, Optional<NamespaceId> parentNamespaceId) {
        this(networkType, namespaceType, namespaceId, name, duration, parentNamespaceId, Optional.empty());
    }

    EmbeddedRegisterNamespaceTransaction(NetworkType networkType, NamespaceType namespaceType, NamespaceId namespaceId, String name, Optional<BlockDuration> duration, Optional<NamespaceId> parentNamespaceId, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(138 + name.length()), new EntityBody(networkType, (short)2, EntityType.REGISTER_NAMESPACE), transactionInfo);
        this.registerNamespaceTransactionBody = new RegisterNamespaceTransactionBody(namespaceType, namespaceId, name, duration, parentNamespaceId);
    }

    EmbeddedRegisterNamespaceTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.registerNamespaceTransactionBody = RegisterNamespaceTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedRegisterNamespaceTransaction create(NamespaceType namespaceType, NamespaceId namespaceId, String name, Optional<BlockDuration> duration, Optional<NamespaceId> parentNamespaceId, NetworkType networkType) {
        return new EmbeddedRegisterNamespaceTransaction(networkType, namespaceType, namespaceId, name, duration, parentNamespaceId, Optional.empty());
    }

    public RegisterNamespaceTransactionBody getRegisterNamespaceTransactionBody() { return this.registerNamespaceTransactionBody; }

    public static EmbeddedRegisterNamespaceTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedRegisterNamespaceTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.registerNamespaceTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}