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
import io.nem.sdk.model.account.AddressPropertyModification;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class AddressPropertyTransactionBody {
    private final PropertyType propertyType;
    private final ArrayList<AddressPropertyModification> addressPropertyModifications;

    AddressPropertyTransactionBody(PropertyType propertyType, ArrayList<AddressPropertyModification> addressPropertyModifications) {
        Validate.notNull(propertyType, "Property Type must not be null");
        Validate.notNull(addressPropertyModifications, "List of Address Property Modification must not be null");
        this.propertyType = propertyType;
        this.addressPropertyModifications = addressPropertyModifications;
    }

    AddressPropertyTransactionBody(final DataInput inputStream) throws Exception {
        this.propertyType = PropertyType.loadFromBinary(inputStream);
        short addressPropertyModificationsCount = ByteUtils.streamToByte(inputStream, ByteOrder.LITTLE_ENDIAN);
        this.addressPropertyModifications = new ArrayList<>(addressPropertyModificationsCount);
        for (int i = 0; i < addressPropertyModificationsCount; i++) {
            addressPropertyModifications.add(AddressPropertyModification.loadFromBinary(inputStream));
        }
    }

    public PropertyType getPropertyType()  { return this.propertyType; }

    public ArrayList<AddressPropertyModification> getTransactionTypePropertyModifications()  { return this.addressPropertyModifications; }

    public static AddressPropertyTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new AddressPropertyTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(this.propertyType.serialize());
        stream.write(ByteUtils.byteToBytes((byte)this.addressPropertyModifications.size(), ByteOrder.LITTLE_ENDIAN));
        for (int i = 0; i < this.addressPropertyModifications.size(); i++) {
            byte[] addressPropertyModification = this.addressPropertyModifications.get(i).serialize();
            stream.write(addressPropertyModification, 0, addressPropertyModification.length);
        }
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
