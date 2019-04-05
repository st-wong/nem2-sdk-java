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
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.namespace.NamespaceName;
import io.nem.sdk.model.namespace.NamespaceType;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

public class RegisterNamespaceTransactionBody {
    private final NamespaceType namespaceType;
    private final Optional<BlockDuration> duration;
    private final NamespaceName namespaceName;

    RegisterNamespaceTransactionBody(NamespaceType namespaceType, NamespaceId namespaceId, String name, Optional<BlockDuration> duration, Optional<NamespaceId> parentNamespaceId) {
        Validate.notNull(namespaceType, "Namespace Type must not be null");
        Validate.notNull(name, "Namespace name must not be null");
        Validate.notNull(namespaceId, "Namespace ID must not be null");
        this.namespaceType = namespaceType;
        this.duration = duration;
        this.namespaceName = new NamespaceName(namespaceId, name, parentNamespaceId);
    }

    RegisterNamespaceTransactionBody(final DataInput inputStream) throws Exception {
        this.namespaceType = NamespaceType.loadFromBinary(inputStream);
        Optional<NamespaceId> parentID = Optional.empty();
        if (namespaceType == NamespaceType.ROOT) {
            this.duration = Optional.of(new BlockDuration(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN)));
        } else {
            this.duration = Optional.empty();
            parentID = Optional.of(new NamespaceId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN)));
        }
        NamespaceId namespaceId = new NamespaceId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
        ByteBuffer nameBuffer = ByteBuffer.allocate(ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN));
        inputStream.readFully(nameBuffer.array());
        this.namespaceName = new NamespaceName(namespaceId, new String(nameBuffer.array()), parentID);
    }

    /**
     * Returns namespace type either RootNamespace or SubNamespace.
     *
     * @return namespace type
     */
    public NamespaceType getNamespaceType()  { return this.namespaceType; }

    /**
     * Returns number of blocks a namespace is active.
     *
     * @return namespace renting duration
     */
    public Optional<BlockDuration> getBlockDuration()  { return this.duration; }

    /**
     * Returns id of the namespace derived from namespaceName.
     * When creating a sub namespace the namespaceId is derived from namespaceName and parentId.
     *
     * @return namespace id
     */
    public NamespaceId getNamespaceId() { return this.namespaceName.getNamespaceId(); }

    /**
     * Returns namespace name.
     *
     * @return namespace name
     */
    public String getNamespaceName() { return this.namespaceName.getName(); }

    /**
     * The id of the parent sub namespace.
     *
     * @return sub namespace
     */
    public Optional<NamespaceId> getParentNamespaceId() { return this.namespaceName.getParentId(); }

    public static RegisterNamespaceTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new RegisterNamespaceTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.namespaceType.serialize());
        if (this.duration.isPresent()) {
            stream.write(ByteUtils.longToBytes(this.duration.get().getValue(), ByteOrder.LITTLE_ENDIAN));
        } else {
            stream.write(ByteUtils.longToBytes(this.namespaceName.getParentId().get().getId(), ByteOrder.LITTLE_ENDIAN));
        }
        stream.write(ByteUtils.byteToBytes((byte)this.namespaceName.getName().length(), ByteOrder.LITTLE_ENDIAN));
        stream.write(this.namespaceName.getName().getBytes());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
