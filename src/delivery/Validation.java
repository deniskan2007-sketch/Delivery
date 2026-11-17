package delivery;

import java.math.BigDecimal;

final class Validation {
    private Validation() { }

    static String text(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + ": значение не должно быть пустым.");
        }
        return value.strip();
    }

    static BigDecimal price(BigDecimal value) {
        if (value == null || value.signum() <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной.");
        }
        try {
            return value.setScale(2, java.math.RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Цена должна содержать не более двух знаков после запятой.");
        }
    }
}
