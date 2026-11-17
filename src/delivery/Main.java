package delivery;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        new ConsoleUI(new OrderManager(), new ConsoleInput(scanner, out), out).start();
    }
}
