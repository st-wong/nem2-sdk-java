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

import java.io.DataInput;
import java.nio.ByteOrder;

public enum EntityType {
    RESERVED((short)0),

    // Mosaic
    /**
     * Mosaic definition transaction type.
     */
    MOSAIC_DEFINITION((short)0x414D),

    /**
     * Mosaic supply change transaction.
     */
    MOSAIC_SUPPLY_CHANGE((short)0x424D),

    // Namespace
    /**
     * Register namespace transaction type.
     */
    REGISTER_NAMESPACE((short)0x414E),

    /**
     * Address alias transaction type.
     */
    ADDRESS_ALIAS((short)0x424E),

    /**
     * Mosaic alias transaction type.
     */
    MOSAIC_ALIAS((short)0x434E),

    // Transfer
    /**
     * Transfer Transaction transaction type.
     */
    TRANSFER((short)0x4154),

    // Multisignature
    /**
     * Modify multisig account transaction type.
     */
    MODIFY_MULTISIG_ACCOUNT((short)0x4155),

    /**
     * Aggregate complete transaction type.
     */
    AGGREGATE_COMPLETE((short)0x4141),

    /**
     * Aggregate bonded transaction type
     */
    AGGREGATE_BONDED((short)0x4241),

    /**
     * Hash Lock transaction type
     */
    LOCK((short)0x4148),

    // Account filters
    /**
     * Account properties address transaction type
     */
    ACCOUNT_PROPERTIES_ADDRESS((short)0x4151),

    /**
     * Account properties mosaic transaction type
     */
    ACCOUNT_PROPERTIES_MOSAIC((short)0x4250),

    /**
     * Account properties entity type transaction type
     */
    ACCOUNT_PROPERTIES_ENTITY_TYPE((short)0x4350),

    /**
     * Account properties transaction type
     */
    ACCOUNT_PROPERTIES_TRANSACTION_ENTITY_TYPE((short)0x4153),

    // Cross-chain swaps
    /**
     * Secret Lock Transaction type
     */
    SECRET_LOCK((short)0x4152),

    /**
     * Secret Proof transaction type
     */
    SECRET_PROOF((short)0x4252),

    // Remote harvesting
    /**
     * Account link transaction type
     */
    ACCOUNT_LINK((short)0x414C);

    private final short value;

    EntityType(short value)  { this.value = value; }

    public short getValue() {
        return this.value;
    }

    /**
     * Static constructor converting transaction type raw value to enum instance.
     *
     * @return {@link EntityType}
     */
    public static EntityType rawValueOf(short value) {
        switch (value) {
            case (short)0:
                return EntityType.RESERVED;
            case (short)0x414D:
                return EntityType.MOSAIC_DEFINITION;
            case (short)0x424D:
                return EntityType.MOSAIC_SUPPLY_CHANGE;
            case (short)0x414E:
                return EntityType.REGISTER_NAMESPACE;
            case (short)0x424E:
                return EntityType.ADDRESS_ALIAS;
            case (short)0x434E:
                return EntityType.MOSAIC_ALIAS;
            case (short)0x4154:
                return EntityType.TRANSFER;
            case (short)0x4155:
                return EntityType.MODIFY_MULTISIG_ACCOUNT;
            case (short)0x4141:
                return EntityType.AGGREGATE_COMPLETE;
            case (short)0x4241:
                return EntityType.AGGREGATE_BONDED;
            case (short)0x4148:
                return EntityType.LOCK;
            case (short)0x4151:
                return EntityType.ACCOUNT_PROPERTIES_ADDRESS;
            case (short)0x4250:
                return EntityType.ACCOUNT_PROPERTIES_MOSAIC;
            case (short)0x4350:
                return EntityType.ACCOUNT_PROPERTIES_ENTITY_TYPE;
            case (short)0x4153:
                return EntityType.ACCOUNT_PROPERTIES_TRANSACTION_ENTITY_TYPE;
            case (short)0x4152:
                return EntityType.SECRET_LOCK;
            case (short)0x4252:
                return EntityType.SECRET_PROOF;
            case (short)0x414C:
                return EntityType.ACCOUNT_LINK;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    public static EntityType loadFromBinary(final DataInput inputStream) throws Exception { return EntityType.rawValueOf(ByteUtils.streamToShort(inputStream, ByteOrder.LITTLE_ENDIAN)); }

    public byte[] serialize() { return ByteUtils.shortToBytes(this.value, ByteOrder.LITTLE_ENDIAN); }
}
