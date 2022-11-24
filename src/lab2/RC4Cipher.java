package lab2;

import java.util.Scanner;

public class RC4Cipher {
    //length of the S state
    static final int N = 256;
    //used to permute in the S state
    static int temp = 0;

    static String RC4(final String text, final String key){
        int [] S = new int[N];
        String keyStream = "";
        String result = "";
        S = KSA(S, key);
        keyStream = PRGA(S, keyStream, text.length());
        //initialization of ciphertext/decrypted text by XOR (^) operation between input/ciphertext and key stream
        for (int i = 0; i < text.length(); ++i) {
            result += (char)(text.charAt(i)^keyStream.charAt(i));
        }

        return result;
    }

    static String PRGA(int[] S, String keyStream, final int textLen){
        //initialization of iterators with an initial value
        int i = 0, j = 0;
        for (int k = 0; k < textLen; ++k) {
            //determination of new values ​​of iterators i and j
            i = (i + 1) % N;
            j = (j + S[i]) % N;
            //permutation in state S with iterators i and j using temp variable
            temp = S[i];
            S[i] = S[j];
            S[j] = temp;
            //key flow generation
            keyStream += (char) ((S[(S[i] + S[j]) % N]));
        }

        return  keyStream;
    }

    static int [] KSA(int[] S, final String key) {
        //initialization of the initial state S with values ​​from 0 to -1
        for (int i = 0; i < N; ++i) {
            S[i] = i;
        }
        //initialization of the iterator j with the initial value and length of the text kLen
        int j = 0;
        int kLen = key.length();
        //repeat (N-1) times the permutation in state S
        for (int i = 0; i < N; ++i) {
            //defining the new value of the iterator j
            j = (j + S[i] + (int)key.charAt(i % kLen)) % N;
            //permutation in state S with iterators i and j using a variable temp
            temp = S[i];
            S[i] = S[j];
            S[j] = temp;
        }

        return S;
    }

    public static void rc4() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the input text: ");
        String originaltext = in.next();
        System.out.print("Enter the key: ");
        String key = in.next();
        String cipherText = RC4(originaltext, key);
        String decryptText = RC4(cipherText, key);
        System.out.println("Result:");
        System.out.println("Encrypted text: " + cipherText);
        System.out.println("Decoded text: " + decryptText);
    }
}