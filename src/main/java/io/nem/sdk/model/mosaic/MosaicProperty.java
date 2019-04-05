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

import io.nem.core.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;

public class MosaicProperty {
    private final MosaicPropertyId id;
    private final long value;

    MosaicProperty(final DataInput inputStream) throws Exception {
        this.id = MosaicPropertyId.loadFromBinary(inputStream);
        this.value = ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    public MosaicPropertyId getId()  {
        return this.id;
    }

    public long getValue()  {
        return this.value;
    }

    public static MosaicProperty loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicProperty(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(this.id.serialize());
        stream.write(ByteUtils.longToBytes(this.value, ByteOrder.LITTLE_ENDIAN));
        stream.close();
        return bos.toByteArray();
    }
}
