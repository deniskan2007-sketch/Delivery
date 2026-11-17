package delivery;

import java.io.PrintStream;

/** Только представление данных, без изменения состояния. */
public final class OrderPrinter {
    private final PrintStream out;
    public OrderPrinter(PrintStream out) { this.out = out; }

    public void print(Order order) {
        out.println("=== Заказ №" + order.getId() + " ===");
        out.println("Статус: " + order.getStatus() + " — " + order.getStatus().getLabel());
        out.println("Клиент: " + order.getClient().getName() + ", " + order.getClient().getPhone());
        out.println("Адрес: " + order.getAddress());
        for (OrderItem item : order.getItems()) {
            out.println("  " + item.getName() + ": " + item.getQuantity() + " × "
                    + item.getPrice() + " = " + item.getSubtotal() + " руб.");
        }
        out.println("Товары: " + order.getItemsCost() + " руб.");
        DeliveryMethod method = order.getDeliveryMethod();
        if (method == null) { out.println("Доставка: не выбрана; сумма пока без доставки."); }
        else {
            out.println("Доставка: " + method.getName() + ", " + order.getDeliveryCost() + " руб.");
            out.println("Срок: " + method.getEstimatedTime());
        }
        out.println("Курьер: " + (order.getCourier() == null ? "не назначен" : order.getCourier().getName()));
        out.println("Итого: " + order.getTotalCost() + " руб.");
    }
}
