import java.math.BigInteger;

/**
 * Created by Peter on 19/04/15.
 */
public interface RSA {
    BigInteger encrypt(BigInteger m, BigInteger pk);

    BigInteger decrypt(BigInteger c);

    boolean verify(BigInteger c, BigInteger m);
    BigInteger sign(BigInteger m);

    BigInteger getPublicKey();
}
