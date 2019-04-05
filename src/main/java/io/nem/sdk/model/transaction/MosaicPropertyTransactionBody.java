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
import io.nem.sdk.model.mosaic.MosaicPropertyModification;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class MosaicPropertyTransactionBody {
    private final PropertyType propertyType;
    private final ArrayList<MosaicPropertyModification> mosaicPropertyModifications;

    MosaicPropertyTransactionBody(PropertyType propertyType, ArrayList<MosaicPropertyModification> mosaicPropertyModifications) {
        Validate.notNull(propertyType, "Property Type must not be null");
        Validate.notNull(mosaicPropertyModifications, "List of Address Property Modification must not be null");
        this.propertyType = propertyType;
        this.mosaicPropertyModifications = mosaicPropertyModifications;
    }

    MosaicPropertyTransactionBody(final DataInput inputStream) throws Exception {
        this.propertyType = PropertyType.loadFromBinary(inputStream);
        byte mosaicPropertyModificationsCount = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.mosaicPropertyModifications = new ArrayList<>(mosaicPropertyModificationsCount);
        for (int i = 0; i < mosaicPropertyModificationsCount; i++) {
            mosaicPropertyModifications.add(MosaicPropertyModification.loadFromBinary(inputStream));
        }
    }

    public PropertyType getPropertyType()  { return this.propertyType; }

    public ArrayList<MosaicPropertyModification> getMosaicPropertyModifications()  { return this.mosaicPropertyModifications; }

    public static MosaicPropertyTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicPropertyTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.propertyType.serialize());
        stream.write(ByteUtils.byteToBytes((byte)this.mosaicPropertyModifications.size(), ByteOrder.LITTLE_ENDIAN));
        for (int i = 0; i < this.mosaicPropertyModifications.size(); i++) {
            byte[] mosaicPropertyModification = this.mosaicPropertyModifications.get(i).serialize();
            stream.write(mosaicPropertyModification, 0, mosaicPropertyModification.length);
        }
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
