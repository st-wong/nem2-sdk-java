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

package io.nem.sdk.model.mosaic;

import io.nem.core.utils.ArrayUtils;
import io.nem.core.utils.ByteUtils;

import java.io.DataInput;
import java.nio.ByteOrder;

public class Mosaic {
    private final long mosaicId;
    private final long amount;

    public Mosaic(long mosaicId, long amount) {
        this.mosaicId = mosaicId;
        this.amount = amount;
    }

    Mosaic(DataInput inputStream) throws Exception {
        this.mosaicId = ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.amount =ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    public long getMosaicId()  { return this.mosaicId; }

    public long getAmount()  { return this.amount; }

    public static Mosaic loadFromBinary(final DataInput inputStream) throws Exception { return new Mosaic(inputStream); }

    public byte[] serialize() {
        byte[] mosaicIdBytes = ByteUtils.longToBytes(this.mosaicId, ByteOrder.LITTLE_ENDIAN);
        byte[] amountBytes = ByteUtils.longToBytes(this.amount, ByteOrder.LITTLE_ENDIAN);
        return ArrayUtils.concat(mosaicIdBytes, amountBytes);
    }
}
