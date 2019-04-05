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
import io.nem.sdk.model.mosaic.MosaicFlags;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicNonce;
import io.nem.sdk.model.mosaic.MosaicProperty;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Optional;

public class EmbeddedMosaicDefinitionTransaction extends EmbeddedTransaction {
    private MosaicDefinitionTransactionBody mosaicDefinitionTransactionBody;

    EmbeddedMosaicDefinitionTransaction(NetworkType networkType, MosaicId mosaicId, MosaicFlags mosaicFlags, byte divisibility, MosaicNonce mosaicNonce, ArrayList<MosaicProperty> mosaicProperties) {
        this(networkType, mosaicId, mosaicFlags, divisibility, mosaicNonce, mosaicProperties, Optional.empty());
    }

    EmbeddedMosaicDefinitionTransaction(NetworkType networkType, MosaicId mosaicId, MosaicFlags mosaicFlags, byte divisibility, MosaicNonce mosaicNonce, ArrayList<MosaicProperty> mosaicProperties, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(144), new EntityBody(networkType, (short)2, EntityType.MOSAIC_DEFINITION), transactionInfo);
        this.mosaicDefinitionTransactionBody = new MosaicDefinitionTransactionBody(mosaicId, mosaicFlags, divisibility, mosaicNonce, mosaicProperties);
    }

    EmbeddedMosaicDefinitionTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.mosaicDefinitionTransactionBody = MosaicDefinitionTransactionBody.loadFromBinary(inputStream);
    }

    public static EmbeddedMosaicDefinitionTransaction create(MosaicId mosaicId, MosaicFlags mosaicFlags, byte divisibility, MosaicNonce mosaicNonce, ArrayList<MosaicProperty> mosaicProperties, NetworkType networkType) {
        return new EmbeddedMosaicDefinitionTransaction(networkType, mosaicId, mosaicFlags, divisibility, mosaicNonce, mosaicProperties);
    }

    public MosaicDefinitionTransactionBody getMosaicDefinitionTransactionBody() { return this.mosaicDefinitionTransactionBody; }

    public static EmbeddedMosaicDefinitionTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new EmbeddedMosaicDefinitionTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(super.serialize());
        stream.write(this.mosaicDefinitionTransactionBody.serialize());
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
