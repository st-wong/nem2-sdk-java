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
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.namespace.AliasAction;
import io.nem.sdk.model.namespace.NamespaceId;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;

public class MosaicAliasTransactionBody {
    private final AliasAction aliasAction;
    private final NamespaceId namespaceId;
    private final MosaicId mosaicId;

    MosaicAliasTransactionBody(AliasAction aliasAction, NamespaceId namespaceId, MosaicId mosaicId) {
        Validate.notNull(aliasAction, "Alias Action must not be null");
        Validate.notNull(namespaceId, "Namespace ID must not be null");
        Validate.notNull(mosaicId,    "Mosaic ID must not be null");
        this.mosaicId = mosaicId;
        this.namespaceId = namespaceId;
        this.aliasAction = aliasAction;
    }

    MosaicAliasTransactionBody(final DataInput inputStream) throws Exception {
        this.aliasAction = AliasAction.loadFromBinary(inputStream);
        this.namespaceId = new NamespaceId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
        this.mosaicId = new MosaicId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
    }

    public MosaicId getMosaicId()  { return this.mosaicId; }

    public NamespaceId getNamespaceId()  { return this.namespaceId; }

    public AliasAction getAliasAction()  { return this.aliasAction; }

    public static MosaicAliasTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new MosaicAliasTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(this.aliasAction.serialize());
        stream.write(ByteUtils.longToBytes(this.namespaceId.getId(), ByteOrder.LITTLE_ENDIAN));
        stream.write(ByteUtils.longToBytes(this.mosaicId.getId(), ByteOrder.LITTLE_ENDIAN));
        stream.close();
        return bos.toByteArray();
    }
}
