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

import io.nem.core.utils.ByteUtils;

import java.nio.ByteOrder;
import java.security.SecureRandom;

public class MosaicNonce {
    /**
     * Mosaic nonce
     */
    private final int nonce;

    /**
     * Create a random Mosaic Nonce
     *
     * @return  MosaicNonce
     */
    public static MosaicNonce createRandom() {
        byte[] bytes = SecureRandom.getSeed(32);
        return new MosaicNonce(ByteUtils.bytesToInt(bytes, ByteOrder.nativeOrder()));
    }

    /**
     * Create a Mosaic Nonce from hexadecimal notation.
     *
     * @param   hexString
     * @return  Mosaic Nonce
     */
    public static MosaicNonce createFromHex(String hexString) { return new MosaicNonce(Integer.parseInt(hexString, 16)); }

    /**
     * Create MosaicNonce from
     *
     * @param nonce to generate Mosaic ID
     */
    MosaicNonce(int nonce) { this.nonce = nonce; }

    public int getNonce() { return this.nonce; }
}
