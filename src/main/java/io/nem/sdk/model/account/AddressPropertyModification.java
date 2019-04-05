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

package io.nem.sdk.model.account;

import io.nem.core.utils.Base32Encoder;
import io.nem.sdk.model.transaction.PropertyModification;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

public class AddressPropertyModification {
    AddressPropertyModification(PropertyModification propertyModification, Address address) {
        this.propertyModification = propertyModification;
        this.address = address;
    }

    AddressPropertyModification(final DataInput inputStream) throws Exception {
        this.propertyModification = PropertyModification.loadFromBinary(inputStream);
        ByteBuffer addressBuffer = ByteBuffer.allocate(25);
        inputStream.readFully(addressBuffer.array());
        this.address = Address.createFromEncoded(new String(addressBuffer.array()));
    }

    public PropertyModification getModificationType()  {
        return this.propertyModification;
    }

    public Address getAddress()  {
        return this.address;
    }

    public static AddressPropertyModification loadFromBinary(final DataInput inputStream) throws Exception { return new AddressPropertyModification(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.propertyModification.serialize());
        stream.write(Base32Encoder.getBytes(this.address.plain()));
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private final PropertyModification propertyModification;
    private final Address address;
}
