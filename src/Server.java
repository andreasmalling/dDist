import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by amao on 5/11/15.
 */
public class Server implements Runnable{

        /*
         * Your group should use port number 40HGG, where H is your "hold nummer (1,2 or 3)
         * and GG is gruppe nummer 00, 01, 02, ... So, if you are in group 3 on hold 1 you
         * use the port number 40103. This will avoid the unfortunate situation that you
         * connect to each others servers.
         */
        protected int portNumber = 40499;
        protected ServerSocket serverSocket;

        private AuthenticatedKeyExchange ake = new AuthenticatedKeyExchange();
        private RSA rsa = new RSAImpl(16);

        /**
         *
         * Will print out the IP address of the local host and the port on which this
         * server is accepting connections.
         */
        protected void printLocalHostAddress() {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                String localhostAddress = localhost.getHostAddress();
                System.out.println("Contact this server on the IP address " + localhostAddress);
            } catch (UnknownHostException e) {
                System.err.println("Cannot resolve the Internet address of the local host.");
                System.err.println(e);
                System.exit(-1);
            }
        }

        /**
         *
         * Will register this server on the port number portNumber. Will not start waiting
         * for connections. For this you should call waitForConnectionFromClient().
         */
        protected void registerOnPort() {
            try {
                serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                serverSocket = null;
                System.err.println("Cannot open server socket on port number" + portNumber);
                System.err.println(e);
                System.exit(-1);
            }
        }

        public void deregisterOnPort() {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                    serverSocket = null;
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }

        /**
         *
         * Waits for the next client to connect on port number portNumber or takes the
         * next one in line in case a client is already trying to connect. Returns the
         * socket of the connection, null if there were any failures.
         */
        protected Socket waitForConnectionFromClient() {
            Socket res = null;
            try {
                res = serverSocket.accept();
            } catch (IOException e) {
                // We return null on IOExceptions
            }
            return res;
        }

        public void run() {
            System.out.println("Hello world!");

            printLocalHostAddress();

            registerOnPort();

            ObjectInputStream fromClient = null;
            ObjectOutputStream toClient = null;
            Socket socket = waitForConnectionFromClient();

            if (socket != null) {
                System.out.println("Connection from client " + socket);

                try {
                    fromClient = new ObjectInputStream(socket.getInputStream());
                    toClient = new ObjectOutputStream(socket.getOutputStream());

                    // Exchange Public-Keys
                    BigInteger clientPK = (BigInteger) fromClient.readObject();
                    toClient.writeObject(rsa.getPublicKey());

                    // Get calculation from client
                    int clientCalc = fromClient.readInt();

                    // Send calculation
                    int serverCalc = ake.calculate();
                    toClient.writeInt(serverCalc);

                    // Validate received client-msg
                    BigInteger signedServerCalc = (BigInteger) fromClient.readObject();
                    if (rsa.encrypt(signedServerCalc, clientPK).intValue() != serverCalc)
                        return;

                    // Signed and send received msg
                    toClient.writeObject(rsa.decrypt(new BigInteger(clientCalc + "")));

                    // Generate common-key
                    int key = ake.calculate(clientCalc);

                    socket.close();
                } catch (Exception e) {
                    // We report but otherwise ignore IOExceptions
                    System.err.println(e);
                }
                System.out.println("Connection closed by client.");
            }

            deregisterOnPort();

            System.out.println("Goodbuy world!");
        }
}
