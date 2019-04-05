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

import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

public class AggregateTransactionDetachedCosignature extends AggregateTransactionCosignature {
    private final String parentHash;

    public AggregateTransactionDetachedCosignature(String signature, PublicAccount signer, String parentHash) {
        super(signature, signer);
        this.parentHash = parentHash;
    }

    AggregateTransactionDetachedCosignature(final DataInput inputStream, final NetworkType networkType) throws Exception {
        super(inputStream, networkType);
        ByteBuffer parentHashBuffer = ByteBuffer.allocate(32);
        inputStream.readFully(parentHashBuffer.array());
        this.parentHash = new String(parentHashBuffer.array());
    }

    public static AggregateTransactionDetachedCosignature loadFromBinary(final DataInput inputStream, final NetworkType networkType) throws Exception {
        return new AggregateTransactionDetachedCosignature(inputStream, networkType);
    }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.parentHash.getBytes(), 0, 32);
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
