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

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.lock.LockFundsInfo;
import io.nem.sdk.model.lock.SecretLockInfo;
import io.reactivex.Observable;

import java.util.List;

/**
 * Lock interface repository.
 *
 * @since 1.0
 */
public interface LockRepository {

    /**
     * Gets a LockFundsInfo for a given secret.
     *
     * @param hash String
     * @return Observable of {@link LockFundsInfo}
     */
    Observable<LockFundsInfo> getLockFunds(String hash);

    /**
     * Gets a SecretLockInfo for a given secret.
     *
     * @param secret String
     * @return Observable of List<{@link SecretLockInfo}>
     */
    Observable<SecretLockInfo> getSecretLock(String secret);

    /**
     * Gets array of LockFundsInfo for an account.
     * With pagination.
     *
     * @param address Address
     * @return Observable of List<{@link LockFundsInfo}>
     */
    Observable<List<LockFundsInfo>> getLockFundsInfoFromAccount(Address address);

    /**
     * Gets array of LockFundsInfo for an account.
     * With pagination.
     *
     * @param address Address
     * @param queryParams    QueryParams
     * @return Observable of List<{@link LockFundsInfo}>
     */
    Observable<List<LockFundsInfo>> getLockFundsInfoFromAccount(Address address, QueryParams queryParams);

    /**
     * Gets array of SecretLockInfo for an account.
     * With pagination.
     *
     * @param address Address
     * @return Observable of List<{@link SecretLockInfo}>
     */
    Observable<List<SecretLockInfo>> getSecretLocksInfoFromAccount(Address address);

    /**
     * Gets array of SecretLockInfo for an account.
     * With pagination.
     *
     * @param address Address
     * @param queryParams    QueryParams
     * @return Observable of List<{@link SecretLockInfo}>
     */
    Observable<List<SecretLockInfo>> getSecretLocksInfoFromAccount(Address address, QueryParams queryParams);
}