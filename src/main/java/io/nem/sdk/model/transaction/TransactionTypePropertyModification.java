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

import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;

public class TransactionTypePropertyModification {
    private final PropertyModificationType propertyModificationType;
    private final EntityType entityType;

    TransactionTypePropertyModification(PropertyModificationType propertyModificationType, EntityType entityType) {
        Validate.notNull(propertyModificationType, "Property Modification Type must not be null");
        Validate.notNull(entityType, "Entity Type must not be null");
        this.propertyModificationType = propertyModificationType;
        this.entityType = entityType;
    }

    TransactionTypePropertyModification(final DataInput inputStream) throws Exception {
        this.propertyModificationType = PropertyModificationType.loadFromBinary(inputStream);
        this.entityType = EntityType.loadFromBinary(inputStream);
    }

    public PropertyModificationType getPropertyModificationType()  { return this.propertyModificationType; }

    public EntityType getEntityType()  { return this.entityType; }

    public static TransactionTypePropertyModification loadFromBinary(final DataInput inputStream) throws Exception { return new TransactionTypePropertyModification(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(this.propertyModificationType.serialize());
        stream.write(this.entityType.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
