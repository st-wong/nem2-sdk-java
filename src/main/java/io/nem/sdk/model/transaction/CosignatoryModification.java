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

import io.nem.core.crypto.PublicKey;
import io.nem.core.utils.ByteUtils;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CosignatoryModification {
    private final CosignatoryModificationType cosignatoryModificationType;
    private final PublicKey cosignatoryPublicKey ;

    public CosignatoryModification (CosignatoryModificationType cosignatoryModificationType, PublicKey cosignatoryPublicKey) {
        Validate.notNull(cosignatoryModificationType, "Cosignatory Modification Type cannot be null");
        Validate.notNull(cosignatoryPublicKey, "Cosignatory Public Key cannot be null");
        this.cosignatoryModificationType = cosignatoryModificationType;
        this.cosignatoryPublicKey = cosignatoryPublicKey;
    }

    CosignatoryModification(final DataInput inputStream) throws Exception {
        this.cosignatoryModificationType = CosignatoryModificationType.loadFromBinary(inputStream);
        ByteBuffer cosignatoryPublicKeyBuffer = ByteBuffer.allocate(32);
        inputStream.readFully(cosignatoryPublicKeyBuffer.array());
        this.cosignatoryPublicKey = PublicKey.fromHexString(new String(cosignatoryPublicKeyBuffer.array()).trim());
    }

    public CosignatoryModificationType getCosignatoryModificationType()  { return this.cosignatoryModificationType; }

    public PublicKey getCosignatoryPublicKey()  { return this.cosignatoryPublicKey; }

    public static CosignatoryModification loadFromBinary(final DataInput inputStream) throws Exception { return new CosignatoryModification(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(this.cosignatoryModificationType.serialize());
        stream.write(this.cosignatoryPublicKey.getRaw(), 0, 32);
        stream.close();
        return bos.toByteArray();
    }
}
