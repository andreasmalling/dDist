

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by Peter on 19/04/15.
 *
 */

public class Main  {

    private static RSAImpl rsa = new RSAImpl();

    public static void main(String[] args) throws IOException {

        BigInteger m = new BigInteger("123456");

        System.out.println("M is " + m);

        BigInteger c = rsa.encrypt(m);

        System.out.println("E(M) is " + c );

        System.out.println("D(E(M)) is " + rsa.decrypt(c));

        /*Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Client client = new Client();
        Thread clientThread = new Thread(client);
        clientThread.start();*/

    }

}
