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

import com.fasterxml.jackson.core.type.TypeReference;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.lock.LockFundsInfo;
import io.nem.sdk.model.lock.LockStatus;
import io.nem.sdk.model.lock.SecretLockInfo;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.transaction.HashType;
import io.reactivex.Observable;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public class LockHttp extends Http implements LockRepository {

    public LockHttp(String host) throws MalformedURLException {
        this(host, new NetworkHttp(host));
        System.out.println(host);

    }

    public LockHttp(String host, NetworkHttp networkHttp) throws MalformedURLException {
        super(host, networkHttp);
    }

    @Override
    public Observable<LockFundsInfo> getLockFunds(String hash) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/lock/hash/" + hash)
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(HttpResponse::body)
                        .map(json -> objectMapper.readValue(json.toString(), LockFundsInfoDTO.class))
                        .map(lockFundsInfoDTO -> new LockFundsInfo(
                                PublicAccount.createFromPublicKey(lockFundsInfoDTO.getLock().getAccount(), networkType),
                                new Mosaic(new MosaicId(lockFundsInfoDTO.getLock().getMosaicId().extractIntArray()),
                                        lockFundsInfoDTO.getLock().getAmount().extractIntArray()),
                                lockFundsInfoDTO.getLock().getHeight().extractIntArray(),
                                lockFundsInfoDTO.getLock().getStatus() == 0 ? LockStatus.UNUSED : LockStatus.USED,
                                lockFundsInfoDTO.getLock().getHash(),
                                lockFundsInfoDTO.getMeta().getId()
                        )));
    }

    @Override
    public Observable<SecretLockInfo> getSecretLock(String secret) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/lock/secret/" + secret)
                        .as(BodyCodec.jsonObject())
                        .rxSend()
                        .toObservable()
                        .map(HttpResponse::body)
                        .map(json -> objectMapper.readValue(json.toString(), SecretLockInfoDTO.class))
                        .map(secretLockInfoDTO -> new SecretLockInfo(
                                PublicAccount.createFromPublicKey(secretLockInfoDTO.getLock().getAccount(), networkType),
                                new Mosaic(new MosaicId(secretLockInfoDTO.getLock().getMosaicId().extractIntArray()),
                                        secretLockInfoDTO.getLock().getAmount().extractIntArray()),
                                secretLockInfoDTO.getLock().getHeight().extractIntArray(),
                                secretLockInfoDTO.getLock().getStatus() == 0 ? LockStatus.UNUSED : LockStatus.USED,
                                HashType.rawValueOf(secretLockInfoDTO.getLock().getHashAlgorithm()),
                                secretLockInfoDTO.getLock().getSecret(),
                                Address.createFromEncoded(secretLockInfoDTO.getLock().getRecipient()),
                                secretLockInfoDTO.getMeta().getId()
                        )));
    }

    @Override
    public Observable<List<LockFundsInfo>> getLockFundsInfoFromAccount(Address address) {
        return this.getLockFundsInfoFromAccount(address, Optional.empty());
    }

    @Override
    public Observable<List<LockFundsInfo>> getLockFundsInfoFromAccount(Address address, QueryParams queryParams) {
        return this.getLockFundsInfoFromAccount(address, Optional.of(queryParams));
    }

    private Observable<List<LockFundsInfo>> getLockFundsInfoFromAccount(Address address, Optional<QueryParams> queryParams) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/account/" + address.plain() + "/lock/hash" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                        .as(BodyCodec.jsonArray())
                        .rxSend()
                        .toObservable()
                        .map(HttpResponse::body)
                        .map(json -> objectMapper.<List<LockFundsInfoDTO>>readValue(json.toString(), new TypeReference<List<LockFundsInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(lockFundsInfoDTO -> new LockFundsInfo(
                                PublicAccount.createFromPublicKey(lockFundsInfoDTO.getLock().getAccount(), networkType),
                                new Mosaic(new MosaicId(lockFundsInfoDTO.getLock().getMosaicId().extractIntArray()),
                                        lockFundsInfoDTO.getLock().getAmount().extractIntArray()),
                                lockFundsInfoDTO.getLock().getHeight().extractIntArray(),
                                lockFundsInfoDTO.getLock().getStatus() == 0 ? LockStatus.UNUSED : LockStatus.USED,
                                lockFundsInfoDTO.getLock().getHash(),
                                lockFundsInfoDTO.getMeta().getId()
                        ))
                        .toList()
                        .toObservable());
    }

    @Override
    public Observable<List<SecretLockInfo>> getSecretLocksInfoFromAccount(Address address) {
        return this.getSecretLocksInfoFromAccount(address, Optional.empty());
    }

    @Override
    public Observable<List<SecretLockInfo>> getSecretLocksInfoFromAccount(Address address, QueryParams queryParams) {
        return this.getSecretLocksInfoFromAccount(address, Optional.of(queryParams));
    }

    private Observable<List<SecretLockInfo>> getSecretLocksInfoFromAccount(Address address, Optional<QueryParams> queryParams) {
        Observable<NetworkType> networkTypeResolve = getNetworkTypeObservable();
        return networkTypeResolve
                .flatMap(networkType -> this.client
                        .getAbs(this.url + "/account/" + address.plain() + "/lock/secret" + (queryParams.isPresent() ? queryParams.get().toUrl() : ""))
                        .as(BodyCodec.jsonArray())
                        .rxSend()
                        .toObservable()
                        .map(HttpResponse::body)
                        .map(json -> objectMapper.<List<SecretLockInfoDTO>>readValue(json.toString(), new TypeReference<List<SecretLockInfoDTO>>() {
                        }))
                        .flatMapIterable(item -> item)
                        .map(secretLockInfoDTO -> new SecretLockInfo(
                                PublicAccount.createFromPublicKey(secretLockInfoDTO.getLock().getAccount(), networkType),
                                new Mosaic(new MosaicId(secretLockInfoDTO.getLock().getMosaicId().extractIntArray()),
                                        secretLockInfoDTO.getLock().getAmount().extractIntArray()),
                                secretLockInfoDTO.getLock().getHeight().extractIntArray(),
                                secretLockInfoDTO.getLock().getStatus() == 0 ? LockStatus.UNUSED : LockStatus.USED,
                                HashType.rawValueOf(secretLockInfoDTO.getLock().getHashAlgorithm()),
                                secretLockInfoDTO.getLock().getSecret(),
                                Address.createFromEncoded(secretLockInfoDTO.getLock().getRecipient()),
                                secretLockInfoDTO.getMeta().getId()
                        ))
                        .toList()
                        .toObservable());
    }
}
