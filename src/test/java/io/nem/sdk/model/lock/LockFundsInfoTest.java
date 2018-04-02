package io.nem.sdk.model.lock;

import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LockFundsInfoTest {

    @Test
    void createALockFundsInfo() {
        PublicAccount publicAccount = PublicAccount.createFromPublicKey("b4f12e7c9f6946091e2cb8b6d3a12b50d17" +
                "ccbbf646386ea27ce2946a7423dcf", NetworkType.MIJIN_TEST);
        Mosaic mosaic = new Mosaic(new MosaicId("nem:xem"), BigInteger.valueOf(1));
        BigInteger expirationHeight = BigInteger.valueOf(0);
        LockStatus lockStatus = LockStatus.USED;
        String hash = "b4f12e7c9f6946091e2cb8b6d3a12b50d17ccbbf646386ea27ce2946a7423dcf";

        LockFundsInfo lockFundsInfo = new LockFundsInfo(publicAccount, mosaic, expirationHeight, lockStatus, hash);

        assertEquals(publicAccount, lockFundsInfo.getAccount());
        assertEquals(mosaic, lockFundsInfo.getMosaic());
        assertEquals(expirationHeight, lockFundsInfo.getExpirationHeight());
        assertEquals(lockStatus, lockFundsInfo.getStatus());
        assertEquals(hash, lockFundsInfo.getHash());
    }
}
