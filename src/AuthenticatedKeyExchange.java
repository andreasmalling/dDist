import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Random;

/**
 * Diffie-Hellman Key Exchange
 */

public class AuthenticatedKeyExchange {
    // Random prime
    public final BigInteger p = new BigInteger("3001");

    // Random number between 0 and p-1
    public final BigInteger g = new BigInteger("793");

    private int nonce;
    private RSA rsa;

    public AuthenticatedKeyExchange(RSA rsa){
        generateNonce();
        this.rsa = rsa;
    }

    private void generateNonce() {
        Random rand = new Random(System.currentTimeMillis()); // Yes , it's not 'that' random TODO: SecureRandom?
        nonce = rand.nextInt( p.intValue() - 1);
    }

    /**
     * Value calculated from secret random-choosen nonce, to be passed in public.
     * Exchange of these can be used to get common secret value.
     * @return public value to seed {@link #getCommonValue(BigInteger) getCommonValue}
     */
    public BigInteger getPublicValue(){
        BigInteger pow = g.pow(nonce);
        BigInteger publicValue = pow.mod(p);
        return publicValue;
    }

    public BigInteger getCommonValue(BigInteger publicValue){
        BigInteger pow = publicValue.pow(nonce);
        BigInteger commonValue = pow.mod(p);
        return commonValue;
    }



    public BigInteger handshake(Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream to = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream from = new ObjectInputStream(socket.getInputStream());

        // Exchange Public-Keys instead of certificates
        BigInteger myPK = rsa.getPublicKey();
        to.writeObject(myPK);
        BigInteger receivedPK = (BigInteger) from.readObject();

        // Send public value
        BigInteger myPV = getPublicValue();
        to.writeObject(myPV);

        // Get public value
        BigInteger receivedPV = (BigInteger) from.readObject();

        // Sign and send received public value
        BigInteger mySignedPV = rsa.sign(new BigInteger(receivedPV + ""));
        to.writeObject(mySignedPV);

        // Verify public value, and generate common secret value
        BigInteger signedPV = (BigInteger) from.readObject();
        if (rsa.verify(signedPV, new BigInteger(myPV + ""), receivedPK))
            return getCommonValue(receivedPV);

        return null;     // Verification failed TODO: Throw exception?
    }
}