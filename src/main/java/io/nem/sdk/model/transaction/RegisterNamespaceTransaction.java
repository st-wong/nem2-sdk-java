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

import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.namespace.NamespaceType;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

/**
 * Accounts can rent a namespace for an amount of blocks and after a this renew the contract.
 * This is done via a RegisterNamespaceTransaction.
 *
 * @since 1.0
 */
public class RegisterNamespaceTransaction extends Transaction {
    private final RegisterNamespaceTransactionBody registerNamespaceTransactionBody;

    RegisterNamespaceTransaction(NetworkType networkType, Deadline deadline, long max_fee, NamespaceType namespaceType, NamespaceId namespaceId, String name, Optional<BlockDuration> duration, Optional<NamespaceId> parentNamespaceId) {
        this(networkType, deadline, max_fee, namespaceType, namespaceId, name, duration, parentNamespaceId, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public RegisterNamespaceTransaction(NetworkType networkType, Deadline deadline, long max_fee, NamespaceType namespaceType, NamespaceId namespaceId, String name, Optional<BlockDuration> duration, Optional<NamespaceId> parentNamespaceId, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, namespaceType, namespaceId, name, duration, parentNamespaceId, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    RegisterNamespaceTransaction(NetworkType networkType, Deadline deadline, long max_fee, NamespaceType namespaceType, NamespaceId namespaceId, String name, Optional<BlockDuration> duration, Optional<NamespaceId> parentNamespaceId, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(138 + name.length()), new EntityBody(networkType, (short)2, EntityType.REGISTER_NAMESPACE, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.registerNamespaceTransactionBody = new RegisterNamespaceTransactionBody(namespaceType, namespaceId, name, duration, parentNamespaceId);
    }

    RegisterNamespaceTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.registerNamespaceTransactionBody = RegisterNamespaceTransactionBody.loadFromBinary(inputStream);
    }

    /**
     * Create a root namespace object.
     *
     * @param deadline      The deadline to include the transaction.
     * @param namespaceName The namespace name.
     * @param duration      The duration of the namespace.
     * @param networkType   The network type.
     * @return instance of RegisterNamespaceTransaction
     */
    public static RegisterNamespaceTransaction createRootNamespace(Deadline deadline, String namespaceName, long duration, NetworkType networkType) {
        Validate.notNull(namespaceName, "NamespaceName must not be null");
        return new RegisterNamespaceTransaction(networkType, deadline, 0, NamespaceType.ROOT, new NamespaceId(IdGenerator.generateNamespaceId(namespaceName)), namespaceName, Optional.of(new BlockDuration(duration)), Optional.empty());
    }

    /**
     * Create a sub namespace object.
     *
     * @param deadline      - The deadline to include the transaction.
     * @param namespaceName - The namespace name.
     * @param parentId      - The parent id name.
     * @param networkType   - The network type.
     * @return instance of RegisterNamespaceTransaction
     */
    public static RegisterNamespaceTransaction createSubNamespace(Deadline deadline, String namespaceName, NamespaceId parentId, NetworkType networkType) {
        Validate.notNull(namespaceName, "NamespaceName must not be null");
        Validate.notNull(parentId, "ParentId must not be null");
        return new RegisterNamespaceTransaction(networkType, deadline, 0, NamespaceType.CHILD, new NamespaceId(IdGenerator.generateSubNamespaceIdFromParentId(parentId.getId(), namespaceName)), namespaceName, Optional.empty(), Optional.of(parentId));
    }

    public RegisterNamespaceTransactionBody getRegisterNamespaceTransactionBody()  { return this.registerNamespaceTransactionBody; }

    public static RegisterNamespaceTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new RegisterNamespaceTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.registerNamespaceTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}