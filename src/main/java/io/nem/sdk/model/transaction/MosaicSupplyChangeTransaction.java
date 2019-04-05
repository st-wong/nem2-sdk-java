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
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicSupplyChangeDirection;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class MosaicSupplyChangeTransaction extends Transaction {
    private final MosaicSupplyChangeTransactionBody mosaicSupplyChangeTransactionBody;

    MosaicSupplyChangeTransaction(NetworkType networkType, Deadline deadline, long max_fee, MosaicId mosaicId, MosaicSupplyChangeDirection mosaicSupplyChangeDirection, long delta) {
        this(networkType, deadline, max_fee, mosaicId, mosaicSupplyChangeDirection, delta, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public MosaicSupplyChangeTransaction(NetworkType networkType, Deadline deadline, long max_fee, MosaicId mosaicId, MosaicSupplyChangeDirection mosaicSupplyChangeDirection, long delta, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, mosaicId, mosaicSupplyChangeDirection, delta, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    MosaicSupplyChangeTransaction(NetworkType networkType, Deadline deadline, long max_fee, MosaicId mosaicId, MosaicSupplyChangeDirection mosaicSupplyChangeDirection, long delta, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(137), new EntityBody(networkType, (short)2, EntityType.MOSAIC_SUPPLY_CHANGE, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.mosaicSupplyChangeTransactionBody = new MosaicSupplyChangeTransactionBody(mosaicId, mosaicSupplyChangeDirection, delta);
    }
    MosaicSupplyChangeTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.mosaicSupplyChangeTransactionBody = MosaicSupplyChangeTransactionBody.loadFromBinary(inputStream);
    }

    public MosaicSupplyChangeTransactionBody getMosaicSupplyChangeTransactionBody() { return this.mosaicSupplyChangeTransactionBody; }

    public static MosaicSupplyChangeTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicSupplyChangeTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.mosaicSupplyChangeTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
