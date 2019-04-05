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

public class SizePrefixedEntity {
    private final int size;

    SizePrefixedEntity(int size) { this.size = size; }

    SizePrefixedEntity(DataInput inputStream) throws Exception { this.size = ByteUtils.streamToInt(inputStream, ByteOrder.LITTLE_ENDIAN); }

    public int getSize()  { return this.size; }

    public static SizePrefixedEntity loadFromBinary(DataInput inputStream) throws Exception { return new SizePrefixedEntity(inputStream); }

    public byte[] serialize() { return ByteUtils.intToBytes(this.size, ByteOrder.LITTLE_ENDIAN); }
}