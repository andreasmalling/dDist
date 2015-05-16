import java.net.*;
import java.io.*;

public class Server implements Runnable{

        protected int portNumber = 40499;
        protected ServerSocket serverSocket;

        private AuthenticatedKeyExchange ake = null;
        private RSA rsa = null;

        public Server(RSA rsa){
            this.rsa = rsa;
            ake = new AuthenticatedKeyExchange(rsa);
        }


        /**
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
            registerOnPort();

            Socket socket = waitForConnectionFromClient();

            if (socket != null) {
                System.out.println("Connection from client");

                try {
                    System.out.println("Server Key is " + ake.handshake(socket));
                    socket.close();
                } catch (IOException e) {
                    System.err.println(e);
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                }
            }

            deregisterOnPort();

            System.out.println("Goodbuy world!");
        }
}
