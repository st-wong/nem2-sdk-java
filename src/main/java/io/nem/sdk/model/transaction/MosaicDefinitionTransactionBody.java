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
import io.nem.sdk.model.mosaic.MosaicFlags;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicNonce;
import io.nem.sdk.model.mosaic.MosaicProperty;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class MosaicDefinitionTransactionBody {
    private final MosaicId mosaicId;
    private final MosaicFlags mosaicFlags;
    private final byte divisibility;
    private final MosaicNonce mosaicNonce;
    private final ArrayList<MosaicProperty> mosaicProperties;

    MosaicDefinitionTransactionBody(MosaicId mosaicId, MosaicFlags mosaicFlags, byte divisibility, MosaicNonce mosaicNonce, ArrayList<MosaicProperty> mosaicProperties) {
        Validate.notNull(mosaicId, "Mosaic ID must not be null");
        Validate.notNull(mosaicFlags, "Mosaic Flags must not be null");
        Validate.notNull(mosaicNonce, "Mosaic Nonce must not be null");
        Validate.notNull(mosaicProperties, "List of Mosaic Properties must not be null");
        this.mosaicId = mosaicId;
        this.mosaicFlags = mosaicFlags;
        this.divisibility = divisibility;
        this.mosaicNonce = mosaicNonce;
        this.mosaicProperties = mosaicProperties;
    }

    MosaicDefinitionTransactionBody(final DataInput inputStream) throws Exception {
        this.mosaicNonce = MosaicNonce.createFromHex(Integer.toHexString(ByteUtils.streamToInt(inputStream, ByteOrder.LITTLE_ENDIAN)));
        this.mosaicId = new MosaicId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
        byte mosaicPropertiesCount = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.mosaicFlags = MosaicFlags.loadFromBinary(inputStream);
        this.divisibility = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.mosaicProperties = new ArrayList<>(mosaicPropertiesCount);
        for (int i = 0; i < mosaicPropertiesCount; i++) {
            mosaicProperties.add(MosaicProperty.loadFromBinary(inputStream));
        }
    }

    public MosaicId getMosaicId() { return this.mosaicId; }

    public MosaicFlags getMosaicFlags() { return this.mosaicFlags; }

    public byte getDivisibility() { return this.divisibility; }

    public MosaicNonce getMosaicNonce()  { return this.mosaicNonce; }

    public ArrayList<MosaicProperty> getMosaicProperties()  { return this.mosaicProperties; }

    public static MosaicDefinitionTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicDefinitionTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(ByteUtils.intToBytes(this.mosaicNonce.getNonce(), ByteOrder.LITTLE_ENDIAN));
        stream.write(ByteUtils.longToBytes(this.mosaicId.getId(), ByteOrder.LITTLE_ENDIAN));
        stream.write(ByteUtils.byteToBytes((byte)this.mosaicProperties.size(), ByteOrder.LITTLE_ENDIAN));
        stream.write(this.mosaicFlags.serialize());
        stream.write(ByteUtils.byteToBytes(this.divisibility, ByteOrder.LITTLE_ENDIAN));
        for (int i = 0; i < this.mosaicProperties.size(); i++) {
            byte[] mosaicPropertyModification = this.mosaicProperties.get(i).serialize();
            stream.write(mosaicPropertyModification, 0, mosaicPropertyModification.length);
        }
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
