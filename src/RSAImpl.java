import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Peter on 19/04/15.
 */

public class RSAImpl implements RSA {

    private BigInteger e = new BigInteger("3");
    private BigInteger p,q,n,d = null;
    private Random rnd = new Random();



    @Override
    public BigInteger keyGen(int k) {
        int bitLength = k;
        do {
            p = BigInteger.probablePrime(bitLength, rnd);
            q = BigInteger.probablePrime(bitLength, rnd);
            System.out.println("(Re)doing the while with p: " + p.toString() + " and q:" + q.toString());
        } while (!correctPrimesTest(p, q));

        BigInteger pTemp = p.subtract(BigInteger.ONE);
        BigInteger qTemp = q.subtract(BigInteger.ONE);
        BigInteger pTimesq = pTemp.multiply(qTemp);

        d = e.modInverse(pTimesq);

        n = p.multiply(q);

        return n;
    }

    @Override
    public BigInteger encrypt(BigInteger m) {
        if(n == null) return null;

        BigInteger c = m.modPow(e, n); // Create the ciphertext c = m^e mod n

        return c;
    }

    @Override
    public BigInteger decrypt(BigInteger c) {
        if(p == null || q == null) return null;
        System.out.println("Ciphertext c : " + c);
        BigInteger m = c.modPow(d,n); // decrypt message to big int
        System.out.println("decrypted message is " + m.toString());

        return m;
    }


    @Override
    public BigInteger generate(BigInteger m){

        BigInteger c = hashBigInteger(m);

        System.out.println("m hashed: " + c);

        return decrypt(c);

    }

    @Override
    public boolean verify(BigInteger c, BigInteger m) {

        m = hashBigInteger(m);
        c = encrypt(c);

        System.out.println("c: " + c);
        System.out.println("m: " + m);

        if(m == null || c == null) return false;

        if(m.equals(c)) return true;

        return false;

    }

    private BigInteger hashBigInteger(BigInteger message) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(message.toByteArray());

            return new BigInteger(md.digest());

        } catch (NoSuchAlgorithmException e){
            System.err.println(e);
        }
        return null;

    }



    private boolean correctPrimesTest(BigInteger p, BigInteger q){
        return areNotEqual(p, q) && notGcdTest(p,q);
    }

    private boolean areNotEqual(BigInteger p, BigInteger q){
        return !p.equals(q);
    }

    private boolean notGcdTest(BigInteger p, BigInteger q){
        return (e.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE) && e.gcd(q.subtract(BigInteger.ONE)).equals(BigInteger.ONE));
    }
}
