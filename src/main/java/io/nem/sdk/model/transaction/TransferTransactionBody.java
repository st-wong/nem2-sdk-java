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
import io.nem.sdk.model.mosaic.Mosaic;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * The transfer transaction body object containing necessary data for transfer transactions and embedded transfer transactions.
 *
 * @since 1.0
 */
public class TransferTransactionBody {
    private final Address recipient;
    private final Message message;
    private final List<Mosaic> mosaics;

    /**
     * Constructor
     *
     * @param recipient of the transaction.
     * @param mosaics list of the transaction.
     * @param message for the transaction.
     */
    TransferTransactionBody(Address recipient, List<Mosaic> mosaics, Message message) {
        Validate.notNull(recipient, "Recipient must not be null");
        Validate.notNull(mosaics, "Mosaics must not be null");
        Validate.notNull(message, "Message must not be null");
        this.recipient = recipient;
        this.mosaics = mosaics;
        this.message = message;
    }

    /**
     * Constructor
     *
     * @param inputStream of the binary layout.
     * @throws Exception when there are issues in reading the data input.
     */
    TransferTransactionBody(final DataInput inputStream) throws Exception {
        ByteBuffer recipientBuffer = ByteBuffer.allocate(25);
        inputStream.readFully(recipientBuffer.array());
        this.recipient = Address.createFromEncoded(new String(recipientBuffer.array()));
        short messageType = ByteUtils.streamToShort(inputStream, ByteOrder.LITTLE_ENDIAN);
        short mosaicsCount = ByteUtils.streamToShort(inputStream, ByteOrder.LITTLE_ENDIAN);
        ByteBuffer messageBuffer = ByteBuffer.allocate(messageType - 1);
        inputStream.readFully(messageBuffer.array());
        this.message = new PlainMessage(new String(messageBuffer.array()));
        this.mosaics = new ArrayList<>(mosaicsCount);
        for (int i = 0; i < mosaicsCount; i++) {
            mosaics.add(Mosaic.loadFromBinary(inputStream));
        }
    }

    /**
     * Returns address of the recipient.
     *
     * @return recipient address
     */
    public Address getRecipient()  {
        return this.recipient;
    }

    /**
     * Returns transaction message.
     *
     * @return Message
     */
    public Message getMessage()  {
        return this.message;
    }

    /**
     * Returns list of mosaic objects.
     *
     * @return Link<{ @ link   Mosaic }>
     */
    public List<Mosaic> getMosaics()  {
        return this.mosaics;
    }

    /**
     * Load an instance of Transfer Transaction Body object from a binary layout.
     *
     * @param inputStream of the binary layout.
     * @return an instance of Transfer Transaction Body object from the binary layout.
     * @throws Exception when there are issues in reading the data input.
     */
    public static TransferTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new TransferTransactionBody(inputStream); }

    /**
     * Serialize the Transfer Transaction Body object to a binary layout.
     *
     * @return an array of bytes representing the binary layout of the Transfer Transaction Body object.
     * @throws Exception when there are issues in writing the binary layout.
     */
    public byte[] serialize() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
        stream.write(Base32Encoder.getBytes(this.recipient.plain()));
        stream.write(ByteUtils.shortToBytes((short)(this.message.getType() + 1), ByteOrder.LITTLE_ENDIAN));     //TODO: +1 due to Message Type not implemented
        stream.write(ByteUtils.shortToBytes((short)this.mosaics.size(), ByteOrder.LITTLE_ENDIAN));
        stream.write(this.message.getPayload().getBytes());
        for (int i = 0; i < this.mosaics.size(); i++) {
            byte[] mosaic = this.mosaics.get(i).serialize();
            stream.write(mosaic);
        }
        stream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
