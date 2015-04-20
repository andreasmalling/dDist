import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Peter on 19/04/15.
 */

public class RSAImpl implements RSA {

    private BigInteger e = new BigInteger("3");
    private BigInteger p,q,n,d,c = null;
    private Random rnd = new Random();
    private BigInteger decryptedM = null;


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
    public void encrypt(String m) {
        if(n == null) return;
        BigInteger mAsInt = new BigInteger(m); // Convert message to a bytearray mByte
        System.out.println("m: " + mAsInt);
        c = mAsInt.modPow(e, n); // Create the ciphertext c = m^e mod n
    }

    @Override
    public void decrypt() {
        if(p == null || q == null) return;
        System.out.println("Ciphertext c : " + c);

        decryptedM = c.modPow(d,n); // decrypt message to bytearray

        System.out.println("decrypted message is " + decryptedM.toString());
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