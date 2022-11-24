package lab2;

public class DESCipher {
    private static class DES {

        // hexadecimal to binary conversion
        String hextoBin(String input)
        {
            int n = input.length() * 4;
            input = Long.toBinaryString(
                    Long.parseUnsignedLong(input, 16));
            while (input.length() < n)
                input = "0" + input;
            return input;
        }

        // binary to hexadecimal conversion
        String binToHex(String input)
        {
            int n = (int)input.length() / 4;
            input = Long.toHexString(
                    Long.parseUnsignedLong(input, 2));
            while (input.length() < n)
                input = "0" + input;
            return input;
        }

        // permutate input hexadecimal according to specified sequence
        String permutation(int[] sequence, String input)
        {
            String output = "";
            input = hextoBin(input);
            for (int i = 0; i < sequence.length; i++)
                output += input.charAt(sequence[i] - 1);
            output = binToHex(output);
            return output;
        }

        // xor 2 hexadecimal strings
        String xor(String a, String b)
        {
            // hexadecimal to decimal(base 10)
            long t_a = Long.parseUnsignedLong(a, 16);
            // hexadecimal to decimal(base 10)
            long t_b = Long.parseUnsignedLong(b, 16);
            // xor
            t_a = t_a ^ t_b;
            // decimal to hexadecimal
            a = Long.toHexString(t_a);
            // prepend 0's to maintain length
            while (a.length() < b.length())
                a = "0" + a;
            return a;
        }

        // left Circular Shifting bits
        String leftCircularShift(String input, int numBits)
        {
            int n = input.length() * 4;
            int perm[] = new int[n];
            for (int i = 0; i < n - 1; i++)
                perm[i] = (i + 2);
            perm[n - 1] = 1;
            while (numBits-- > 0)
                input = permutation(perm, input);
            return input;
        }

        // preparing 16 keys for 16 rounds
        String[] getKeys(String key)
        {
            String keys[] = new String[16];
            // first key permutation
            key = permutation(constants.PC1, key);
            for (int i = 0; i < 16; i++) {
                key = leftCircularShift(key.substring(0, 7),
                        constants.shiftBits[i])
                        + leftCircularShift(
                        key.substring(7, 14),
                        constants.shiftBits[i]);
                // second key permutation
                keys[i] = permutation(constants.PC2, key);
            }
            return keys;
        }

        String sBox(String input)
        {
            String output = "";
            input = hextoBin(input);
            for (int i = 0; i < 48; i += 6) {
                String temp = input.substring(i, i + 6);
                int num = i / 6;
                int row = Integer.parseInt(
                        temp.charAt(0) + "" + temp.charAt(5),
                        2);
                int col = Integer.parseInt(
                        temp.substring(1, 5), 2);
                output += Integer.toHexString(
                        constants.sbox[num][row][col]);
            }
            return output;
        }

        String round(String input, String key, int num)
        {
            String left = input.substring(0, 8);
            String temp = input.substring(8, 16);
            String right = temp;
            // Expansion permutation
            temp = permutation(constants.EP, temp);
            // xor temp and round key
            temp = xor(temp, key);
            // lookup in s-box table
            temp = sBox(temp);
            // Straight D-box
            temp = permutation(constants.P, temp);
            // xor
            left = xor(left, temp);
            System.out.println("Round " + (num + 1) + " "
                    + right.toUpperCase() + " "
                    + left.toUpperCase() + " "
                    + key.toUpperCase());

            // swapper
            return right + left;
        }

        String encrypt(String plainText, String key)
        {
            int i;
            // get round keys
            String keys[] = getKeys(key);

            // initial permutation
            plainText = permutation(constants.IP, plainText);
            System.out.println("After initial permutation: "
                    + plainText.toUpperCase());
            System.out.println(
                    "After splitting: L0="
                            + plainText.substring(0, 8).toUpperCase()
                            + " R0="
                            + plainText.substring(8, 16).toUpperCase()
                            + "\n");

            // 16 rounds
            for (i = 0; i < 16; i++) {
                plainText = round(plainText, keys[i], i);
            }

            // 32-bit swap
            plainText = plainText.substring(8, 16)
                    + plainText.substring(0, 8);
            System.out.println("32bitswap: "+plainText);

            // final permutation
            plainText = permutation(constants.IP1, plainText);
            return plainText;
        }

        String decrypt(String plainText, String key)
        {
            int i;
            // get round keys
            String keys[] = getKeys(key);

            // initial permutation
            plainText = permutation(constants.IP, plainText);
            System.out.println("After initial permutation: "
                    + plainText.toUpperCase());
            System.out.println(
                    "After splitting: L0="
                            + plainText.substring(0, 8).toUpperCase()
                            + " R0="
                            + plainText.substring(8, 16).toUpperCase()
                            + "\n");

            // 16-rounds
            for (i = 15; i > -1; i--) {
                plainText
                        = round(plainText, keys[i], 15 - i);
            }

            // 32-bit swap
            plainText = plainText.substring(8, 16)
                    + plainText.substring(0, 8);
            plainText = permutation(constants.IP1, plainText);
            return plainText;
        }
    }

    public static void des()
    {
        String text = "596F7572214C6970";
        String key = "133457799BBCDFF1";

        DES cipher = new DES();
        System.out.println("Encryption:\n");
        text = cipher.encrypt(text, key);
        System.out.println(
                "\nCipher Text: " + text.toUpperCase() + "\n");
        System.out.println("Decryption\n");
        text = cipher.decrypt(text, key);
        System.out.println("\nPlain Text: "
                + text.toUpperCase());
    }
}
