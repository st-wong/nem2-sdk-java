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

import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Optional;

public class ModifyMultisigAccountTransaction extends Transaction {
    private final ModifyMultisigAccountTransactionBody modifyMultisigAccountTransactionBody;

    ModifyMultisigAccountTransaction(NetworkType networkType, Deadline deadline, long max_fee, byte minRemovalDelta, byte minApprovalDelta, List<CosignatoryModification> cosignatoryModifications) {
        this(networkType, deadline, max_fee, minRemovalDelta, minApprovalDelta, cosignatoryModifications, new VerifiableEntity(Optional.empty()), Optional.empty(), Optional.empty());
    }

    public ModifyMultisigAccountTransaction(NetworkType networkType, Deadline deadline, long max_fee, byte minRemovalDelta, byte minApprovalDelta, List<CosignatoryModification> cosignatoryModifications, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, deadline, max_fee, minRemovalDelta, minApprovalDelta, cosignatoryModifications, new VerifiableEntity(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    ModifyMultisigAccountTransaction(NetworkType networkType, Deadline deadline, long max_fee, byte minRemovalDelta, byte minApprovalDelta, List<CosignatoryModification> cosignatoryModifications, VerifiableEntity verifiableEntity, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(new SizePrefixedEntity(123 * (33 * cosignatoryModifications.size())), new EntityBody(networkType, (short)3, EntityType.MODIFY_MULTISIG_ACCOUNT, signer), deadline, max_fee, verifiableEntity, transactionInfo);
        this.modifyMultisigAccountTransactionBody = new ModifyMultisigAccountTransactionBody(minRemovalDelta, minApprovalDelta, cosignatoryModifications);
    }

    ModifyMultisigAccountTransaction(final DataInput inputStream) throws Exception {
        super(inputStream);
        this.modifyMultisigAccountTransactionBody = ModifyMultisigAccountTransactionBody.loadFromBinary(inputStream);
    }

    public ModifyMultisigAccountTransactionBody getModifyMultisigAccountTransactionBody() { return this.modifyMultisigAccountTransactionBody; }

    public static ModifyMultisigAccountTransaction loadFromBinary(final DataInput inputStream) throws Exception { return new ModifyMultisigAccountTransaction(inputStream); }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.write(super.serialize());
        stream.write(this.modifyMultisigAccountTransactionBody.serialize());
        stream.close();
        return bos.toByteArray();
    }
}
