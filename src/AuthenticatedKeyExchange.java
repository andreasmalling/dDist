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
    public final int p = 3001;

    // Random number between 0 and p-1
    public final int g = 793;

    private int nonce;
    private RSA rsa;

    public AuthenticatedKeyExchange(RSA rsa){
        nonce = generateNonce();
        this.rsa = rsa;
    }

    private int generateNonce() {
        Random rand = new Random(System.currentTimeMillis()); // Yes , it's not 'that' random TODO: SecureRandom?
        return rand.nextInt(p-1);
    }

    /**
     * Value calculated from secret random-choosen nonce, to be passed in public.
     * Exchange of these can be used to get common secret value.
     * @return public value to seed {@link #getCommonValue(int) getCommonValue}
     */
    public int getPublicValue(){
        return (int) Math.pow(g,nonce) % p;
    }

    public int getCommonValue(int publicValue){
        return (int) Math.pow(publicValue,nonce) % p;
    }

    public int handshake(Socket socket) throws IOException, ClassNotFoundException {

        ObjectOutputStream to = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream from = new ObjectInputStream(socket.getInputStream());

        // Exchange Public-Keys instead of certificates
        to.writeObject(rsa.getPublicKey());
        BigInteger receivedPK = (BigInteger) from.readObject();

        // Send public value
        to.writeInt(getPublicValue());

        // Get public value
        int receivedPV = from.readInt();

        // Sign and send received public value
        to.writeObject(rsa.sign(new BigInteger(receivedPV + "")) );

        // Verify public value, and generate common secret value
        BigInteger signedPV = (BigInteger) from.readObject();
        if (rsa.verify(signedPV, new BigInteger(getPublicValue() + ""), receivedPK))
            return getCommonValue(receivedPV);;

        // Verification failed TODO: Throw exception?
        return 0;
    }
}