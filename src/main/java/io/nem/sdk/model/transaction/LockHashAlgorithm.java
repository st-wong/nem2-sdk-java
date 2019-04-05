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

public enum LockHashAlgorithm {
    SHA3_256((byte)0),
    KECCAK_256((byte)1),
    HASH_160((byte)2),
    HASH_256((byte)3);

    private final byte value;

    LockHashAlgorithm(byte value)  {
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }

    public static LockHashAlgorithm rawValueOf(byte value) {
        switch (value) {
            case (byte)0:
                return LockHashAlgorithm.SHA3_256;
            case (byte)1:
                return LockHashAlgorithm.KECCAK_256;
            case (byte)2:
                return LockHashAlgorithm.HASH_160;
            case (byte)3:
                return LockHashAlgorithm.HASH_256;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    public static LockHashAlgorithm loadFromBinary(final DataInput inputStream) throws Exception { return LockHashAlgorithm.rawValueOf(ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN)); }

    public byte[] serialize() { return ByteUtils.shortToBytes(this.value, ByteOrder.LITTLE_ENDIAN); }
}