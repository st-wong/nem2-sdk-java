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
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The model representing cosignature of an aggregate transaction.
 *
 * @since 1.0
 */
public class AggregateTransactionCosignature {
    private final String signature;
    private final PublicAccount signer;

    public AggregateTransactionCosignature(String signature, PublicAccount signer) {
        this.signature = signature;
        this.signer = signer;
    }

    AggregateTransactionCosignature(final DataInput inputStream, final NetworkType networkType) throws Exception {
        ByteBuffer signerBuffer = ByteBuffer.allocate(32);
        inputStream.readFully(signerBuffer.array());
        ByteBuffer signatureBuffer = ByteBuffer.allocate(64);
        inputStream.readFully(signatureBuffer.array());
        this.signer = PublicAccount.createFromPublicKey(new String(signerBuffer.array()).trim(), networkType);
        this.signature = new String(signatureBuffer.array());
    }

    /**
     * Returns the signature of aggregate transaction done by the cosigner.
     *
     * @return String
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Returns cosigner public account.
     *
     * @return {@link PublicAccount}
     */
    public PublicAccount getSigner() {
        return signer;
    }

    public static AggregateTransactionCosignature loadFromBinary(final DataInput inputStream, final NetworkType networkType) throws Exception { return new AggregateTransactionCosignature(inputStream, networkType); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.signer.getPublicKey().getBytes(), 0, 32);
        stream.write(this.signature.getBytes(), 0 , 64);
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}