package io.nem.sdk.model.lock;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.transaction.HashType;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecretLockInfoTest {

    @Test
    void createASecretLockInfo() {
        PublicAccount publicAccount = PublicAccount.createFromPublicKey("b4f12e7c9f6946091e2cb8b6d3a12b50d17" +
                "ccbbf646386ea27ce2946a7423dcf", NetworkType.MIJIN_TEST);
        Mosaic mosaic = new Mosaic(new MosaicId("nem:xem"), BigInteger.valueOf(1));
        BigInteger expirationHeight = BigInteger.valueOf(0);
        LockStatus lockStatus = LockStatus.UNUSED;
        HashType hashType = HashType.SHA3_512;
        String secret = "b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf";
        Address recipient = Address.createFromRawAddress("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY");
        String metaId = "5ad9c23ca17289a20d88ca6a";

        SecretLockInfo secretLockInfo = new SecretLockInfo(publicAccount, mosaic, expirationHeight, lockStatus, hashType, secret, recipient, metaId);

        assertEquals(publicAccount, secretLockInfo.getAccount());
        assertEquals(mosaic, secretLockInfo.getMosaic());
        assertEquals(expirationHeight, secretLockInfo.getExpirationHeight());
        assertEquals(lockStatus, secretLockInfo.getStatus());
        assertEquals(hashType, secretLockInfo.getHashType());
        assertEquals(secret, secretLockInfo.getSecret());
        assertEquals(recipient, secretLockInfo.getRecipient());
        assertEquals(metaId, secretLockInfo.getMetaId());
    }
}