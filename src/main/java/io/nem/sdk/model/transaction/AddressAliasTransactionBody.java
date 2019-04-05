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

import io.nem.core.utils.Base32Encoder;
import io.nem.core.utils.ByteUtils;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.namespace.AliasAction;
import io.nem.sdk.model.namespace.NamespaceId;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AddressAliasTransactionBody {
    private final AliasAction aliasAction;
    private final NamespaceId namespaceId;
    private final Address address;

    AddressAliasTransactionBody(Address address, NamespaceId namespaceId, AliasAction aliasAction) {
        Validate.notNull(address, "Address must not be null");
        Validate.notNull(namespaceId, "Namespace ID must not be null");
        Validate.notNull(aliasAction, "Alias Action must not be null");
        this.address = address;
        this.namespaceId = namespaceId;
        this.aliasAction = aliasAction;
    }

    AddressAliasTransactionBody(final DataInput inputStream) throws Exception {
        this.aliasAction = AliasAction.loadFromBinary(inputStream);
        this.namespaceId = new NamespaceId(ByteUtils.streamToLong(inputStream, ByteOrder.LITTLE_ENDIAN));
        ByteBuffer addressBuffer = ByteBuffer.allocate(25);
        inputStream.readFully(addressBuffer.array());
        this.address = Address.createFromEncoded(new String(addressBuffer.array()));
    }

    public Address getAddress()  { return this.address; }

    public NamespaceId getNamespaceId()  { return this.namespaceId; }

    public AliasAction getAliasAction()  { return this.aliasAction; }

    public static AddressAliasTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new AddressAliasTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(this.aliasAction.serialize());
        stream.write(ByteUtils.longToBytes(this.namespaceId.getId(), ByteOrder.LITTLE_ENDIAN));
        stream.write(Base32Encoder.getBytes(this.address.plain()));
        stream.close();
        return bos.toByteArray();
    }
}
