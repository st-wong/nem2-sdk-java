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
import io.nem.sdk.model.transaction.PropertyModificationType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;

public class MosaicPropertyModification {
    private final PropertyModificationType propertyModificationType;
    private final MosaicId mosaicId;

    MosaicPropertyModification(final DataInput inputStream) throws Exception {
        this.propertyModificationType = PropertyModificationType.loadFromBinary(inputStream);
        this.mosaicId = new MosaicId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
    }

    public PropertyModificationType getModificationType()  {
        return this.propertyModificationType;
    }

    public MosaicId getMosaicId()  { return this.mosaicId; }

    public static MosaicPropertyModification loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicPropertyModification(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(this.propertyModificationType.serialize());
        stream.write(ByteUtils.longToBytes(this.mosaicId.getId(), ByteOrder.LITTLE_ENDIAN));
        stream.close();
        return bos.toByteArray();
    }
}
