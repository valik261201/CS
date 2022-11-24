package lab4;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;


public class Runner {

    public static void main(String[] args) {

        ArrayList<String> database = new ArrayList<String>();

        // Take the user input message.

        @SuppressWarnings("resource")
        Scanner inn = new Scanner(System.in);
        String message = inn.nextLine();
        database.add(message);

        // Generate hash value for the message
        SHA256 sha256 = new SHA256();
        byte[] msgBytes = message.getBytes();
        byte[] hash1 = sha256.hash(msgBytes);
        String hashStr = DatatypeConverter.printHexBinary(hash1); //variable for printing
        System.out.println("Hash in String: " + hashStr);

        // Hash value together with a private key as input for RSA algorithm
        RSA rsa = new RSA();
        BigInteger[] pk = rsa.getPrivateKey();
        BigInteger[] puk = rsa.getPublicKey();
        System.out.println("Encrypting hash: " + hashStr);
        System.out.println("Hash in Bytes: "
                + rsa.toString(hashStr.getBytes()));
        //Encrypt/Generate digital signature
        byte[] encrypted = rsa.encrypt(pk[0],pk[1],hashStr.getBytes());
        System.out.println("Encrypted Hash: " + DatatypeConverter.printHexBinary(encrypted));


        //verification:
        String messageToVerify = message;

        // Calculating a hash value for the message to verify*/
        byte[] rMsgBytes = messageToVerify.getBytes();
        byte[] rHash = sha256.hash(rMsgBytes);
        String rHashStr = DatatypeConverter.printHexBinary(rHash);//variable for printing
        System.out.println("Hash in String: " + rHashStr);

        // The received encryption and public key as input for RSA decryption.
        byte[] decrypted = rsa.decrypt(puk[0],puk[1],encrypted);
        System.out.println("Alices' decrypted Hash in bytes: " + rsa.toString(decrypted));
        System.out.println("Alices' decrypted Hash in String: " + new String(decrypted));

        //4. Compare the RSA decryption with the calculated hash value for the received message
        if (new String(decrypted).equals(rHashStr)) {
            System.out.println("Verification successfull! Decryption matches calculated hash!");
        } else {
            System.out.println("Verification is not successful! Decryption does not match calculated hash!");
        }

    }

}