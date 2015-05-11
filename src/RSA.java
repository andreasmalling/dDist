import java.math.BigInteger;

/**
 * Created by Peter on 19/04/15.
 */
public interface RSA {

    public BigInteger keyGen(int k);
    public BigInteger encrypt(BigInteger m);

    BigInteger encrypt(BigInteger m, BigInteger n);

    public BigInteger decrypt(BigInteger c);
    public BigInteger generate(BigInteger m);
    public boolean verify(BigInteger c, BigInteger m);
    public BigInteger getPublicKey();
}
