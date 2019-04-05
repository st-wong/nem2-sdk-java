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
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ModifyMultisigAccountTransactionBody {
    private final byte minApprovalDelta;
    private final byte minRemovalDelta;
    private final List<CosignatoryModification> cosignatoryModifications;

    ModifyMultisigAccountTransactionBody(byte minRemovalDelta, byte minApprovalDelta, List<CosignatoryModification> cosignatoryModifications) {
        Validate.notNull(cosignatoryModifications,"List of Cosignatory Modification must not be null");
        this.minApprovalDelta = minApprovalDelta;
        this.minRemovalDelta = minRemovalDelta;
        this.cosignatoryModifications = cosignatoryModifications;
    }

    ModifyMultisigAccountTransactionBody(final DataInput inputStream) throws Exception {
        this.minRemovalDelta = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.minApprovalDelta = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        byte cosignModificationCount = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.cosignatoryModifications = new ArrayList<>(cosignModificationCount);
        for (int i = 0; i < cosignModificationCount; i++) {
            cosignatoryModifications.add(CosignatoryModification.loadFromBinary(inputStream));
        }
    }

    public byte getMinApprovalDelta()  { return this.minApprovalDelta; }

    public byte getMinRemovalDelta()  { return this.minRemovalDelta; }

    public List<CosignatoryModification> getCosignatoryModifications()  { return this.cosignatoryModifications; }

    public static ModifyMultisigAccountTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new ModifyMultisigAccountTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(ByteUtils.byteToBytes(minRemovalDelta, ByteOrder.LITTLE_ENDIAN));
        stream.write(ByteUtils.byteToBytes(minApprovalDelta, ByteOrder.LITTLE_ENDIAN));
        stream.write(ByteUtils.byteToBytes((byte)this.cosignatoryModifications.size(), ByteOrder.LITTLE_ENDIAN));
        for (int i = 0; i < this.cosignatoryModifications.size(); i++) {
            byte[] cosignatoryModification = this.cosignatoryModifications.get(i).serialize();
            stream.write(cosignatoryModification, 0, cosignatoryModification.length);
        }
        stream.close();
        return bos.toByteArray();
    }
}
