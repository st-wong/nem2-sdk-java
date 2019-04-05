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
import io.nem.sdk.model.mosaic.MosaicPropertyModification;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Optional;

public class MosaicPropertyTransaction extends Transaction {
    private final MosaicPropertyTransactionBody mosaicPropertyTransactionBody;

    MosaicPropertyTransaction(NetworkType networkType, Deadline deadline, long max_fee, PropertyType propertyType, ArrayList<MosaicPropertyModification> mosaicPropertyModifications) {
        this(networkType, deadline, max_fee, propertyType, mosaicPropertyModifications, new VerifiableEntity(Optional.empty()), Optional.empty());
    }

    MosaicPropertyTransaction(NetworkType networkType, Deadline deadline, long max_fee, PropertyType propertyType, ArrayList<MosaicPropertyModification> mosaicPropertyModifications, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(122 + (9 * mosaicPropertyModifications.size())), new EntityBody(networkType, (short)1, EntityType.ACCOUNT_PROPERTIES_MOSAIC), deadline, max_fee, verifiableEntity, transactionInfo);
        this.mosaicPropertyTransactionBody = new MosaicPropertyTransactionBody(propertyType, mosaicPropertyModifications);
    }

    MosaicPropertyTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.mosaicPropertyTransactionBody = MosaicPropertyTransactionBody.loadFromBinary(inputStream);
    }

    public MosaicPropertyTransactionBody getMosaicPropertyTransactionBody() { return this.mosaicPropertyTransactionBody; }

    public static MosaicPropertyTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicPropertyTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.mosaicPropertyTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
