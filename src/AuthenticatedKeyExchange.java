import java.util.Random;

public class AuthenticatedKeyExchange {
    // Random prime
    private int p = 3001;

    // Random number between 0 and p
    private int g = 793;

    private int nonce;

    public AuthenticatedKeyExchange(){
        nonce = generateNonce();
    }

    private int generateNonce() {
        Random rand = new Random(System.currentTimeMillis());
        return rand.nextInt(p-1);
    }

    public int calculate(){
        return (int) Math.pow(g,nonce) % p;
    }

    public int calculate(int value){
        return (int) Math.pow(value,nonce) % p;
    }


}
