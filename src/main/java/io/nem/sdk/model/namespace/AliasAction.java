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

package io.nem.sdk.model.namespace;

import io.nem.core.utils.ByteUtils;

import java.io.DataInput;
import java.nio.ByteOrder;

public enum AliasAction {
    LINK((byte)0),
    UNLINK((byte)1);

    private final byte value;

    AliasAction(byte value)  {
        this.value = value;
    }

    public static AliasAction rawValueOf(byte value) {
        switch (value) {
            case (byte)0:
                return AliasAction.LINK;
            case (byte)1:
                return AliasAction.UNLINK;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    public static AliasAction loadFromBinary(final DataInput inputStream) throws Exception { return AliasAction.rawValueOf(ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN)); }

    public byte[] serialize() { return ByteUtils.byteToBytes(this.value, ByteOrder.LITTLE_ENDIAN); }
}