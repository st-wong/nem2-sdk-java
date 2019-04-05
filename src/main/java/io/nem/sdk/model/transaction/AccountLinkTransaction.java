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
import io.nem.sdk.model.blockchain.NetworkType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.Optional;

public class AccountLinkTransaction extends Transaction {
    private final AccountLinkTransactionBody accountLinkTransactionBody;

    AccountLinkTransaction(NetworkType networkType, Deadline deadline, long max_fee, PublicKey remotePublicKey, AccountLinkAction linkAction) {
        this(networkType, deadline, max_fee, remotePublicKey, linkAction, new VerifiableEntity(Optional.empty()), Optional.empty());
    }

    AccountLinkTransaction(NetworkType networkType, Deadline deadline, long max_fee, PublicKey remotePublicKey, AccountLinkAction linkAction, VerifiableEntity verifiableEntity, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(154), new EntityBody(networkType, (short)2, EntityType.ACCOUNT_LINK), deadline, max_fee, verifiableEntity, transactionInfo);
        this.accountLinkTransactionBody = new AccountLinkTransactionBody(remotePublicKey, linkAction);
    }
    AccountLinkTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.accountLinkTransactionBody = AccountLinkTransactionBody.loadFromBinary(inputStream);
    }

    public static AccountLinkTransaction create(Deadline deadline, PublicKey remotePublicKey, AccountLinkAction linkAction, NetworkType networkType) {
        return new AccountLinkTransaction(networkType, deadline, 0, remotePublicKey, linkAction);
    }

    public AccountLinkTransactionBody getAccountLinkTransactionBody() { return this.accountLinkTransactionBody; }

    public static AccountLinkTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new AccountLinkTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.accountLinkTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}