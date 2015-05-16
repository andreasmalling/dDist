import java.net.*;
import java.io.*;

public class Client implements Runnable {
    protected int portNumber = 40499;

    private AuthenticatedKeyExchange ake = null;
    private RSA rsa = null;

    public Client(RSA rsa){
        this.rsa = rsa;
        ake = new AuthenticatedKeyExchange(rsa);
    }

    /**
     *
     * Connects to the server on IP address serverName and port number portNumber.
     */
    protected Socket connectToServer(String serverName) {
        Socket res = null;
        try {
            res = new Socket(serverName,portNumber);
        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }

    public void run() {
        String serverName = "127.0.0.1";

        Socket socket = connectToServer(serverName);

        if (socket != null) {
            System.out.println("Connected to server");
            try {
                System.out.println("Client Key is " + ake.handshake(socket));
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            } catch (ClassNotFoundException e) {
                System.err.println(e);
            }
        }

        System.out.println("Goodbuy world!");
    }

}