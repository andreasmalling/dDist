

import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * Created by Peter on 19/04/15.
 *
 */

public class Main  {

    private static BigInteger m = new BigInteger("123456");
    private static BigInteger _m = new BigInteger("123457");

    private static RSAImpl rsa = null;

    public static void main(String[] args) throws IOException {

        String selection = "n";
        Scanner input = new Scanner(System.in);

        /* CONFIDENTIALITY */
        System.out.println("Run Exercise 1, confidentiality? [y/n]");
        selection = input.next();
        if(selection.equals("y")||selection.equals("Y"))
            confidentiality();

        /* AUTHENTICATION */
        System.out.println("\nRun Exercise 2, authentication? [y/n]");
        selection = input.next();
        if(selection.equals("y")||selection.equals("Y"))
            authentication();

        /* AUTHENTICATED KEY EXCHANGE */
        System.out.println("\nRun Exercise 3, authenticated key exchange? [y/n]");
        selection = input.next();
        if(selection.equals("y")||selection.equals("Y"))
            authenticatedKeyExchange();
    }

    public static void confidentiality() {
        initRSA();

        System.out.println("\n=== Confidentiality ===");

        System.out.println("M is " + m);
        BigInteger c = rsa.encrypt(m);
        System.out.println("E(M) is " + c );
        System.out.println("D(E(M)) is " + rsa.decrypt(c));

        System.out.println("=======================");
    }

    public static void authentication() {
        initRSA();

        System.out.println("\n=== Authentication ===");

        BigInteger s = rsa.sign(m);
        System.out.println("M is " + m);
        System.out.println("S(M) is " + s);
        System.out.println("V(S(M), M) is " + rsa.verify(s,m));
        System.out.println("\nM' is " + _m);
        System.out.println("V(S(M), M') is " + rsa.verify(s, _m));

        System.out.println("======================");
    }

    public static void authenticatedKeyExchange() {
        System.out.println("\n=== Authenticated Key Exchange ===");

        System.out.println();
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Client client = new Client();
        Thread clientThread = new Thread(client);
        clientThread.start();
    }

    private static void initRSA() {
        System.out.println("\n--- Initializing keys ---");

        if(rsa != null) {
            System.out.println("Keys already initialized");
            System.out.println("-------------------------");
            return;
        }
        rsa = new RSAImpl();

        System.out.println("\nDone!");
        System.out.println("-------------------------");
    }
}
