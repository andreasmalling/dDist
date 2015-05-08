

import java.math.BigInteger;

/**
 * Created by Peter on 19/04/15.
 */

public class Main {

    private static RSAImpl rsa = new RSAImpl();

    public static void main(String[] args){



        int m = 123456;

        System.out.println(rsa.keyGen(16).toString());
        System.out.println("Number to en/decrypt is " + m);
        BigInteger c = rsa.encrypt(new BigInteger(m + ""));
        rsa.decrypt(c);

        System.out.println("\n" +
                "\n" +
                "\nVerification");

        BigInteger m2 = new BigInteger(5768191 + "");

        rsa.keyGen(256);

        long startTime = System.currentTimeMillis();

        BigInteger signature = rsa.generate(m2);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime-startTime;

        System.out.println("Elapsed time for signature generation: " + elapsedTime + "ms");

        System.out.println("Message: " + m2);
        System.out.println("Signature: " + signature);

        boolean isAccepted = rsa.verify(signature,m2);

        System.out.println("Result of verification: " + isAccepted);


        if(args.length > 0) {
            System.out.println("Testing for average with n = " + args[0]);
            double msRun = runGenerationNTimes(Integer.parseInt(args[0]), m2);
            System.out.println("Average time for signature generation: " + msRun + "ms");
            System.out.println("Bits per second: " + (2000 / (msRun / 1000)));
        }


    }

    private static long runGenerationNTimes(int n, BigInteger message){
        long totalElapsedTime = 0;

        rsa.keyGen(2000);

        for (int i = 0; i < n; i++){
            long startTime = System.currentTimeMillis();
            BigInteger signature = rsa.generate(message);
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            totalElapsedTime += elapsedTime;
        }

        return totalElapsedTime / n;

    }

}
