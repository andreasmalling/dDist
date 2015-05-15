import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class RSAImpl implements RSA {

    private int k = 2000; // Default bitlength og keys

    private BigInteger e = new BigInteger("3");
    private BigInteger p,q,n,d = null;

    private Random rnd = new Random(System.currentTimeMillis()); // Yes , it's not 'that' random

    public RSAImpl(){
        generateKeys(k);
    }

    public RSAImpl(int bitLength){
        k = bitLength;
        generateKeys(k);
    }

    private void generateKeys(int k) {
        // Start work indicator
        ProgressBarRotating pb = new ProgressBarRotating("Locating primes");
        pb.start();

        do {
            p = BigInteger.probablePrime(k, rnd);
            q = BigInteger.probablePrime(k, rnd);
        } while (!correctPrimesTest(p, q));

        // Stop work indicator
        pb.showProgress = false;

        // (q-1)*(p-1)
        BigInteger pTemp = p.subtract(BigInteger.ONE);
        BigInteger qTemp = q.subtract(BigInteger.ONE);
        BigInteger pTimesq = pTemp.multiply(qTemp);

        d = e.modInverse(pTimesq);

        n = p.multiply(q);
    }

    public BigInteger encrypt(BigInteger m) {
        BigInteger c = m.modPow(e, n); // Create the ciphertext c = m^e mod n

        return c;
    }

    @Override
    public BigInteger encrypt(BigInteger m, BigInteger pk) {
        BigInteger c = m.modPow(e, pk); // Create the ciphertext c = m^e mod n

        return c;
    }

    @Override
    public BigInteger decrypt(BigInteger c) {
        BigInteger m = c.modPow(d,n); // decrypt message to big int

        return m;
    }

    /**
     * Sign hash of message
     * @param m message to sign
     * @return signed hash of message
     */
    @Override
    public BigInteger sign(BigInteger m){
        m = hashBigInteger(m);
        return decrypt(m);
    }

    /**
     * Compares message with signed hash
     * @param c signed hash of message
     * @param m mesage to check against signature
     * @return returns true if the message matches the signature
     */
    @Override
    public boolean verify(BigInteger c, BigInteger m) {
        m = hashBigInteger(m);
        c = encrypt(c);

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
        if(p.equals(q))
            return false;

        if(!e.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE))
            return false;

        if(!e.gcd(q.subtract(BigInteger.ONE)).equals(BigInteger.ONE))
            return false;

        return true;
    }

    public BigInteger getPublicKey() {
        return n;
    }
}

class ProgressBarRotating extends Thread {
    boolean showProgress = true;
    String message = "Processing";

    public ProgressBarRotating(){}
    public ProgressBarRotating(String msg){
        message = msg;
    }

    public void run() {
        String anim= "|/-\\";
        int x = 0;
        while (showProgress) {
            System.out.print("\r " + message + " " + anim.charAt(x++ % anim.length()));
            try { Thread.sleep(100); }
            catch (Exception e) {};
        }

    }
}