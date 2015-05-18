

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static BigInteger m = new BigInteger("123456");
    private static BigInteger _m = new BigInteger("123457");

    private static RSAImpl rsa = null;

    public static void main(String[] args) throws IOException {

        if(args.length > 0) {
            for(int i = 0; i < args.length; i++) {
                int exercise = Integer.parseInt(args[i]);

                switch (exercise) {
                    case 1: confidentiality();
                        break;
                    case 2: authentication();
                        break;
                    case 3: authenticatedKeyExchange();
                        break;
                    default:
                        System.out.println("No such programming exercise as " + exercise);
                }
            }
        } else {
            authenticatedKeyExchange();
        }
    }

    public static void confidentiality() {
        initRSA();

        System.out.println("\n=== Confidentiality ===");

        System.out.println("M is " + m);
        BigInteger c = rsa.encrypt(m);
        System.out.println("E(M) is " + c);
        System.out.println("D(E(M)) is " + rsa.decrypt(c));

        System.out.println("=======================");
    }

    public static void authentication() {
        initRSA();

        System.out.println("\n=== Authentication ===");

        BigInteger s = rsa.sign(m);
        System.out.println("M is " + m);
        System.out.println("S(M) is " + s);
        System.out.println("V(S(M), M) is " + rsa.verify(s, m));
        System.out.println("\nM' is " + _m);
        System.out.println("V(S(M), M') is " + rsa.verify(s, _m));

        /* Average */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Number of runs for testing average:");
        int runs = Integer.parseInt(scanner.next());

        double msRun = runGenerationNTimes(runs, m);
        System.out.println("Average time for signature generation: " + msRun + "ms");
        System.out.println("Bits per second: " + (2000 / (msRun / 1000)));

        System.out.println("======================");
    }

    public static void authenticatedKeyExchange() {
        System.out.println("\n=== Authenticated Key Exchange ===");

        String selection = null;
        int bitLength = 2000;

        ArrayList <String> s = new ArrayList<>();
        s.add("server");
        s.add("s");

        ArrayList <String> c = new ArrayList<>();
        c.add("client");
        c.add("c");

        Scanner input = new Scanner(System.in);
        System.out.println("Select role [client/server/both]");

        selection = input.next();

        RSA r1 = new RSAImpl(bitLength);
        System.out.println();

        if (s.contains(selection)) {
            System.out.println("Server selected");
            new Thread(new Server(r1)).start();
        } else if (c.contains(selection)) {
            System.out.println("Client selected");
            new Thread(new Client(r1)).start();
        } else {
            System.out.println("Both selected");
            RSA r2 = new RSAImpl(bitLength);
            new Thread(new Server(r1)).start();
            new Thread(new Client(r2)).start();
        }
    }

    private static void initRSA() {
        System.out.println("\n--- Initializing keys ---");

        if(rsa != null) {
            System.out.println("Keys already initialized");
        } else {
            rsa = new RSAImpl();
            System.out.println("\nDone");
        }
        System.out.println("-------------------------");
    }


    private static long runGenerationNTimes(int n, BigInteger message){
        long totalElapsedTime = 0;

        RSA rsa = new RSAImpl(2000);

        for (int i = 0; i < n; i++){
            long startTime = System.currentTimeMillis();
            BigInteger signature = rsa.sign(message);
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            totalElapsedTime += elapsedTime;
        }

        return totalElapsedTime / n;

    }
}
