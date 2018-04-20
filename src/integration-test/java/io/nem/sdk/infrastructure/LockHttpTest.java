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
        String hash = "9253B3165DF4D4FC0CE831F104BFCFDDAF50A6E36D1BAD35F234AFA0F32B29D6";
        LockFundsInfo lockFundsInfo = lockHttp
                .getLockFunds(hash)
                .toFuture()
                .get();

        assertEquals(hash, lockFundsInfo.getHash());
    }

    @Test
    void getSecretLock() throws ExecutionException, InterruptedException {
        String secret = "05C489CAA2D96764AE684B489DF83994F3331D4E964580A1B17943E60131CB895E06" +
            "24987B646C99FA3823F0E49273401E138E3D100FA424FF15F57183467D1D";
        SecretLockInfo secretLockInfo = lockHttp
                .getSecretLock(secret)
                .toFuture()
                .get();

        assertEquals(secret, secretLockInfo.getSecret());
    }

    @Test
    void getLockFundsInfoFromAccount() throws ExecutionException, InterruptedException {
        Address address = Address.createFromRawAddress("SCTVW23D2MN5VE4AQ4TZIDZENGNOZXPRPRLIKCF2");
        List<LockFundsInfo> lockFundsInfo = lockHttp
                .getLockFundsInfoFromAccount(address)
                .toFuture()
                .get();

        assertEquals(address, lockFundsInfo.get(0).getAccount().getAddress());

    }

    @Test
    void getSecretLocksInfoFromAccount() throws ExecutionException, InterruptedException {
        Address address = Address.createFromRawAddress("SCTVW23D2MN5VE4AQ4TZIDZENGNOZXPRPRLIKCF2");
        List<SecretLockInfo> secretLocksInfo = lockHttp
                .getSecretLocksInfoFromAccount(address)
                .toFuture()
                .get();

        assertEquals(address, secretLocksInfo.get(0).getAccount().getAddress());
    }
}
