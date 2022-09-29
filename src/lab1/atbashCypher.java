package lab1;

public class atbashCypher {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String encrypt(String message )
    {
        StringBuilder newMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                int newChar = ('Z' - c) + 'A';
                newMessage.append((char) newChar);
            } else {
                newMessage.append(c);
            }
        }
        return newMessage.toString();
    }
}