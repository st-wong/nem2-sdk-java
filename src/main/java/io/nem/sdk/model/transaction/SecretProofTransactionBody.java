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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SecretProofTransactionBody {
    private final LockHashAlgorithm lockHashAlgorithm;
    private final String secret;
    private final String proof;

    SecretProofTransactionBody(LockHashAlgorithm lockHashAlgorithm, String secretHash, String proof) {
        Validate.notNull(lockHashAlgorithm, "Lock Hash Algorithm must not be null");
        Validate.notNull(secretHash, "Secret Hash must not be null");
        Validate.notNull(proof, "Proof must not be null");
        this.lockHashAlgorithm = lockHashAlgorithm;
        this.secret = secretHash;
        this.proof = proof;
    }

    SecretProofTransactionBody(final DataInput inputStream) throws Exception {
        this.lockHashAlgorithm = LockHashAlgorithm.loadFromBinary(inputStream);
        ByteBuffer secretHashBuffer = ByteBuffer.allocate(32);
        inputStream.readFully(secretHashBuffer.array());
        this.secret = new String(secretHashBuffer.array());
        ByteBuffer recipientBuffer = ByteBuffer.allocate(ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN));
        inputStream.readFully(recipientBuffer.array());
        this.proof = new String(recipientBuffer.array());
    }

    public LockHashAlgorithm getLockHashAlgorithm() { return this.lockHashAlgorithm; }

    public String getSecret() { return this.secret; }

    public String getProof() { return this.proof; }

    public static SecretProofTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new SecretProofTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.lockHashAlgorithm.serialize());
        stream.write(this.secret.getBytes());
        stream.write(ByteUtils.byteToBytes((byte)proof.length(), ByteOrder.LITTLE_ENDIAN));
        stream.write(this.proof.getBytes());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
