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

public enum PropertyType {
    ADDRESS((byte)1),
    MOSAICID((byte)2),
    TRANSACTION_TYPE((byte)4),
    SENTINEL((byte)5),
    BLOCK((byte)128);

    private final byte value;

    PropertyType(byte value)  {
        this.value = value;
    }

    public static PropertyType rawValueOf(byte value) {
        switch (value) {
            case (byte)1:
                return PropertyType.ADDRESS;
            case (byte)2:
                return PropertyType.MOSAICID;
            case (byte)4:
                return PropertyType.TRANSACTION_TYPE;
            case (byte)5:
                return PropertyType.SENTINEL;
            case (byte)128:
                return PropertyType.BLOCK;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    public static PropertyType loadFromBinary(final DataInput inputStream) throws Exception { return PropertyType.rawValueOf(ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN)); }

    public byte[] serialize() { return ByteUtils.byteToBytes(this.value, ByteOrder.LITTLE_ENDIAN); }
}
