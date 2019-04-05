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
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

public class EntityBody {
    private final short version;
    private final EntityType entityType;
    private final NetworkType networkType;
    private Optional<PublicAccount> signer;

    EntityBody(NetworkType networkType, short version, EntityType entityType) { this(networkType, version, entityType, Optional.empty()); }

    EntityBody(NetworkType networkType, short version, EntityType entityType, PublicAccount signer) { this(networkType, version, entityType, Optional.of(signer)); }

    EntityBody(NetworkType networkType, short version, EntityType entityType, Optional<PublicAccount> signer) {
        Validate.notNull(networkType, "Network Type must not be null");
        Validate.notNull(entityType, "Entity Type must not be null");
        this.version = version;
        this.networkType = networkType;
        this.entityType = entityType;
        this.signer = signer;
    }

    EntityBody(final DataInput inputStream) throws Exception {
        ByteBuffer signerBuffer = ByteBuffer.allocate(32);
        inputStream.readFully(signerBuffer.array());
        int networkVersion = (int)ByteUtils.streamToShort(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.networkType = NetworkType.rawValueOf(networkVersion >> 8 & 0xFF);
        this.version = (short) (networkVersion & 0xFF);
        this.entityType = EntityType.loadFromBinary(inputStream);
        String signerPublicKey = new String(signerBuffer.array()).trim();
        if (signerPublicKey.isEmpty()) {
            this.signer = Optional.empty();
        } else {
            this.signer = Optional.of(PublicAccount.createFromPublicKey(signerPublicKey, this.networkType));
        }
    }

    /**
     * Returns the transaction version.
     *
     * @return transaction version
     */
    public short getVersion()  {
        return this.version;
    }

    /**
     * Returns the entity type.
     *
     * @return entity type
     */
    public EntityType getEntityType()  {
        return this.entityType;
    }

    /**
     * Returns the network type.
     *
     * @return the network type
     */
    public NetworkType getNetworkType()  {
        return this.networkType;
    }

    /**
     * Returns the transaction creator public account.
     *
     * @return signer public account
     */
    public Optional<PublicAccount> getSigner()  { return this.signer; }

    /**
     * Sets the public account of the transaction creator.
     *
     * @param signer public account
     */
    public void setSigner(PublicAccount signer) { this.signer = Optional.of(signer); }

    public static EntityBody loadFromBinary(final DataInput inputStream) throws Exception { return new EntityBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        ByteBuffer signerBuffer = ByteBuffer.allocate(32);
        if (this.signer.isPresent()) {
            signerBuffer.put(this.signer.get().getPublicKey().getBytes(), 0, 32);
        }
        stream.write(signerBuffer.array());
        short networkVersion = (short)(networkType.getValue() << 8 | version);
        stream.write(ByteUtils.shortToBytes(networkVersion, ByteOrder.LITTLE_ENDIAN));
        stream.write(this.entityType.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}