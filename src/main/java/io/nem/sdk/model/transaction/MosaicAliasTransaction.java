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
import io.nem.sdk.model.namespace.AliasAction;
import io.nem.sdk.model.namespace.NamespaceId;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class MosaicAliasTransaction extends Transaction {
    private final MosaicAliasTransactionBody mosaicAliasTransactionBody;

    MosaicAliasTransaction(NetworkType networkType, Deadline deadline, long max_fee, AliasAction aliasAction, NamespaceId namespaceId, MosaicId mosaicId) {
        this(networkType, deadline, max_fee, aliasAction, namespaceId, mosaicId, new VerifiableEntity(Optional.empty()), Optional.empty());
    }

    MosaicAliasTransaction(NetworkType networkType, Deadline deadline, long max_fee, AliasAction aliasAction, NamespaceId namespaceId, MosaicId mosaicId, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(137), new EntityBody(networkType, (short)1, EntityType.MOSAIC_ALIAS), deadline, max_fee, verifiableEntity, transactionInfo);
        this.mosaicAliasTransactionBody = new MosaicAliasTransactionBody(aliasAction, namespaceId, mosaicId);
    }

    MosaicAliasTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.mosaicAliasTransactionBody = MosaicAliasTransactionBody.loadFromBinary(inputStream);
    }

    public MosaicAliasTransactionBody getMosaicAliasTransactionBody() { return this.mosaicAliasTransactionBody; }

    public static MosaicAliasTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicAliasTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.mosaicAliasTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
