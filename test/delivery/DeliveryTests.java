package delivery;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/** Автономные проверки: исключение завершает процесс с ненулевым кодом. */
public final class DeliveryTests {
    private static int checks;
    private static void check(boolean value, String message) {
        checks++;
        if (!value) { throw new AssertionError(message); }
    }
    private static void fails(Runnable action) {
        checks++;
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException
                | UnsupportedOperationException | NullPointerException e) { return; }
        throw new AssertionError("Ожидался отказ операции");
    }
    private static OrderItem item() { return new OrderItem("Книга", 2, new BigDecimal("199.90")); }
    private static Order create(OrderManager manager) {
        return manager.createOrder(new Client("Иван", "+7 900 123-45-67"), "ЛЭТИ", List.of(item()));
    }
    public static void main(String[] args) {
        fails(() -> new Client(" ", "1"));
        fails(() -> new Client("Имя", " "));
        fails(() -> new OrderItem("Товар", 0, BigDecimal.ONE));
        fails(() -> new OrderItem("Товар", 1, new BigDecimal("-1")));
        fails(() -> new OrderItem("Товар", 1, new BigDecimal("1.001")));
        check(item().getSubtotal().equals(new BigDecimal("399.80")), "Точная сумма");
        OrderManager manager = new OrderManager();
        fails(() -> manager.createOrder(new Client("Иван", "1"), "Адрес", List.of()));
        Order a = create(manager);
        Order b = create(manager);
        check(a.getId() == 1 && b.getId() == 2, "Уникальные номера");
        fails(() -> manager.getOrder(999));
        fails(() -> manager.getOrders().clear());
        fails(() -> a.getItems().clear());
        fails(() -> a.changeStatus(OrderStatus.COMPLETED));
        fails(() -> a.changeStatus(OrderStatus.DELIVERING));
        fails(() -> a.assignCourier(manager.getCourier(1)));
        a.setDeliveryMethod(new StandardDelivery());
        check(a.getTotalCost().equals(new BigDecimal("699.80")), "Стандартная доставка");
        fails(() -> a.changeStatus(OrderStatus.DELIVERING));
        a.assignCourier(manager.getCourier(1));
        fails(() -> a.assignCourier(manager.getCourier(1)));
        b.setDeliveryMethod(new ExpressDelivery());
        check(b.getTotalCost().equals(new BigDecimal("1199.80")), "Экспресс");
        fails(() -> b.assignCourier(manager.getCourier(1)));
        b.assignCourier(manager.getCourier(2));
        fails(() -> a.assignCourier(manager.getCourier(2)));
        check(a.getCourier() == manager.getCourier(1), "Неудачная замена сохраняет курьера");
        a.setDeliveryMethod(new PickupDelivery());
        check(manager.getCourier(1).isAvailable() && a.getCourier() == null, "Освобождение при самовывозе");
        check(a.getTotalCost().equals(new BigDecimal("399.80")), "Бесплатный самовывоз");
        fails(() -> a.assignCourier(manager.getCourier(1)));
        a.changeStatus(OrderStatus.DELIVERING);
        fails(() -> a.addItem(item()));
        fails(() -> a.setDeliveryMethod(new StandardDelivery()));
        fails(() -> a.changeStatus(OrderStatus.CREATED));
        a.changeStatus(OrderStatus.COMPLETED);
        fails(() -> a.changeStatus(OrderStatus.COMPLETED));
        b.changeStatus(OrderStatus.DELIVERING);
        check(!manager.getCourier(2).isAvailable(), "Курьер занят в пути");
        b.changeStatus(OrderStatus.COMPLETED);
        check(manager.getCourier(2).isAvailable(), "Освобождение после завершения");
        Order c = create(manager);
        c.setDeliveryMethod(new StandardDelivery());
        c.assignCourier(manager.getCourier(1));
        c.assignCourier(manager.getCourier(2));
        check(manager.getCourier(1).isAvailable() && !manager.getCourier(2).isAvailable(), "Замена курьера");
        Order empty = new Order(100, new Client("А", "1"), "Адрес");
        empty.setDeliveryMethod(new PickupDelivery());
        fails(() -> empty.changeStatus(OrderStatus.DELIVERING));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        ConsoleInput input = new ConsoleInput(new Scanner("abc\n999999999999999999\n0\n2\n-1\nNaN\n1.001\n199,90\n"), out);
        check(input.integer("", 1, 3) == 2, "Восстановление после неверного целого");
        check(input.price("").equals(new BigDecimal("199.90")), "Проверка цены и запятая");
        OrderManager interrupted = new OrderManager();
        new ConsoleUI(interrupted, new ConsoleInput(new Scanner("1\nИмя\n"), out), out).start();
        check(interrupted.getOrders().isEmpty(), "EOF не оставляет частичный заказ");
        new ConsoleUI(new OrderManager(), new ConsoleInput(new Scanner("x\n2\n6\n"), out), out).start();
        check(buffer.toString(StandardCharsets.UTF_8).contains("Работа завершена"), "Явный выход после ошибки");
        System.out.println("PASS: " + checks + " checks");
    }
}
