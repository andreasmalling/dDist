/**
 * Created by Peter on 19/04/15.
 */
public class Main {

    public static void main(String[] args){
        RSAImpl rsa = new RSAImpl();

        int m = 123456;

        System.out.println(rsa.keyGen(16).toString());
        System.out.println("Number to en/decrypt is " + m);
        rsa.encrypt(m + "");
        rsa.decrypt();
    }

}
