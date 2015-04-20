import java.math.BigInteger;

/**
 * Created by Peter on 19/04/15.
 */
public interface RSA {

    public BigInteger keyGen(int k);
    public void encrypt(String m);
    public void decrypt();

}
