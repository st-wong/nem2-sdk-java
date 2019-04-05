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

package io.nem.core.utils;

import java.io.DataInput;
import java.nio.*;

public class ByteUtils {

    /**
     * Converts a stream of data into a long with specific byte order.
     *
     * @param stream The data input stream.
     * @param byteOrder The byte order.
     * @return The long.
     */
    public static long streamToLong(final DataInput stream, ByteOrder byteOrder) throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        stream.readFully(buffer.array());
        return buffer.order(byteOrder).getLong();
    }

    /**
     * Converts an array of 8 bytes into a long with specific byte order.
     *
     * @param bytes The bytes.
     * @param byteOrder The byte order.
     * @return The long.
     */
    public static long bytesToLong(final byte[] bytes, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(byteOrder).put(bytes, 0, 8);
        buffer.flip();
        return buffer.getLong();
    }

    /**
     * Converts a long value into an array of 8 bytes with specific byte order.
     *
     * @param x The long.
     * @param byteOrder The byte order.
     * @return The bytes.
     */
    public static byte[] longToBytes(final long x, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(byteOrder);
        LongBuffer longBuffer = buffer.asLongBuffer();
        longBuffer.put(x);
        return buffer.array();
    }

    /**
     * Converts a stream of data into an int with specific byte order.
     *
     * @param stream The data input stream.
     * @param byteOrder The byte order.
     * @return The int.
     */
    public static int streamToInt(final DataInput stream, ByteOrder byteOrder) throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        stream.readFully(buffer.array());
        return buffer.order(byteOrder).getInt();
    }

    /**
     * Converts an array of 4 bytes into a int with specific byte order.
     *
     * @param bytes The bytes.
     * @param byteOrder The byte order.
     * @return The int.
     */
    public static int bytesToInt(final byte[] bytes, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(byteOrder).put(bytes, 0, 4);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * Converts an int value into an array of 4 bytes with specific byte order.
     *
     * @param x The int.
     * @param byteOrder The byte order.
     * @return The bytes.
     */
    public static byte[] intToBytes(final int x, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(byteOrder);
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(x);
        return buffer.array();
    }

    /**
     * Converts an int array into an array of bytes with specific byte order.
     *
     * @param data The int array.
     * @param byteOrder The byte order.
     * @return The bytes.
     */
    public static byte[] intArrayToBytes(final int[] data, ByteOrder byteOrder) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 4); // 4 is 4 bytes for int
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(data);
        return byteBuffer.array();
    }

    /**
     * Converts a stream of data into a short with specific byte order.
     *
     * @param stream The data input stream.
     * @param byteOrder The byte order.
     * @return The short.
     */
    public static short streamToShort(final DataInput stream, ByteOrder byteOrder) throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(2);
        stream.readFully(buffer.array());
        return buffer.order(byteOrder).getShort();
    }

    /**
     * Converts an array of 2 bytes into a short with specific byte order.
     *
     * @param bytes The bytes.
     * @param byteOrder The byte order.
     * @return The short.
     */
    public static short bytesToShort(final byte[] bytes, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(byteOrder).put(bytes, 0, 2);
        buffer.flip();
        return buffer.getShort();
    }

    /**
     * Converts a short value into an array of 2 bytes with specific byte order.
     *
     * @param x The short.
     * @param byteOrder The byte order.
     * @return The bytes.
     */
    public static byte[] shortToBytes(final short x, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(byteOrder);
        ShortBuffer shortBuffer = buffer.asShortBuffer();
        shortBuffer.put(x);
        return buffer.array();
    }

    /**
     * Converts a stream of data into a byte with specific byte order.
     *
     * @param stream The data input stream.
     * @param byteOrder The byte order.
     * @return The byte.
     */
    public static byte streamToByte(final DataInput stream, ByteOrder byteOrder) throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        stream.readFully(buffer.array());
        return buffer.order(byteOrder).get();
    }

    /**
     * Converts an array of 1 bytes into a byte with specific byte order.
     *
     * @param bytes The bytes.
     * @param byteOrder The byte order.
     * @return The byte.
     */
    public static byte bytesToByte(final byte[] bytes, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(byteOrder).put(bytes);
        buffer.flip();
        return buffer.get();
    }

    /**
     * Converts a byte value into an array of 2 bytes with specific byte order.
     *
     * @param x The byte.
     * @param byteOrder The byte order.
     * @return The bytes.
     */
    public static byte[] byteToBytes(final byte x, ByteOrder byteOrder) {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(byteOrder).put(x);
        return buffer.array();
    }

    /**
     * Constant-time byte comparison. The constant time behavior eliminates side channel attacks.
     *
     * @param b One byte.
     * @param c Another byte.
     * @return 1 if b and c are equal, 0 otherwise.
     */
    public static int isEqualConstantTime(final int b, final int c) {
        int result = 0;
        final int xor = b ^ c;
        for (int i = 0; i < 8; i++) {
            result |= xor >> i;
        }

        return (result ^ 0x01) & 0x01;
    }

    /**
     * Constant-time check if byte is negative. The constant time behavior eliminates side channel attacks.
     *
     * @param b The byte to check.
     * @return 1 if the byte is negative, 0 otherwise.
     */
    public static int isNegativeConstantTime(final int b) {
        return (b >> 8) & 1;
    }

    /**
     * Creates a human readable representation of an array of bytes.
     *
     * @param bytes The bytes.
     * @return An string representation of the bytes.
     */
    public static String toString(final byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append("{ ");
        for (final byte b : bytes) {
            builder.append(String.format("%02X ", (byte) (0xFF & b)));
        }

        builder.append("}");
        return builder.toString();
    }
}
