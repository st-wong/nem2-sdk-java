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
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.mosaic.Mosaic;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HashLockTransactionBody {
    private final Mosaic mosaic;
    private final BlockDuration duration;
    private final String secret;

    HashLockTransactionBody(Mosaic mosaic, BlockDuration duration, String secretHash) {
        Validate.notNull(mosaic, "Mosaic must not be null");
        Validate.notNull(duration, "Block Duration must not be null");
        Validate.notNull(secretHash, "Secret Hash must not be null");
        this.mosaic = mosaic;
        this.duration = duration;
        this.secret = secretHash;
    }

    HashLockTransactionBody(final DataInput inputStream) throws Exception {
        this.mosaic = Mosaic.loadFromBinary(inputStream);
        this.duration = new BlockDuration(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
        ByteBuffer secretHashBuffer = ByteBuffer.allocate(32);
        inputStream.readFully(secretHashBuffer.array());
        this.secret = new String(secretHashBuffer.array());
    }

    public Mosaic getMosaic()  { return this.mosaic; }

    public BlockDuration getBlockDuration()  { return this.duration; }

    public String getSecret() { return this.secret; }

    public static HashLockTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new HashLockTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.mosaic.serialize());
        stream.write(ByteUtils.longToBytes(this.duration.getValue(), ByteOrder.LITTLE_ENDIAN));
        stream.write(this.secret.getBytes());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}