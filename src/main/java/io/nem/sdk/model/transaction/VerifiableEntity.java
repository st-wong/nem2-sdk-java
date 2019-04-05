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

import java.io.DataInput;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * The Verifiable Entity object containing the signature of the transaction.
 *
 * @since 1.0
 */
public class VerifiableEntity {
    private final Optional<String> signature;

    /**
     * Constructor
     *
     * @param signature (optional) of the Transaction.
     */
    VerifiableEntity(Optional<String> signature) { this.signature = signature; }

    /**
     * Constructor
     *
     * @param signature of the Transaction.
     */
    public VerifiableEntity(String signature) {
        if (signature == null)
            throw new NullPointerException("signature");

        if (signature.length() != 64)
            throw new IllegalArgumentException("signature should be 64 bytes");

        this.signature = Optional.of(signature);
    }

    /**
     * Returns the transaction signature (missing if part of an aggregate transaction).
     *
     * @return transaction signature
     */
    public Optional<String> getSignature() { return this.signature; }

    /**
     * Load an instance of Verifiable Entity object from a binary layout.
     *
     * @param inputStream of the binary layout.
     * @return an instance of Verifiable Entity object from the binary layout.
     * @throws Exception when there are issues in reading the data input.
     */
    public static VerifiableEntity loadFromBinary(DataInput inputStream) throws Exception {
        ByteBuffer signatureBuffer = ByteBuffer.allocate(64);
        inputStream.readFully(signatureBuffer.array());
        return new VerifiableEntity(new String(signatureBuffer.array()));
    }

    /**
     * Serialize the Verifiable Entity object to a binary layout.
     *
     * @return an array of bytes representing the binary layout of the Verifiable Entity object.
     */
    public byte[] serialize() {
        ByteBuffer bb = ByteBuffer.allocate(64);
        if (this.signature.isPresent()) {
            bb.put(this.signature.get().getBytes());
        }
        return bb.array();
    }
}
