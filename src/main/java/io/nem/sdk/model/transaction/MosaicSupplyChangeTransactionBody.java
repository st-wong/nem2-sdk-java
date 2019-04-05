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
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicProperty;
import io.nem.sdk.model.mosaic.MosaicSupplyChangeDirection;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;

public class MosaicSupplyChangeTransactionBody {
    private final MosaicId mosaicId;
    private final MosaicSupplyChangeDirection mosaicSupplyChangeDirection;
    private final long delta;

    MosaicSupplyChangeTransactionBody(MosaicId mosaicId, MosaicSupplyChangeDirection mosaicSupplyChangeDirection, long delta) {
        Validate.notNull(mosaicId, "Mosaic ID must not be null");
        Validate.notNull(mosaicSupplyChangeDirection, "Mosaic Supply Change Direction must not be null");

        this.mosaicId = mosaicId;
        this.mosaicSupplyChangeDirection = mosaicSupplyChangeDirection;
        this.delta = delta;
    }

    MosaicSupplyChangeTransactionBody(final DataInput inputStream) throws Exception {
        this.mosaicId = new MosaicId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
        this.mosaicSupplyChangeDirection = MosaicSupplyChangeDirection.loadFromBinary(inputStream);
        this.delta = ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    public MosaicId getMosaicId() { return this.mosaicId; }

    public MosaicSupplyChangeDirection getMosaicSupplyChangeDirection() { return this.mosaicSupplyChangeDirection; }

    public long getDelta() { return this.delta; }

    public static MosaicSupplyChangeTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicSupplyChangeTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(ByteUtils.longToBytes(this.mosaicId.getId(), ByteOrder.LITTLE_ENDIAN));
        stream.write(this.mosaicSupplyChangeDirection.serialize());
        stream.write(ByteUtils.longToBytes(this.delta, ByteOrder.LITTLE_ENDIAN));
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
