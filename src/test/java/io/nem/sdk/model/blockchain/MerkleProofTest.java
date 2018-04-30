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

package io.nem.sdk.model.blockchain;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MerkleProofTest {

    @Test
    void createANewValidMerkleProof() {
        String transactionHash = "8d25b2639a7d12feaaaef34358b215e97533f9ffdda5b9fadfd8ecc229695263";
        BigInteger blockHeight = BigInteger.valueOf(10000);
        String merkleRoot = "16d28dd7af87b86c79273450470e96f22c66f8ef4598064603699b69f10464d0";
        List<String> hashes = Arrays.asList(
                "8ab2f19a47c5b30cc389ae1580f0472b4d3afeea83cdf0f707d03ed76b15a00c",
                "aa1dfff01b3aba492188195df4d77af25ff9d57df4ea0fd6fe498d572b6e67fd"
        );

        MerkleProof merkleProof = new MerkleProof(transactionHash, blockHeight, merkleRoot, hashes);

        assertEquals(transactionHash, merkleProof.getTransactionHash());
        assertEquals(blockHeight, merkleProof.getBlockHeight());
        assertEquals(merkleRoot, merkleProof.getMerkleRootHash());
        assertEquals(hashes, merkleProof.getHashes());
        assertTrue(merkleProof.isValid());

    }

    @Test
    void createANewInvalidMerkleProof() {
        String transactionHash = "8d25b2639a7d12feaaaef34358b215e97533f9ffdda5b9fadfd8ecc229695263";
        BigInteger blockHeight = BigInteger.valueOf(10000);
        String merkleRoot = "16d28dd7af87b86c79273450470e96f22c66f8ef4598064603699b69f10464d0";
        List<String> hashes = Arrays.asList(
                "8ab2f19a47c5b30cc389ae1580f0472b4d3afeea83cdf0f707d03ed76b15a00c",
                "b9840c4eadb6724a2dfca81d5e90ef3f4ee91beb63a58fa91a4f05e951f08fcf"
        );

        MerkleProof merkleProof = new MerkleProof(transactionHash, blockHeight, merkleRoot, hashes);

        assertEquals(transactionHash, merkleProof.getTransactionHash());
        assertEquals(blockHeight, merkleProof.getBlockHeight());
        assertEquals(merkleRoot, merkleProof.getMerkleRootHash());
        assertEquals(hashes, merkleProof.getHashes());
        assertFalse(!merkleProof.isValid()); // TODO: change when implement method auditMerkle method in MerkleProof model

    }
}
