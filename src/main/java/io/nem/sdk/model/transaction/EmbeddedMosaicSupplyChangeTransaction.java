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

import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicSupplyChangeDirection;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class EmbeddedMosaicSupplyChangeTransaction extends EmbeddedTransaction {
    private MosaicSupplyChangeTransactionBody mosaicSupplyChangeTransactionBody;

    EmbeddedMosaicSupplyChangeTransaction(NetworkType networkType, MosaicId mosaicId, MosaicSupplyChangeDirection mosaicSupplyChangeDirection, long delta) {
        this(networkType, mosaicId, mosaicSupplyChangeDirection, delta, Optional.empty());
    }

    EmbeddedMosaicSupplyChangeTransaction(NetworkType networkType, MosaicId mosaicId, MosaicSupplyChangeDirection mosaicSupplyChangeDirection, long delta, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(137), new EntityBody(networkType, (short)2, EntityType.MOSAIC_SUPPLY_CHANGE), transactionInfo);
        this.mosaicSupplyChangeTransactionBody = new MosaicSupplyChangeTransactionBody(mosaicId, mosaicSupplyChangeDirection, delta);
    }

    EmbeddedMosaicSupplyChangeTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.mosaicSupplyChangeTransactionBody = MosaicSupplyChangeTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedMosaicSupplyChangeTransaction create(MosaicId mosaicId, MosaicSupplyChangeDirection mosaicSupplyChangeDirection, long delta, NetworkType networkType) {
        return new EmbeddedMosaicSupplyChangeTransaction(networkType, mosaicId, mosaicSupplyChangeDirection, delta);
    }

    public MosaicSupplyChangeTransactionBody getMosaicSupplyChangeTransactionBody() { return this.mosaicSupplyChangeTransactionBody; }

    public static EmbeddedMosaicSupplyChangeTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedMosaicSupplyChangeTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.mosaicSupplyChangeTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
