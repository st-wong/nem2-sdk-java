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
import io.nem.sdk.model.mosaic.MosaicFlags;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicNonce;
import io.nem.sdk.model.mosaic.MosaicProperty;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Optional;

public class MosaicDefinitionTransaction extends Transaction {
    private final MosaicDefinitionTransactionBody mosaicDefinitionTransactionBody;

    MosaicDefinitionTransaction(NetworkType networkType, Deadline deadline, long max_fee, MosaicId mosaicId, MosaicFlags mosaicFlags, byte divisibility, MosaicNonce mosaicNonce, ArrayList<MosaicProperty> mosaicProperties) {
        this(networkType, deadline, max_fee, mosaicId, mosaicFlags, divisibility, mosaicNonce, mosaicProperties, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public MosaicDefinitionTransaction(NetworkType networkType, Deadline deadline, long max_fee, MosaicId mosaicId, MosaicFlags mosaicFlags, byte divisibility, MosaicNonce mosaicNonce, ArrayList<MosaicProperty> mosaicProperties, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, mosaicId, mosaicFlags, divisibility, mosaicNonce, mosaicProperties, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    MosaicDefinitionTransaction(NetworkType networkType, Deadline deadline, long max_fee, MosaicId mosaicId, MosaicFlags mosaicFlags, byte divisibility, MosaicNonce mosaicNonce, ArrayList<MosaicProperty> mosaicProperties, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(144), new EntityBody(networkType, (short)2, EntityType.MOSAIC_DEFINITION, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.mosaicDefinitionTransactionBody = new MosaicDefinitionTransactionBody(mosaicId, mosaicFlags, divisibility, mosaicNonce, mosaicProperties);
    }
    MosaicDefinitionTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.mosaicDefinitionTransactionBody = MosaicDefinitionTransactionBody.loadFromBinary(inputStream);
    }

    public MosaicDefinitionTransactionBody getMosaicDefinitionTransactionBody() { return this.mosaicDefinitionTransactionBody; }

    public static MosaicDefinitionTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicDefinitionTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.mosaicDefinitionTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
