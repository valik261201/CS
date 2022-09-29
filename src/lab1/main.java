package lab1;

public abstract class main {

    public static void Main(String[] args) {

        caesarCypher code = new caesarCypher();
        code.setKey(5);
        code.setMessage("lets go");
        System.out.println("encrypted message: " + code.encrypt(code.getMessage(), code.getKey()));

    }
}
