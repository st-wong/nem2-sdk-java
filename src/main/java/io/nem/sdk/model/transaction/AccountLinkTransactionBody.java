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

import io.nem.core.crypto.PublicKey;
import io.nem.sdk.model.account.AccountLinkAction;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

public class AccountLinkTransactionBody {
    private final PublicKey remotePublicKey;
    private final AccountLinkAction linkAction;

    AccountLinkTransactionBody(PublicKey remotePublicKey, AccountLinkAction linkAction) {
        Validate.notNull(remotePublicKey, "Remote Account must not be null");
        Validate.notNull(linkAction, "Account Link Action must not be null");
        this.remotePublicKey = remotePublicKey;
        this.linkAction = linkAction;
    }

    AccountLinkTransactionBody(final DataInput inputStream) throws Exception {
        ByteBuffer remoteAccountBuffer = ByteBuffer.allocate(32);
        inputStream.readFully(remoteAccountBuffer.array());
        this.remotePublicKey = PublicKey.fromHexString(new String(remoteAccountBuffer.array()).trim());
        this.linkAction = AccountLinkAction.loadFromBinary(inputStream);
    }

    public PublicKey getRemotePublicKey()  { return this.remotePublicKey; }

    public AccountLinkAction getLinkAction()  { return this.linkAction; }

    public static AccountLinkTransactionBody loadFromBinary(final DataInput inputStream) throws Exception { return new AccountLinkTransactionBody(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(this.remotePublicKey.getRaw(), 0, 32);
        stream.write(this.linkAction.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
