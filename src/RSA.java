import java.math.BigInteger;

public interface RSA {
    BigInteger encrypt(BigInteger m, BigInteger pk);

    BigInteger decrypt(BigInteger c);

    /**
     * Compares message with signed hash
     * @param s signed hash of message
     * @param m mesage to check against signature
     * @return returns true if the message matches the signature
     */
    boolean verify(BigInteger s, BigInteger m);
    /**
     * Compares message with signed hash in regard to the given public key
     * @param s signed hash of message
     * @param m mesage to check against signature
     * @param pk public key to verify with
     * @return returns true if the message matches the signature
     */
    boolean verify(BigInteger s, BigInteger m, BigInteger pk);

    BigInteger sign(BigInteger m);

    BigInteger getPublicKey();
}
