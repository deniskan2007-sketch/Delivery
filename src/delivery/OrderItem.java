package delivery;

import java.math.BigDecimal;

/** Неизменяемая позиция с точным денежным расчётом. */
public final class OrderItem {
    private final String name;
    private final int quantity;
    private final BigDecimal price;

    public OrderItem(String name, int quantity, BigDecimal price) {
        this.name = Validation.text(name, "Название товара");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным целым числом.");
        }
        this.quantity = quantity;
        this.price = Validation.price(price);
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getSubtotal() { return price.multiply(BigDecimal.valueOf(quantity)); }
}
