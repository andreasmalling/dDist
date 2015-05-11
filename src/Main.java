

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by Peter on 19/04/15.
 *
 */

public class Main  {

    private static RSAImpl rsa = new RSAImpl();

    public static void main(String[] args) throws IOException {


        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Client client = new Client();
        Thread clientThread = new Thread(client);
        clientThread.start();

    }

}
