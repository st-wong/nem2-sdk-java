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

import io.nem.core.crypto.Hashes;
import io.nem.core.utils.ByteUtils;
import io.nem.sdk.model.account.PublicAccount;

import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * The mosaic id structure describes mosaic id
 *
 * @since 1.0
 */
public class MosaicId {
    private final long id;

    /**
     * Create MosaicId from Int nonce
     *
     * @param nonce to generate Mosaic ID
     * @param owner of Mosaic
     */
    public MosaicId(int nonce, PublicAccount owner) {
        byte[] generateBytes = Hashes.sha3_256(ByteUtils.intToBytes(nonce, ByteOrder.nativeOrder()), owner.getPublicKey().getBytes());
        id = ByteUtils.bytesToLong(generateBytes, ByteOrder.nativeOrder());
    }

    /**
     * Create MosaicId from random nonce
     *
     * @param owner of Mosaic
     */

    public MosaicId(PublicAccount owner) {
        byte[] generateBytes = Hashes.sha3_256(new SecureRandom().generateSeed(4), owner.getPublicKey().getBytes());
        id = ByteUtils.bytesToLong(generateBytes, ByteOrder.nativeOrder());
    }

    public MosaicId(long mosaicID) { this.id = mosaicID; }

    public MosaicId(String hexMosaicID) { this.id = Long.parseLong(hexMosaicID, 16); }

    /**
     * Returns mosaic long id
     *
     * @return mosaic long id
     */
    public long getId() {
        return id;
    }

    /**
     * Compares mosaicIds for equality.
     *
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MosaicId)) return false;
        MosaicId mosaicId1 = (MosaicId) o;
        return Objects.equals(id, mosaicId1.id);
    }
}
