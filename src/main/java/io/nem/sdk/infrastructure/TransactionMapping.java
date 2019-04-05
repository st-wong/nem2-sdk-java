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

package io.nem.sdk.infrastructure;

import io.nem.core.crypto.PublicKey;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicProperties;
import io.nem.sdk.model.mosaic.MosaicSupplyChangeDirection;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.namespace.NamespaceType;
import io.nem.sdk.model.transaction.*;
import io.reactivex.functions.Function;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapping implements Function<JsonObject, Transaction> {
    @Override
    public Transaction apply(JsonObject input) {
        JsonObject transaction = input.getJsonObject("transaction");
        int type = transaction.getInteger("type");

        if (type == EntityType.TRANSFER.getValue()) {
            return new TransferTransactionMapping().apply(input);
        } else if (type == EntityType.REGISTER_NAMESPACE.getValue()) {
            return new NamespaceCreationTransactionMapping().apply(input);
//        } else if (type == EntityType.MOSAIC_DEFINITION.getValue()) {
//            return new MosaicCreationTransactionMapping().apply(input);
        } else if (type == EntityType.MOSAIC_SUPPLY_CHANGE.getValue()) {
            return new MosaicSupplyChangeTransactionMapping().apply(input);
        } else if (type == EntityType.MODIFY_MULTISIG_ACCOUNT.getValue()) {
            return new MultisigModificationTransactionMapping().apply(input);
        } else if (type == EntityType.AGGREGATE_COMPLETE.getValue() || type == EntityType.AGGREGATE_BONDED.getValue()) {
            return new AggregateTransactionMapping().apply(input);
        } else if (type == EntityType.LOCK.getValue()) {
            return new HashLockTransactionMapping().apply(input);
        } else if (type == EntityType.SECRET_LOCK.getValue()) {
            return new SecretLockTransactionMapping().apply(input);
        } else if (type == EntityType.SECRET_PROOF.getValue()) {
            return new SecretProofTransactionMapping().apply(input);
        }

        throw new UnsupportedOperationException("Unimplemented Transaction type");
    }

    BigInteger extractBigInteger(JsonArray input) {
        UInt64DTO uInt64DTO = new UInt64DTO();
        input.stream().forEach(item -> uInt64DTO.add(new Long(item.toString())));
        return uInt64DTO.extractIntArray();
    }

    Integer extractTransactionVersion(int version) {
        return (int) Long.parseLong(Integer.toHexString(version).substring(2, 4), 16);
    }

    NetworkType extractNetworkType(int version) {
        int networkType = (int) Long.parseLong(Integer.toHexString(version).substring(0, 2), 16);
        return NetworkType.rawValueOf(networkType);
    }

    public TransactionInfo createTransactionInfo(JsonObject jsonObject) {
        if (jsonObject.containsKey("hash") && jsonObject.containsKey("id")) {
            return TransactionInfo.create(extractBigInteger(jsonObject.getJsonArray("height")),
                    jsonObject.getInteger("index"),
                    jsonObject.getString("id"),
                    jsonObject.getString("hash"),
                    jsonObject.getString("merkleComponentHash"));
        } else if (jsonObject.containsKey("aggregateHash") && jsonObject.containsKey("id")) {
            return TransactionInfo.createAggregate(extractBigInteger(jsonObject.getJsonArray("height")),
                    jsonObject.getInteger("index"),
                    jsonObject.getString("id"),
                    jsonObject.getString("aggregateHash"),
                    jsonObject.getString("aggregateId"));
        } else {
            return TransactionInfo.create(extractBigInteger(jsonObject.getJsonArray("height")),
                    jsonObject.getString("hash"),
                    jsonObject.getString("merkleComponentHash"));
        }
    }
}

class TransferTransactionMapping extends TransactionMapping {

    @Override
    public TransferTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());
        List<Mosaic> mosaics = new ArrayList<>();

        if (transaction.getJsonArray("mosaics") != null) {
            mosaics = transaction
                    .getJsonArray("mosaics")
                    .stream()
                    .map(item -> (JsonObject) item)
                    .map(mosaic -> new Mosaic(
                            extractBigInteger(mosaic.getJsonArray("id")).longValue(),
                            extractBigInteger(mosaic.getJsonArray("amount")).longValue()))
                    .collect(Collectors.toList());
        }

        Message message = PlainMessage.Empty;
        if (transaction.getJsonObject("message") != null) {
            message = new PlainMessage(new String(Hex.decode(transaction.getJsonObject("message").getString("payload")), StandardCharsets.UTF_8));
        }

        return new TransferTransaction(
                extractNetworkType(transaction.getInteger("version")),
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                Address.createFromEncoded(transaction.getString("recipient")),
                mosaics,
                message,
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), extractNetworkType(transaction.getInteger("version"))),
                transactionInfo
        );
    }
}

class NamespaceCreationTransactionMapping extends TransactionMapping {

    @Override
    public RegisterNamespaceTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());
        NamespaceType namespaceType = NamespaceType.rawValueOf(transaction.getInteger("namespaceType").byteValue());

        return new RegisterNamespaceTransaction(
                extractNetworkType(transaction.getInteger("version")),
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                namespaceType,
                new NamespaceId(extractBigInteger(transaction.getJsonArray("namespaceId")).longValue()),
                transaction.getString("name"),
                namespaceType == NamespaceType.ROOT ? Optional.of(new BlockDuration(extractBigInteger(transaction.getJsonArray("duration")).longValue())) : Optional.empty(),
                namespaceType == NamespaceType.CHILD ? Optional.of(new NamespaceId(extractBigInteger(transaction.getJsonArray("parentId")).longValue())) : Optional.empty(),
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), extractNetworkType(transaction.getInteger("version"))),
                transactionInfo
        );
    }
}
/*
class MosaicCreationTransactionMapping extends TransactionMapping {

    @Override
    public MosaicDefinitionTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));


        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());

        JsonArray mosaicProperties = transaction.getJsonArray("properties");

        String flags = "00" + Integer.toBinaryString(extractBigInteger(mosaicProperties.getJsonObject(0).getJsonArray("value")).intValue());
        String bitMapFlags = flags.substring(flags.length() - 3);
        MosaicProperties properties = new MosaicProperties(bitMapFlags.charAt(2) == '1',
                bitMapFlags.charAt(1) == '1',
                bitMapFlags.charAt(0) == '1',
                extractBigInteger(mosaicProperties.getJsonObject(1).getJsonArray("value")).intValue(),
                mosaicProperties.size() == 3 ? extractBigInteger(mosaicProperties.getJsonObject(2).getJsonArray("value")) : BigInteger.valueOf(0));

        return new MosaicDefinitionTransaction(
                extractNetworkType(transaction.getInteger("version")),
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                transaction.getString("name"),
                new NamespaceId(extractBigInteger(transaction.getJsonArray("parentId")).longValue()),
                new MosaicId(extractBigInteger(transaction.getJsonArray("mosaicId")).longValue()),
                properties,
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), extractNetworkType(transaction.getInteger("version"))),
                transactionInfo
        );
    }
}
*/
class MosaicSupplyChangeTransactionMapping extends TransactionMapping {

    @Override
    public MosaicSupplyChangeTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());

        return new MosaicSupplyChangeTransaction(
                extractNetworkType(transaction.getInteger("version")),
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                new MosaicId(extractBigInteger(transaction.getJsonArray("mosaicId")).longValue()),
                MosaicSupplyChangeDirection.rawValueOf(transaction.getInteger("direction").byteValue()),
                extractBigInteger(transaction.getJsonArray("delta")).longValue(),
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), extractNetworkType(transaction.getInteger("version"))),
                transactionInfo
        );
    }
}

class MultisigModificationTransactionMapping extends TransactionMapping {

    @Override
    public ModifyMultisigAccountTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());
        NetworkType networkType = extractNetworkType(transaction.getInteger("version"));

        List<CosignatoryModification> modifications = transaction.containsKey("modifications") ? transaction
                .getJsonArray("modifications")
                .stream()
                .map(item -> (JsonObject) item)
                .map(coSignatoryModification -> new CosignatoryModification(
                        CosignatoryModificationType.rawValueOf(coSignatoryModification.getInteger("type").byteValue()),
                        PublicKey.fromHexString(coSignatoryModification.getString("cosignatoryPublicKey"))))
                .collect(Collectors.toList()) : Collections.emptyList();

        return new ModifyMultisigAccountTransaction(
                networkType,
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                transaction.getInteger("minApprovalDelta").byteValue(),
                transaction.getInteger("minRemovalDelta").byteValue(),
                modifications,
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), networkType),
                transactionInfo
        );
    }
}

class AggregateTransactionMapping extends TransactionMapping {

    @Override
    public AggregateTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());
        NetworkType networkType = extractNetworkType(transaction.getInteger("version"));

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < transaction.getJsonArray("transactions").getList().size(); i++) {
            JsonObject innerTransaction = transaction.getJsonArray("transactions").getJsonObject(i);
            innerTransaction.getJsonObject("transaction").put("deadline", transaction.getJsonArray("deadline"));
            innerTransaction.getJsonObject("transaction").put("fee", transaction.getJsonArray("fee"));
            innerTransaction.getJsonObject("transaction").put("signature", transaction.getString("signature"));
            if (!innerTransaction.containsKey("meta")) {
                innerTransaction.put("meta", input.getJsonObject("meta"));
            }
            transactions.add(new TransactionMapping().apply(innerTransaction));
        }

        List<AggregateTransactionCosignature> cosignatures = new ArrayList<>();
        if (transaction.getJsonArray("cosignatures") != null) {
            cosignatures = transaction
                    .getJsonArray("cosignatures")
                    .stream()
                    .map(item -> (JsonObject) item)
                    .map(aggregateCosignature -> new AggregateTransactionCosignature(
                            aggregateCosignature.getString("signature"),
                            new PublicAccount(aggregateCosignature.getString("signer"), networkType)))
                    .collect(Collectors.toList());
        }

        return new AggregateTransaction(
                networkType,
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                transactions,
                cosignatures,
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), networkType),
                transactionInfo
        );
    }
}

class HashLockTransactionMapping extends TransactionMapping {

    @Override
    public HashLockTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());
        NetworkType networkType = extractNetworkType(transaction.getInteger("version"));
        Mosaic mosaic;
        if (transaction.containsKey("mosaicId")) {
            mosaic = new Mosaic(extractBigInteger(transaction.getJsonArray("mosaicId")).longValue(), extractBigInteger(transaction.getJsonArray("amount")).longValue());
        } else {
            mosaic = new Mosaic(extractBigInteger(transaction.getJsonObject("mosaic").getJsonArray("id")).longValue(), extractBigInteger(transaction.getJsonObject("mosaic").getJsonArray("amount")).longValue());
        }
        return new HashLockTransaction(
                networkType,
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                mosaic,
                new BlockDuration(extractBigInteger(transaction.getJsonArray("duration")).longValue()),
                transaction.getString("hash"),
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), networkType),
                transactionInfo
        );
    }
}

class SecretLockTransactionMapping extends TransactionMapping {

    @Override
    public SecretLockTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());
        NetworkType networkType = extractNetworkType(transaction.getInteger("version"));
        Mosaic mosaic;
        if (transaction.containsKey("mosaicId")) {
            mosaic = new Mosaic(extractBigInteger(transaction.getJsonArray("mosaicId")).longValue(), extractBigInteger(transaction.getJsonArray("amount")).longValue());
        } else {
            mosaic = new Mosaic(extractBigInteger(transaction.getJsonObject("mosaic").getJsonArray("id")).longValue(), extractBigInteger(transaction.getJsonObject("mosaic").getJsonArray("amount")).longValue());
        }
        return new SecretLockTransaction(
                networkType,
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                mosaic,
                new BlockDuration(extractBigInteger(transaction.getJsonArray("duration")).longValue()),
                LockHashAlgorithm.rawValueOf(transaction.getInteger("hashAlgorithm").byteValue()),
                transaction.getString("secret"),
                Address.createFromEncoded(transaction.getString("recipient")),
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), networkType),
                transactionInfo
        );
    }
}

class SecretProofTransactionMapping extends TransactionMapping {

    @Override
    public SecretProofTransaction apply(JsonObject input) {
        TransactionInfo transactionInfo = this.createTransactionInfo(input.getJsonObject("meta"));

        JsonObject transaction = input.getJsonObject("transaction");
        Deadline deadline = new Deadline(extractBigInteger(transaction.getJsonArray("deadline")).longValue());
        NetworkType networkType = extractNetworkType(transaction.getInteger("version"));

        return new SecretProofTransaction(
                networkType,
                deadline,
                extractBigInteger(transaction.getJsonArray("fee")).longValue(),
                LockHashAlgorithm.rawValueOf(transaction.getInteger("hashAlgorithm").byteValue()),
                transaction.getString("secret"),
                transaction.getString("proof"),
                transaction.getString("signature"),
                new PublicAccount(transaction.getString("signer"), networkType),
                transactionInfo
        );
    }
}

