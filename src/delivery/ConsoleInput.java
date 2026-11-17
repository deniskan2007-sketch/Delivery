package delivery;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Построчное чтение не оставляет перевод строки после числа. */
public final class ConsoleInput {
    private final Scanner scanner;
    private final PrintStream out;
    public ConsoleInput(Scanner scanner, PrintStream out) { this.scanner = scanner; this.out = out; }

    public String text(String prompt) {
        while (true) {
            out.print(prompt);
            if (!scanner.hasNextLine()) { throw new NoSuchElementException("Конец ввода"); }
            String value = scanner.nextLine().strip();
            if (!value.isBlank()) { return value; }
            out.println("Ошибка: пустое значение недопустимо.");
        }
    }
    public int integer(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(text(prompt));
                if (value >= min && value <= max) { return value; }
            } catch (NumberFormatException e) { /* Сообщение об ошибке ниже. */ }
            out.println("Ошибка: введите целое число от " + min + " до " + max + ".");
        }
    }
    public BigDecimal price(String prompt) {
        while (true) {
            String value = text(prompt).replace(',', '.');
            if (!value.matches("[0-9]+([.][0-9]{1,2})?")) {
                out.println("Ошибка: введите положительную цену, например 199,90.");
                continue;
            }
            try { return Validation.price(new BigDecimal(value)); }
            catch (IllegalArgumentException e) { out.println("Ошибка: " + e.getMessage()); }
        }
    }
}
