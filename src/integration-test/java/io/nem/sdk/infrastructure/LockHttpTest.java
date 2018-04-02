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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LockHttpTest extends BaseTest {
    private LockHttp lockHttp;

    @BeforeAll
    void setup() throws IOException {
        lockHttp = new LockHttp(this.getNodeUrl());
    }

    @Test
    void getLockFunds() throws ExecutionException, InterruptedException {
        String hash = "ADF6E45D8C21BC3B835A2C3ABAC90800A270601F0B1361BC46C26E00968105E6";
        LockFundsInfo lockFundsInfo = lockHttp
                .getLockFunds(hash)
                .toFuture()
                .get();

        assertEquals(hash, lockFundsInfo.getHash());
    }

    @Test
    void getSecretLock() throws ExecutionException, InterruptedException {
        String secret = "E70D37DA074D5F2CEF6BCE7E3E06D3D7E42E5A653EDDE49EDEB0628C295883CE6685494F" +
                "E64B835762BB2D5959AE48F87501E7DB3B826B4C1BA9D3BA70BC5DC5";
        SecretLockInfo secretLockInfo = lockHttp
                .getSecretLock(secret)
                .toFuture()
                .get();

        assertEquals(secret, secretLockInfo.getSecret());
    }

    @Test
    void getLockFundsInfoFromAccount() throws ExecutionException, InterruptedException {
        Address address = Address.createFromRawAddress("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY");
        List<LockFundsInfo> lockFundsInfo = lockHttp
                .getLockFundsInfoFromAccount(address)
                .toFuture()
                .get();

        assertEquals(address, lockFundsInfo.get(0).getAccount().getAddress());

    }

    @Test
    void getSecretLocksInfoFromAccount() throws ExecutionException, InterruptedException {
        Address address = Address.createFromRawAddress("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY");
        List<SecretLockInfo> secretLocksInfo = lockHttp
                .getSecretLocksInfoFromAccount(address)
                .toFuture()
                .get();

        assertEquals(address, secretLocksInfo.get(0).getAccount().getAddress());
    }
}
